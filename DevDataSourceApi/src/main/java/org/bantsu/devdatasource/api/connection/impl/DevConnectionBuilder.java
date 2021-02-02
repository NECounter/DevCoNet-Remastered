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

    public IDevConnection buildTCPConnection(TCPConfig tcpConfig) throws Exception {
        String host = tcpConfig.getIp();
        Integer port = tcpConfig.getPort();
        if(host != null && port != null){
            if(NetUtils.isValidConnection(host, port)){
                DevConnectionTCP connectionCache = connectionPoolTCP.get(host);
                if(connectionCache == null){
                    DevConnectionTCP connection = new DevConnectionTCP(host, port, this.operatorClassName + ".operator.OperatorTCP");
                    connectionPoolTCP.put(host, connection);
                    System.out.println("Create a connection");
                    return connection;
                }else{
                    System.out.println("Use a pooled connection");
                    return connectionCache;
                }
            }else {
                throw new TimeoutException();
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
