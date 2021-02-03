package org.bantsu.devdatasource.api.connection.impl;

import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class DevConnectionSerial implements IDevConnection {
    private Integer port = null;
    private String operatorClassName = null;
    private volatile IDevParaOperator devParaOperator = null;

    public DevConnectionSerial() {
    }


    public DevConnectionSerial(Integer port, String operatorClassName) {
        this.port = port;
        this.operatorClassName = operatorClassName;
    }


    public Integer getPort() {
        return port;
    }

    public IDevParaOperator getDevParaOperator() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.devParaOperator == null) {
            synchronized (this){
                if (this.devParaOperator == null){
                    Class operatorClass = Class.forName(this.operatorClassName);
                    Constructor constructor = operatorClass.getConstructor(this.getClass());
                    this.devParaOperator = (IDevParaOperator)constructor.newInstance(new Object[]{this});
                    System.out.println("Create DevParaOperatorSerial");
                }
            }
        }
        return devParaOperator;
    }

}
