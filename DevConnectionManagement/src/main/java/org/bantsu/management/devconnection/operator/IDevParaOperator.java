package org.bantsu.management.devconnection.operator;

import java.io.IOException;

public interface IDevParaOperator {
    Boolean getBit(String slot, int offset, int bitOffset) throws IOException;
    Boolean setBit(String slot,int offset, int bitOffset, boolean value) throws IOException;

    Byte getByte(String slot, int offset) throws IOException;
    Boolean setByte(String slot, int offset, byte value) throws IOException;

    Integer getDWord(String slot, int offset) throws IOException;
    Boolean setDWord(String slot, int offset, int value) throws IOException;

    Float getFloat(String slot, int offset) throws IOException;
    Boolean setFloat(String slot, int offset, float value) throws IOException;
}
