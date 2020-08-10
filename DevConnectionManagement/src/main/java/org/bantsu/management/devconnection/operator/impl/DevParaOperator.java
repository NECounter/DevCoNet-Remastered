package org.bantsu.management.devconnection.operator.impl;

import org.bantsu.management.devconnection.connection.impl.DevConnection;
import org.bantsu.management.devconnection.operator.IDevParaOperator;
import org.bantsu.management.devconnection.utils.NetUtils;

import java.io.IOException;

public class DevParaOperator implements IDevParaOperator {

    private DevConnection connection = null;
    private String url = null;

    public DevParaOperator(DevConnection connection) {
        this.connection = connection;
        this.url = "http://" +this.connection.getHost()+":" + this.connection.getPort()+"/";
    }

    public DevConnection getConnection() {
        return connection;
    }

    public String getUrl() {
        return url;
    }


    @Override
    public Boolean getBit(String slot, int offset, int bitOffset) throws IOException {
        String url = this.url + "getBit";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&" +
                "bitOffset=" + bitOffset;

        return Boolean.valueOf(NetUtils.sendGet(url, param,null));
    }

    @Override
    public Boolean setBit(String slot, int offset, int bitOffset, boolean value) throws IOException {
        String url = this.url + "setBit";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&" +
                "value=" + value + "&" +
                "bitOffset=" + bitOffset;
        return Boolean.valueOf(NetUtils.sendGet(url, param,null));
    }

    @Override
    public Byte getByte(String slot, int offset) throws IOException {
        String url = this.url + "getByte";
        String param = "slot=" + slot + "&" +
                "offset=" + offset;
        return Byte.valueOf(NetUtils.sendGet(url, param,null));
    }

    @Override
    public Boolean setByte(String slot, int offset, byte value) throws IOException {
        String url = this.url + "setByte";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&" +
                "value=" + value;
        return Boolean.valueOf(NetUtils.sendGet(url, param,null));
    }

    @Override
    public Integer getDWord(String slot, int offset) throws IOException {

        String url = this.url + "getDWord";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&";
        return Integer.valueOf(NetUtils.sendGet(url, param,null));
    }

    @Override
    public Boolean setDWord(String slot, int offset, int value) throws IOException {
        String url = this.url + "setDWord";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&" +
                "value=" + value;
        return Boolean.valueOf(NetUtils.sendGet(url, param,null));
    }

    @Override
    public Float getFloat(String slot, int offset) throws IOException {

        String url = this.url + "getFloat";
        String param = "slot=" + slot + "&" +
                "offset=" + offset;
        return Float.valueOf(NetUtils.sendGet(url, param,null));
    }

    @Override
    public Boolean setFloat(String slot, int offset, float value) throws IOException {
        String url = this.url + "setFloat";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&" +
                "value=" + value;
        return Boolean.valueOf(NetUtils.sendGet(url, param,null));
    }
}
