package org.bantsu.devdatasource.api.operator;

import org.bantsu.devdatasource.api.connection.IDevConnection;

import java.io.IOException;

/**
 * When defining a customized dataSource, implement this interface
 */
public interface IDevParaOperator {
    /*
    * Two implements should be carried out,
    * and must exactly follow the name `OperatorTCP` and `OperatorSerial`
    * when using TCP and Serial respectively.
    * Each of which should contain the constructor with one specific type of parameter,
    * which is the type of DevConnectionTCP and DevConnectionSerial respectively.
    * */

    Boolean getBit(String slot, int offset, int bitOffset) throws IOException;
    Boolean setBit(String slot,int offset, int bitOffset, boolean value) throws IOException;

    Byte getByte(String slot, int offset) throws IOException;
    Boolean setByte(String slot, int offset, byte value) throws IOException;

    Integer getDWord(String slot, int offset) throws IOException;
    Boolean setDWord(String slot, int offset, int value) throws IOException;

    Float getFloat(String slot, int offset) throws IOException;
    Boolean setFloat(String slot, int offset, float value) throws IOException;
}
