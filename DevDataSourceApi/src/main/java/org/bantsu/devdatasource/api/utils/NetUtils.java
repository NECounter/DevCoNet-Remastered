package org.bantsu.devdatasource.api.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class NetUtils {
    public static boolean isValidConnection(String ip, Integer port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port),1000);
        } catch (SocketTimeoutException s) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
