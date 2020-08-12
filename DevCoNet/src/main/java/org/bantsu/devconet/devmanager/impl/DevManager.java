package org.bantsu.devconet.devmanager.impl;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.bantsu.devconet.anno.resolver.impl.DevParaAnnotationResolver;
import org.bantsu.devconet.configuration.DevParaConfiguration;
import org.bantsu.devconet.configuration.ValueHisPair;
import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;
import org.bantsu.devdatasource.devsim.connection.DevConnectionBuilder;
import org.bantsu.devdatasource.devsim.connection.DevConnectionTCP;
import org.bantsu.devdatasource.devsim.operator.DevSimParaOperatorTCP;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class DevManager implements IDevManager {
    private Map<String, DevParaConfiguration> devParaConfigurationMap = new HashMap<>();
    private DevParaAnnotationResolver devParaAnnotationResolver = null;
    private DevConnectionBuilder devConnectionBuilder = new DevConnectionBuilder();
    private ThreadLocal<Map<String, ValueHisPair>> changeBuffer = null;

    public Map<String, DevParaConfiguration> getDevParaConfigurationMap() {
        return devParaConfigurationMap;
    }

    public void setDevParaConfigurationMap(Map<String, DevParaConfiguration> devParaConfigurationMap) {
        this.devParaConfigurationMap = devParaConfigurationMap;
    }

    public DevParaAnnotationResolver getDevParaAnnotationResolver() {
        return devParaAnnotationResolver;
    }

    public void setDevParaAnnotationResolver(DevParaAnnotationResolver devParaAnnotationResolver) {
        this.devParaAnnotationResolver = devParaAnnotationResolver;
    }

    public DevConnectionBuilder getDevConnectionBuilder() {
        return devConnectionBuilder;
    }

    public void setDevConnectionBuilder(DevConnectionBuilder devConnectionBuilder) {
        this.devConnectionBuilder = devConnectionBuilder;
    }

    public ThreadLocal<Map<String, ValueHisPair>> getChangeBuffer() {
        return changeBuffer;
    }

    public void setChangeBuffer(ThreadLocal<Map<String, ValueHisPair>> changeBuffer) {
        this.changeBuffer = changeBuffer;
    }

    @Override
    public Object getEnhancedDevPara(Class c) throws Exception {
        devParaAnnotationResolver = new DevParaAnnotationResolver(c);
        mergeConfigMap(devParaAnnotationResolver.getFieldAnnotation());
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(c);
        return devParaProxy(enhancer).create();
    }

    @Override
    public void rollbackChangeBuffer() throws Exception {
        for(Map.Entry<String, ValueHisPair> entry : this.changeBuffer.get().entrySet()){
            Object obj = entry.getValue().getObj();
            Class className = Class.forName(obj.getClass().getName().split("\\$\\$")[0]);
            String[] data = entry.getKey().split("\\.");
            String paraName = data[ data.length-1];
            Field field = className.getDeclaredField(paraName);
            field.setAccessible(true);
            field.set(obj, entry.getValue().getFormerValue());
        }
        this.changeBuffer.get().clear();
        this.changeBuffer.remove();
    }

    @Override
    public void updateChangeBuffer() throws Exception {
        for(Map.Entry<String, ValueHisPair> entry : this.changeBuffer.get().entrySet()){
            DevParaConfiguration configuration = devParaConfigurationMap.get(entry.getKey());
            IDevParaOperator devParaOperator = configuration.getDataSource().getConnection(configuration.getConnectionType()).getDevParaOperator();

            switch (configuration.getParaType()){
                case Integer -> devParaOperator.setDWord(configuration.getSlot(),configuration.getOffset(),(Integer) entry.getValue().getCurrentValue());
                case Byte -> devParaOperator.setByte(configuration.getSlot(),configuration.getOffset(),(Byte) entry.getValue().getCurrentValue());
                case Boolean -> devParaOperator.setBit(configuration.getSlot(),configuration.getOffset(),configuration.getBitOffset(), (Boolean) entry.getValue().getCurrentValue());
                case Float -> devParaOperator.setFloat(configuration.getSlot(),configuration.getOffset(), (Float) entry.getValue().getCurrentValue());
            }
        }
        this.changeBuffer.get().clear();
        this.changeBuffer.remove();
    }

    private Enhancer devParaProxy(Enhancer enhancer){
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
           if(Arrays.stream(Thread.currentThread().getStackTrace()).anyMatch(
                    stackTraceElement -> stackTraceElement.getMethodName().equals("doCommitTransactionJob"))){
                return this.devParaOperationWithTrans(obj, method, args, proxy);
            }else{
                return this.devParaOperationWithoutTrans(obj, method, args, proxy);
            }
        });
        return enhancer;
    }

    private Object devParaOperationWithoutTrans(Object obj, Method method, Object[] args, MethodProxy proxy)
            throws Throwable {
        String methodName = method.getName();
        String methodType = methodName.substring(0,3);
        String paraName = methodName.substring(3);
        Class className = method.getDeclaringClass();
        String paraNameFull =className.getName() + "." + paraName;
        DevParaConfiguration configuration = devParaConfigurationMap.get(paraNameFull);
        IDevParaOperator devParaOperator = configuration.getDataSource().getConnection(configuration.getConnectionType()).getDevParaOperator();

        Object result;
        if(methodType.equals("get")){
            switch (configuration.getParaType()){
                case Integer -> result = devParaOperator.getDWord(configuration.getSlot(),configuration.getOffset());
                case Byte ->result = devParaOperator.getByte(configuration.getSlot(),configuration.getOffset());
                case Boolean -> result = devParaOperator.getBit(configuration.getSlot(),configuration.getOffset(),configuration.getBitOffset());
                case Float -> result = devParaOperator.getFloat(configuration.getSlot(),configuration.getOffset());
                default -> result = new Object();
            }
            Field field = className.getDeclaredField(paraName);
            field.setAccessible(true);
            field.set(obj, result);

        }else{
            switch (configuration.getParaType()){
                case Integer -> result = devParaOperator.setDWord(configuration.getSlot(),configuration.getOffset(),(Integer)args[0]);
                case Byte ->result = devParaOperator.setByte(configuration.getSlot(),configuration.getOffset(),(Byte) args[0]);
                case Boolean -> result = devParaOperator.setBit(configuration.getSlot(),configuration.getOffset(),configuration.getBitOffset(), (Boolean) args[0]);
                case Float -> result = devParaOperator.setFloat(configuration.getSlot(),configuration.getOffset(), (Float) args[0]);
                default -> result = new Object();
            }
            proxy.invokeSuper(obj, args);
        }
        return result;
    }

    private Object devParaOperationWithTrans(Object obj, Method method, Object[] args, MethodProxy proxy)
            throws Throwable {

        String methodName = method.getName();
        String methodType = methodName.substring(0,3);
        String paraName = methodName.substring(3);
        Class className = method.getDeclaringClass();
        String paraNameFull =className.getName() + "." + paraName;

        if (methodType.equals("get")) {
            if(changeBuffer.get().get(paraNameFull) != null){
                return proxy.invokeSuper(obj, args);
            }else{
                DevParaConfiguration configuration = devParaConfigurationMap.get(paraNameFull);
                IDevParaOperator devParaOperator = configuration.getDataSource().getConnection(configuration.getConnectionType()).getDevParaOperator();
                Object result = new Object();
                switch (configuration.getParaType()){
                    case Integer -> result = devParaOperator.getDWord(configuration.getSlot(),configuration.getOffset());
                    case Byte ->result = devParaOperator.getByte(configuration.getSlot(),configuration.getOffset());
                    case Boolean -> result = devParaOperator.getBit(configuration.getSlot(),configuration.getOffset(),configuration.getBitOffset());
                    case Float -> result = devParaOperator.getFloat(configuration.getSlot(),configuration.getOffset());
                }
                return result;
            }
        }else{
            Field field = className.getDeclaredField(paraName);
            field.setAccessible(true);
            Object formerValue = field.get(obj);
            changeBuffer.get().put(paraNameFull, new ValueHisPair(obj, formerValue, args[0]));
            return proxy.invokeSuper(obj, args);
        }
    }


    private void mergeConfigMap(Map<String, DevParaConfiguration> configMap){
        for(Map.Entry<String, DevParaConfiguration> entry : configMap.entrySet()){
            devParaConfigurationMap.put(entry.getKey(), entry.getValue());
        }
    }
}
