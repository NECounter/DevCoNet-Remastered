package org.bantsu.devdatasource.socketsource.utils;

import java.io.*;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketRequestHandler {
    private Socket socket = null;

    public SocketRequestHandler(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public String request(String cmd) throws IOException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write(cmd);
        bufferedWriter.flush();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return bufferedReader.readLine();
    }
}
