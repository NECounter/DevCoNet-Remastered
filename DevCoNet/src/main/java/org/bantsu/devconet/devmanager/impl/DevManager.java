package org.bantsu.devconet.devmanager.impl;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.bantsu.devconet.anno.resolver.impl.DevParaAnnotationResolver;
import org.bantsu.devconet.configuration.DevParaConfiguration;
import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.management.devconnection.connection.impl.DevConnection;
import org.bantsu.management.devconnection.connection.impl.DevConnectionBuilder;
import org.bantsu.management.devconnection.operator.impl.DevParaOperator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class DevManager implements IDevManager {
    private Map<String, DevParaConfiguration> devParaConfigurationMap = new HashMap<>();
    private DevParaAnnotationResolver devParaAnnotationResolver = null;
    private DevConnectionBuilder devConnectionBuilder = new DevConnectionBuilder();


    @Override
    public Object getEnhancedDevPara(Class c) {
        devParaAnnotationResolver = new DevParaAnnotationResolver(c);
        mergeConfigMap(devParaAnnotationResolver.getFieldAnnotation());
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(c);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            String methodName = method.getName();
            String methodType = methodName.substring(0,3);
            String paraName = methodName.substring(3);
            String paraNameFull = method.getDeclaringClass().getName() + "." + paraName;
            DevParaConfiguration configuration = devParaConfigurationMap.get(paraNameFull);
            DevConnection devConnection = (DevConnection)devConnectionBuilder.buildConnection(configuration.getHost(),configuration.getPort());
            DevParaOperator devParaOperator = (DevParaOperator)devConnection.getDevParaOperator();

            Object result = new Object();
            if(methodType.equals("get")){
                switch (configuration.getParaType()){
                    case Integer -> result = devParaOperator.getDWord(configuration.getSlot(),configuration.getOffset());
                    case Byte ->result = devParaOperator.getByte(configuration.getSlot(),configuration.getOffset());
                    case Boolean -> result = devParaOperator.getBit(configuration.getSlot(),configuration.getOffset(),configuration.getBitOffset());
                    case Float -> result = devParaOperator.getFloat(configuration.getSlot(),configuration.getOffset());
                    default -> result = new Object();
                }
                Field field = c.getDeclaredField(paraName);
                field.setAccessible(true);
                field.set(obj, result);
                return result;

            }else{
                switch (configuration.getParaType()){
                    case Integer -> result = devParaOperator.setDWord(configuration.getSlot(),configuration.getOffset(),(Integer)args[0]);
                    case Byte ->result = devParaOperator.setByte(configuration.getSlot(),configuration.getOffset(),(Byte) args[0]);
                    case Boolean -> result = devParaOperator.setBit(configuration.getSlot(),configuration.getOffset(),configuration.getBitOffset(), (Boolean) args[0]);
                    case Float -> result = devParaOperator.setFloat(configuration.getSlot(),configuration.getOffset(), (Float) args[0]);
                    default -> result = new Object();
                }
                proxy.invokeSuper(obj, args);
                return result;
            }

        });
        return enhancer.create();
    }

    private void mergeConfigMap(Map<String, DevParaConfiguration> configMap){
        for(Map.Entry<String, DevParaConfiguration> entry : configMap.entrySet()){
            devParaConfigurationMap.put(entry.getKey(), entry.getValue());
        }
    }
}
