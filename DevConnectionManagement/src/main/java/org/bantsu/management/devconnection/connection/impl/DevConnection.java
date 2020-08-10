package org.bantsu.management.devconnection.connection.impl;

import org.bantsu.management.devconnection.connection.IDevConnection;
import org.bantsu.management.devconnection.operator.IDevParaOperator;
import org.bantsu.management.devconnection.operator.impl.DevParaOperator;

public class DevConnection implements IDevConnection {
    private String host = null;
    private Integer port = null;

    private IDevParaOperator devParaOperator = null;

    public DevConnection(){};

    public DevConnection(String host, Integer port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public IDevParaOperator getDevParaOperator() {
        if (this.devParaOperator == null) {
            this.devParaOperator = new DevParaOperator(this);
        }
        return devParaOperator;
    }
}
