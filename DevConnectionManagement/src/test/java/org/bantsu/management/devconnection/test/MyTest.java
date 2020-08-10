package org.bantsu.management.devconnection.test;

import org.bantsu.management.devconnection.connection.impl.DevConnection;
import org.bantsu.management.devconnection.connection.impl.DevConnectionBuilder;
import org.bantsu.management.devconnection.operator.impl.DevParaOperator;
import org.bantsu.management.devconnection.utils.NetUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyTest {
    public static void main(String[] args) throws Exception {
        DevConnectionBuilder devConnectionBuilder = new DevConnectionBuilder();
        DevConnection devConnection = (DevConnection)devConnectionBuilder.buildConnection("127.0.0.1",8080);
        DevParaOperator devParaOperator = (DevParaOperator)devConnection.getDevParaOperator();

        System.out.println("Content--->" + devParaOperator.setDWord("M", 0, 1234));

        System.out.println("Content--->" + devParaOperator.getDWord("M", 0));

    }


}
