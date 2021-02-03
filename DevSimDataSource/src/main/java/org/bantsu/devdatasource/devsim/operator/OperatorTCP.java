package org.bantsu.devdatasource.devsim.operator;

import org.bantsu.devdatasource.api.connection.impl.DevConnectionTCP;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;
import org.bantsu.devdatasource.devsim.utils.HttpUtils;

import java.io.IOException;

public class OperatorTCP implements IDevParaOperator {

    private DevConnectionTCP connection = null;
    private String url = null;

    public OperatorTCP(DevConnectionTCP connection) {
        this.connection = connection;
        this.url = "http://" +this.connection.getHost()+":" + this.connection.getPort()+"/";
    }

    public DevConnectionTCP getConnection() {
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

        return Boolean.valueOf(HttpUtils.sendGet(url, param,null));
    }

    @Override
    public Boolean setBit(String slot, int offset, int bitOffset, boolean value) throws IOException {
        String url = this.url + "setBit";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&" +
                "value=" + value + "&" +
                "bitOffset=" + bitOffset;
        return Boolean.valueOf(HttpUtils.sendGet(url, param,null));
    }

    @Override
    public Byte getByte(String slot, int offset) throws IOException {
        String url = this.url + "getByte";
        String param = "slot=" + slot + "&" +
                "offset=" + offset;
        return Byte.valueOf(HttpUtils.sendGet(url, param,null));
    }

    @Override
    public Boolean setByte(String slot, int offset, byte value) throws IOException {
        String url = this.url + "setByte";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&" +
                "value=" + value;
        return Boolean.valueOf(HttpUtils.sendGet(url, param,null));
    }

    @Override
    public Integer getDWord(String slot, int offset) throws IOException {

        String url = this.url + "getDWord";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&";
        return Integer.valueOf(HttpUtils.sendGet(url, param,null));
    }

    @Override
    public Boolean setDWord(String slot, int offset, int value) throws IOException {
        String url = this.url + "setDWord";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&" +
                "value=" + value;
        return Boolean.valueOf(HttpUtils.sendGet(url, param,null));
    }

    @Override
    public Float getFloat(String slot, int offset) throws IOException {

        String url = this.url + "getFloat";
        String param = "slot=" + slot + "&" +
                "offset=" + offset;
        return Float.valueOf(HttpUtils.sendGet(url, param,null));
    }

    @Override
    public Boolean setFloat(String slot, int offset, float value) throws IOException {
        String url = this.url + "setFloat";
        String param = "slot=" + slot + "&" +
                "offset=" + offset + "&" +
                "value=" + value;
        return Boolean.valueOf(HttpUtils.sendGet(url, param,null));
    }
}
