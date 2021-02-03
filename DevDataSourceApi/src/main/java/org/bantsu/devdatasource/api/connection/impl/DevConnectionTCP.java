package org.bantsu.devdatasource.api.connection.impl;

import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class DevConnectionTCP implements IDevConnection {
    private String host = null;
    private Integer port = null;

    private volatile IDevParaOperator devParaOperator = null;
    private String operatorClassName = null;


    private Object TCPConnection = null;

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

    public Object getTCPConnection() {
        return TCPConnection;
    }

    public void setTCPConnection(Object TCPConnection) {
        if (this.TCPConnection == null){
            this.TCPConnection = TCPConnection;
        }

    }

    public IDevParaOperator getDevParaOperator() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.devParaOperator == null) {
            synchronized (this){
                if (this.devParaOperator == null){
                    Class operatorClass = Class.forName(this.operatorClassName);
                    Constructor constructor = operatorClass.getConstructor(this.getClass());
                    this.devParaOperator = (IDevParaOperator)constructor.newInstance(new Object[]{this});
                    System.out.println("Create DevParaOperatorTCP");
                }
            }
        }
        return devParaOperator;
    }


}
