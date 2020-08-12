package org.bantsu.devdatasource.devsim.connection;

import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;
import org.bantsu.devdatasource.devsim.operator.DevSimParaOperatorTCP;

public class DevConnectionTCP implements IDevConnection {
    private String host = null;
    private Integer port = null;

    private IDevParaOperator devParaOperator = null;

    public DevConnectionTCP(){};

    public DevConnectionTCP(String host, Integer port){
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
            this.devParaOperator = new DevSimParaOperatorTCP(this);
        }
        return devParaOperator;
    }
}
