package org.bantsu.devdatasource.devsim.connection;

import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;
import org.bantsu.devdatasource.devsim.operator.DevSimParaOperatorSerial;
import org.bantsu.devdatasource.devsim.operator.DevSimParaOperatorTCP;

/**
 *
 */
public class DevConnectionSerial implements IDevConnection {

    private Integer port = null;

    private IDevParaOperator devParaOperator = null;

    public DevConnectionSerial() {
    }


    public DevConnectionSerial(Integer port) {
        this.port = port;
    }


    public Integer getPort() {
        return port;
    }

    public IDevParaOperator getDevParaOperator() {
        if (this.devParaOperator == null) {
            this.devParaOperator = new DevSimParaOperatorSerial(this);
        }
        return devParaOperator;
    }

}
