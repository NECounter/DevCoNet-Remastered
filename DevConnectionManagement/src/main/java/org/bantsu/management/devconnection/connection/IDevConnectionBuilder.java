package org.bantsu.management.devconnection.connection;

public interface IDevConnectionBuilder {
    IDevConnection buildConnection(String host, Integer port) throws Exception;
}
