package org.bantsu.devdatasource.api.datasource.impl;

import org.bantsu.devdatasource.api.configuration.ConnectionType;
import org.bantsu.devdatasource.api.configuration.SerialPortConfig;
import org.bantsu.devdatasource.api.configuration.TCPConfig;
import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.connection.impl.DevConnectionBuilder;
import org.bantsu.devdatasource.api.datasource.IDevDataSource;


public class DefaultDevDataSource implements IDevDataSource {
    private DevConnectionBuilder devConnectionBuilder = null;
    private TCPConfig tcpConfig = null;
    private SerialPortConfig serialPortConfig = null;
    private String operatorClassName = null;

    public DefaultDevDataSource(TCPConfig tcpConfig, SerialPortConfig serialPortConfig, String operatorClassName) {
        this.tcpConfig = tcpConfig;
        this.serialPortConfig = serialPortConfig;
        this.operatorClassName = operatorClassName;
        this.devConnectionBuilder = new DevConnectionBuilder(operatorClassName);
    }

    @Override
    public IDevConnection getConnection(ConnectionType connectionType) throws Exception {
        IDevConnection devConnection;
        switch (connectionType){
            case TCP -> devConnection = devConnectionBuilder.buildTCPConnection(tcpConfig);
            case Serial -> devConnection = devConnectionBuilder.buildSerialConnection(serialPortConfig);
            default -> devConnection = null;
        }
        return devConnection;
    }
}
