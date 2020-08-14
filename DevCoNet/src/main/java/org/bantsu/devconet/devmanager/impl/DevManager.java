package org.bantsu.devconet.devmanager.impl;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.bantsu.devconet.anno.resolver.impl.DevParaAnnotationResolver;
import org.bantsu.devconet.configuration.DevParaConfiguration;
import org.bantsu.devconet.configuration.ValueHisPair;
import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devdatasource.api.configuration.ConnectionType;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class DevManager implements IDevManager {
    private Map<String, DevParaConfiguration> devParaConfigurationMap = new HashMap<>();
    private DevParaAnnotationResolver devParaAnnotationResolver = null;
    private ThreadLocal<Map<String, ValueHisPair>> changeBuffer = null;

    private Boolean useThreadPool = false;

    private ExecutorService executorTCP = null;

    private ExecutorService executorSerial = null;

    public DevManager() {

    }

    public DevManager(Boolean useThreadPool, Integer coreSize) {
        this.useThreadPool = useThreadPool;
        this.executorTCP = Executors.newFixedThreadPool(coreSize);
        this.executorSerial = Executors.newSingleThreadExecutor();
    }

    public void dispose(){
        if(this.executorSerial != null && this.executorTCP != null){
            this.executorTCP.shutdown();
            this.executorSerial.shutdown();
        }
    }

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


    public ThreadLocal<Map<String, ValueHisPair>> getChangeBuffer() {
        return changeBuffer;
    }

    public void setChangeBuffer(ThreadLocal<Map<String, ValueHisPair>> changeBuffer) {
        this.changeBuffer = changeBuffer;
    }

    @Override
    public Object getEnhancedDevPara(Class c) throws Exception {
        devParaAnnotationResolver = new DevParaAnnotationResolver(c);
        mergeConfigMap(devParaAnnotationResolver.getDevParaConfigMap());
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
            if(this.useThreadPool){
                Callable<Object> setTask = new setParamTask(configuration, entry.getValue().getCurrentValue());
                if(configuration.getConnectionType()== ConnectionType.TCP){
                    executorTCP.submit(setTask);
                }
                if(configuration.getConnectionType()== ConnectionType.Serial){
                    executorSerial.submit(setTask);
                }
            }else{
                this.setParam(configuration, entry.getValue().getCurrentValue());
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

        Object result = null;
        Future<Object> future = null;
        if(methodType.equals("get")){
            if(this.useThreadPool){
                Callable<Object> getTask = new getParamTask(configuration);
                if(configuration.getConnectionType()== ConnectionType.TCP) {
                    future = executorTCP.submit(getTask);
                }
                if(configuration.getConnectionType()== ConnectionType.Serial){
                    future = executorSerial.submit(getTask);
                }
                result = future.get();
            }else{
                result = this.getParam(configuration);
            }

            Field field = className.getDeclaredField(paraName);
            field.setAccessible(true);
            field.set(obj, result);

        }else{
            if(this.useThreadPool){
            Callable<Object> setTask = new setParamTask(configuration, args[0]);
                if(configuration.getConnectionType()== ConnectionType.TCP){
                    future = executorTCP.submit(setTask);
                }
                if(configuration.getConnectionType()== ConnectionType.Serial){
                    future = executorSerial.submit(setTask);
                }
                result = future.get();
            }else{
                result = setParam(configuration, args[0]);
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
                Future<Object> future = null;
                DevParaConfiguration configuration = devParaConfigurationMap.get(paraNameFull);
                if(this.useThreadPool){
                    Callable<Object> getTask = new getParamTask(configuration);
                    if(configuration.getConnectionType()== ConnectionType.TCP){
                        future = executorTCP.submit(getTask);
                    }
                    if(configuration.getConnectionType()== ConnectionType.Serial){
                        future = executorSerial.submit(getTask);
                    }
                    return future.get();
                }else{
                    return this.getParam(configuration);
                }

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

    private Object getParam(DevParaConfiguration configuration) throws Exception {
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

    private Object setParam(DevParaConfiguration configuration, Object value) throws Exception {
        IDevParaOperator devParaOperator = configuration.getDataSource().getConnection(configuration.getConnectionType()).getDevParaOperator();
        Object result = new Object();
        switch (configuration.getParaType()){
            case Integer -> result = devParaOperator.setDWord(configuration.getSlot(),configuration.getOffset(),(Integer) value);
            case Byte ->result = devParaOperator.setByte(configuration.getSlot(),configuration.getOffset(),(Byte) value);
            case Boolean -> result = devParaOperator.setBit(configuration.getSlot(),configuration.getOffset(),configuration.getBitOffset(), (Boolean) value);
            case Float -> result = devParaOperator.setFloat(configuration.getSlot(),configuration.getOffset(), (Float) value);
            default -> result = new Object();
        }
        return result;
    }

    class getParamTask implements Callable<Object> {
        private DevParaConfiguration configuration = null;

        public getParamTask(DevParaConfiguration configuration) {
            this.configuration = configuration;
        }

        @Override
        public Object call() throws Exception {
            return getParam(configuration);
        }
    }

    class setParamTask implements Callable<Object> {
        private DevParaConfiguration configuration = null;
        private Object value = null;

        public setParamTask(DevParaConfiguration configuration, Object value) {
            this.configuration = configuration;
            this.value = value;
        }

        @Override
        public Object call() throws Exception {
           return setParam(configuration, value);
        }
    }

}
