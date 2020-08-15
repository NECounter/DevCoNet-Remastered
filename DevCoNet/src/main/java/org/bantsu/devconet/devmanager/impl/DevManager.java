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

/**
 * A Device Manager mainly to enhance parameters and to control the interactions between host and devices.
 */
public class DevManager implements IDevManager {

    //region Local Fields
    /**
     * A map used to store the configs of parameters.
     */
    private Map<String, DevParaConfiguration> devParaConfigurationMap = new HashMap<>();

    /**
     * An AnnotationResolver
     */
    private DevParaAnnotationResolver devParaAnnotationResolver = null;

    /**
     * A map used to store the historical values of a parameter.
     */
    private ThreadLocal<Map<String, ValueHisPair>> changeBuffer = new ThreadLocal<>();

    /**
     * Determines whether the devManager supports the concurrent requests
     */
    private Boolean useThreadPool = false;

    /**
     * Thread pools to handle TCP and Serial connection respectively
     */
    private ExecutorService executorTCP = null;
    private ExecutorService executorSerial = null;
    //endregion

    //region Constructors
    /**
     * Construct a default devManager without concurrent support
     */
    public DevManager() {
        this.changeBuffer.set(new HashMap<>());

    }

    /**
     * Construct a concurrent devManager
     * @param coreSize the core pool size of a thread pool
     */
    public DevManager(Integer coreSize) {
        this.useThreadPool = true;
        this.executorTCP = Executors.newFixedThreadPool(coreSize);
        this.executorSerial = Executors.newSingleThreadExecutor();
        this.changeBuffer.set(new HashMap<>());
    }
    //endregion

    //region Getters and Setters
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
    //endregion

    //region Public Methods
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

    @Override
    public void dispose(){
        if(this.executorSerial != null && this.executorTCP != null){
            this.executorTCP.shutdown();
            this.executorSerial.shutdown();
        }
    }
    //endregion

    //region Private Methods

    /**
     * Determine which dynamic proxy(callback) to use
     * @param enhancer enhancer of cglib
     * @return enhancer with specific callback
     */
    private Enhancer devParaProxy(Enhancer enhancer){
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            //Check the StackTrace to determine whether this call is a call with transaction
           if(Arrays.stream(Thread.currentThread().getStackTrace()).anyMatch(
                    stackTraceElement -> stackTraceElement.getMethodName().equals("doCommitTransactionJob"))){
               // Do with transaction
                return this.devParaOperationWithTrans(obj, method, args, proxy);
            }else{
               // Do without transaction
                return this.devParaOperationWithoutTrans(obj, method, args, proxy);
            }
        });
        return enhancer;
    }


    /**
     * Callback function without transaction
     * @throws Throwable Thread: Interrupted or TimeExceed
     */
    private Object devParaOperationWithoutTrans(Object obj, Method method, Object[] args, MethodProxy proxy)
            throws Throwable {
        //Assemble necessary infos
        String methodName = method.getName();
        String methodType = methodName.substring(0,3);
        String paraName = methodName.substring(3);
        Class className = method.getDeclaringClass();
        String paraNameFull =className.getName() + "." + paraName;

        //Get configuration of this operated parameter
        DevParaConfiguration configuration = devParaConfigurationMap.get(paraNameFull);

        Object result = null;
        Future<Object> future = null;

        if(methodType.equals("get")){
            //Get value from devices
            if(this.useThreadPool){
                Callable<Object> getTask = new getParamTask(configuration);
                if(configuration.getConnectionType()== ConnectionType.TCP) {
                    future = executorTCP.submit(getTask);
                }
                if(configuration.getConnectionType()== ConnectionType.Serial){
                    future = executorSerial.submit(getTask);
                }
                assert future != null;
                result = future.get();
            }else{
                result = this.getParam(configuration);
            }

            //Set value to POJO instance using reflection
            Field field = className.getDeclaredField(paraName);
            field.setAccessible(true);
            field.set(obj, result);

        }else{
            //Set value to devices
            if(this.useThreadPool){
            Callable<Object> setTask = new setParamTask(configuration, args[0]);
                if(configuration.getConnectionType()== ConnectionType.TCP){
                    future = executorTCP.submit(setTask);
                }
                if(configuration.getConnectionType()== ConnectionType.Serial){
                    future = executorSerial.submit(setTask);
                }
                assert future != null;
                result = future.get();
            }else{
                result = setParam(configuration, args[0]);
            }
            //Set value to POJO instance
            proxy.invokeSuper(obj, args);
        }
        return result;
    }

    /**
     * Callback function with transaction
     * @throws Throwable Thread: Interrupted or TimeExceed
     */
    private Object devParaOperationWithTrans(Object obj, Method method, Object[] args, MethodProxy proxy)
            throws Throwable {
        //Assemble necessary infos
        String methodName = method.getName();
        String methodType = methodName.substring(0,3);
        String paraName = methodName.substring(3);
        Class className = method.getDeclaringClass();
        String paraNameFull =className.getName() + "." + paraName;

        if (methodType.equals("get")) {
            if(changeBuffer.get().get(paraNameFull) != null){
                //If the parameter has altered in this transaction, get value from POJO instance directly
                return proxy.invokeSuper(obj, args);
            }else{
                //If hasn't, get value from the device
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
                    assert future != null;
                    return future.get();
                }else{
                    return this.getParam(configuration);
                }

            }
        }else{
            //Set value to the POJO instance, and store their full qualified field name to changeBuffer
            Field field = className.getDeclaredField(paraName);
            field.setAccessible(true);
            Object formerValue = field.get(obj);
            changeBuffer.get().put(paraNameFull, new ValueHisPair(obj, formerValue, args[0]));
            return proxy.invokeSuper(obj, args);
        }
    }


    /**
     * Merge configs of new comers to the config map of devManager
     * @param configMap configMap of the new comer
     */
    private void mergeConfigMap(Map<String, DevParaConfiguration> configMap){
        for(Map.Entry<String, DevParaConfiguration> entry : configMap.entrySet()){
            devParaConfigurationMap.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Get parameter's value from device through configuration
     * @param configuration The configuration of a parameter
     * @return Value
     * @throws Exception IOException
     */
    private Object getParam(DevParaConfiguration configuration) throws Exception {
        //Get DevParaOperator from datasource stored in configuration
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

    /**
     * Set parameter's value to device through configuration
     * @param configuration The configuration of a parameter
     * @return Not defined
     * @throws Exception IOException
     */
    private Object setParam(DevParaConfiguration configuration, Object value) throws Exception {
        //Get DevParaOperator from datasource stored in configuration
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
    //endregion

    //region Inner Classes
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
    //endregion
}
