package org.bantsu.devdatasource.socketsource.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketUtils {
    public static String request(String host, int port, String cmd) throws IOException {
        Socket socket = new Socket(host, port);
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();
        socket.getOutputStream().write(cmd.getBytes(StandardCharsets.UTF_8));
        //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
        }
        inputStream.close();
        outputStream.close();
        socket.close();
        return sb.toString();
    }

}
