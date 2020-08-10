package org.bantsu.management.devconnection.connection.impl;

import org.bantsu.management.devconnection.connection.IDevConnection;
import org.bantsu.management.devconnection.connection.IDevConnectionBuilder;
import org.bantsu.management.devconnection.utils.NetUtils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DevConnectionBuilder implements IDevConnectionBuilder {
    private final Map<String, DevConnection> connectionPool;

    public DevConnectionBuilder() {
        this.connectionPool = new HashMap<String, DevConnection>();
    }

    public IDevConnection buildConnection(String host, Integer port) throws Exception {
        if(host != null && port != null){
            if(NetUtils.isValidConnection(host, port)){
                DevConnection connectionCache = connectionPool.get(host);
                if(connectionCache == null){
                    DevConnection connection = new DevConnection(host, port);
                    connectionPool.put(host, connection);
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

}
