package org.bantsu.devdatasource.devsim.operator;

import org.bantsu.devdatasource.api.connection.impl.DevConnectionSerial;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;


import java.io.IOException;

public class OperatorSerial implements IDevParaOperator {

    private DevConnectionSerial connection = null;
    private String url = null;

    public OperatorSerial(DevConnectionSerial connection) {
        this.connection = connection;
    }

    @Override
    public Boolean getBit(String slot, int offset, int bitOffset) throws IOException {
        return null;
    }

    @Override
    public Boolean setBit(String slot, int offset, int bitOffset, boolean value) throws IOException {
        return null;
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
        return null;
    }

    @Override
    public Boolean setDWord(String slot, int offset, int value) throws IOException {
        return null;
    }

    @Override
    public Float getFloat(String slot, int offset) throws IOException {
        return null;
    }

    @Override
    public Boolean setFloat(String slot, int offset, float value) throws IOException {
        return null;
    }
}
