package org.bantsu.devdatasource.api.connection.impl;

import org.bantsu.devdatasource.api.configuration.SerialPortConfig;
import org.bantsu.devdatasource.api.configuration.TCPConfig;
import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.connection.IDevConnectionBuilder;
import org.bantsu.devdatasource.api.utils.NetUtils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.zip.CheckedOutputStream;

public class DevConnectionBuilder implements IDevConnectionBuilder {
    private final Map<String, DevConnectionTCP> connectionPoolTCP;
    private final Map<String, DevConnectionSerial> connectionPoolSerial;

    private String operatorClassName = null;

    public DevConnectionBuilder(String operatorClassName) {
        this.connectionPoolTCP = new HashMap<>();
        this.connectionPoolSerial = new HashMap<>();
        this.operatorClassName = operatorClassName;

    }

    public IDevConnection buildTCPConnection(TCPConfig tcpConfig){
        String host = tcpConfig.getIp();
        Integer port = tcpConfig.getPort();
        String addr = host + ":" + port;
        if(host != null && port != null){
            DevConnectionTCP connectionCache = connectionPoolTCP.get(addr);
            if(connectionCache == null){
                DevConnectionTCP connection = new DevConnectionTCP(host, port, this.operatorClassName + ".operator.OperatorTCP");
                connectionPoolTCP.put(addr, connection);
                System.out.println("Create a TCPConnection, addr: " + addr);
                return connection;
            }else{
                return connectionCache;
            }
        }else{
            throw new InvalidParameterException();
        }
    }


    @Override
    public IDevConnection buildSerialConnection(SerialPortConfig serialPortConfig) throws Exception {
        return new DevConnectionSerial(serialPortConfig.getPort(), this.operatorClassName + ".operator.OperatorSerial");
    }
}