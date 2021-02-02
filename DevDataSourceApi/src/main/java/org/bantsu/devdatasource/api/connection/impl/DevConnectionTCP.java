package org.bantsu.devdatasource.api.connection.impl;

import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class DevConnectionTCP implements IDevConnection {
    private String host = null;
    private Integer port = null;

    private IDevParaOperator devParaOperator = null;
    private String operatorClassName = null;

    public DevConnectionTCP(){};

    public DevConnectionTCP(String host, Integer port, String operatorClassName){
        this.host = host;
        this.port = port;
        this.operatorClassName = operatorClassName;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public IDevParaOperator getDevParaOperator() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.devParaOperator == null) {
            Class operatorClass = Class.forName(this.operatorClassName);
            Constructor constructor = operatorClass.getConstructor(this.getClass());
            this.devParaOperator = (IDevParaOperator)constructor.newInstance(new Object[]{this});
        }
        return devParaOperator;
    }


}
