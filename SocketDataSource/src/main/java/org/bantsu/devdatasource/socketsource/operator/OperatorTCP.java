package org.bantsu.devdatasource.socketsource.operator;

import org.bantsu.devdatasource.api.connection.impl.DevConnectionSerial;
import org.bantsu.devdatasource.api.connection.impl.DevConnectionTCP;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;
import org.bantsu.devdatasource.socketsource.utils.SocketUtils;

import java.io.IOException;

//todo: Socket Connection Should be Pooled
public class OperatorTCP implements IDevParaOperator {

    private DevConnectionTCP connection = null;
    public OperatorTCP(DevConnectionTCP connection) {
        this.connection = connection;
    }

    @Override
    public Boolean getBit(String slot, int offset, int bitOffset) throws IOException {
        String cmd = "getb,"+offset+","+bitOffset;
//        System.out.println(cmd);
        return Boolean.parseBoolean(this.request(cmd).trim());
    }

    @Override
    public Boolean setBit(String slot, int offset, int bitOffset, boolean value) throws IOException {
        int valueInt = 0;
        valueInt = value?1:0;
        String cmd = "setb,"+offset+","+bitOffset+","+valueInt;
//        System.out.println(cmd);
        return Boolean.parseBoolean(this.request(cmd));
    }

    @Override
    public Byte getByte(String slot, int offset) throws IOException {
        return null;
    }

    @Override
    public Boolean setByte(String slot, int offset, byte value) throws IOException {
        return null;
    }

    @Override
    public Integer getDWord(String slot, int offset) throws IOException {
        String cmd = "getd,"+offset;
        return Integer.parseInt(this.request(cmd).trim());
    }

    @Override
    public Boolean setDWord(String slot, int offset, int value) throws IOException {
        String cmd = "setd,"+offset+","+value;
        return Boolean.parseBoolean(this.request(cmd));
    }

    @Override
    public Float getFloat(String slot, int offset) throws IOException {
        String cmd = "getf,"+offset;
        return Float.parseFloat(this.request(cmd).trim());
    }

    @Override
    public Boolean setFloat(String slot, int offset, float value) throws IOException {
        String cmd = "setf,"+offset+","+value;
        return Boolean.parseBoolean(this.request(cmd));
    }

    private String request(String cmd) throws IOException {
        return SocketUtils.request(this.connection.getHost(),this.connection.getPort(),cmd);
    }
}
