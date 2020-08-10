package org.bantsu.sim.devsim.mapper;

import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IDataMapper {

    Boolean getBit(int offset, int bitOffset);
    Boolean setBit(int offset, int bitOffset, boolean value);

    Byte getByte(int offset);
    Boolean setByte(int offset, byte value);

    Integer getDWord(int offset);
    Boolean setDWord(int offset, int value);

    Float getFloat(int offset);
    Boolean setFloat(int offset, float value);
}
