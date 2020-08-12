package org.bantsu.devdatasource.devsim.datasource;

import org.bantsu.devdatasource.api.configuration.ConnectionType;
import org.bantsu.devdatasource.api.configuration.SerialPortConfig;
import org.bantsu.devdatasource.api.configuration.TCPConfig;
import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.datasource.IDevDataSource;
import org.bantsu.devdatasource.devsim.connection.DevConnectionBuilder;

public class DevDataSource implements IDevDataSource {
    private final DevConnectionBuilder devConnectionBuilder = new DevConnectionBuilder();

    private TCPConfig tcpConfig = null;
    private SerialPortConfig serialPortConfig = null;

    public DevDataSource(TCPConfig tcpConfig, SerialPortConfig serialPortConfig) {
        this.tcpConfig = tcpConfig;
        this.serialPortConfig = serialPortConfig;
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
