package org.bantsu.devdatasource.api.connection;

import org.bantsu.devdatasource.api.configuration.SerialPortConfig;
import org.bantsu.devdatasource.api.configuration.TCPConfig;

public interface IDevConnectionBuilder {
    IDevConnection buildTCPConnection(TCPConfig tcpConfig) throws Exception;
    IDevConnection buildSerialConnection(SerialPortConfig serialPortConfig) throws Exception;
}
