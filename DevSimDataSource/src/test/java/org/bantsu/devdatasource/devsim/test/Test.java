package org.bantsu.devdatasource.devsim.test;

import org.bantsu.devdatasource.api.configuration.ConnectionType;
import org.bantsu.devdatasource.api.configuration.SerialPortConfig;
import org.bantsu.devdatasource.api.configuration.TCPConfig;
import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.datasource.IDevDataSource;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;

import java.lang.reflect.Constructor;

public class Test {
    public static void main(String[] args) throws Exception {
        TCPConfig tcpConfig = new TCPConfig();
        tcpConfig.setIp("127.0.0.1");
        tcpConfig.setPort(8080);
        Class dataSourceClass = Class.forName("org.bantsu.devdatasource.devsim.datasource.DevDataSource");
        Constructor constructor = dataSourceClass.getConstructor(new Class[]{TCPConfig.class, SerialPortConfig.class});

        IDevDataSource dataSource = (IDevDataSource) constructor.newInstance(new Object[] {tcpConfig,null});
        IDevConnection devConnection = dataSource.getConnection(ConnectionType.TCP);
        IDevParaOperator devParaOperator = devConnection.getDevParaOperator();
        System.out.println(devParaOperator.getFloat("M",8));
    }
}
