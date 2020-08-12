package org.bantsu.devdatasource.devsim.connection;

import org.bantsu.devdatasource.api.configuration.SerialPortConfig;
import org.bantsu.devdatasource.api.configuration.TCPConfig;
import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.connection.IDevConnectionBuilder;
import org.bantsu.devdatasource.devsim.utils.NetUtils;

import javax.sound.sampled.Port;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DevConnectionBuilder implements IDevConnectionBuilder {
    private final Map<String, DevConnectionTCP> connectionPoolTCP;
    private final Map<String, DevConnectionSerial> connectionPoolSerial;

    public DevConnectionBuilder() {
        this.connectionPoolTCP = new HashMap<>();
        this.connectionPoolSerial = new HashMap<>();

    }

    public IDevConnection buildTCPConnection(TCPConfig tcpConfig) throws Exception {
        String host = tcpConfig.getIp();
        Integer port = tcpConfig.getPort();
        if(host != null && port != null){
            if(NetUtils.isValidConnection(host, port)){
                DevConnectionTCP connectionCache = connectionPoolTCP.get(host);
                if(connectionCache == null){
                    DevConnectionTCP connection = new DevConnectionTCP(host, port);
                    connectionPoolTCP.put(host, connection);
                    return connection;
                }else{
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
        return null;
    }
}
