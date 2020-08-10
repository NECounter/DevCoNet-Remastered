package org.bantsu.sim.devsim.mapper.impl;

import org.bantsu.sim.devsim.mapper.IDataMapper;
import org.bantsu.sim.devsim.utils.DataConvertor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class DataMapper implements IDataMapper {
    private String fileName = null;
    private File memoryCache = null;

    private static volatile Object readWriteLock = new Object();

    public DataMapper(String fileName) throws Exception {
        String classpath = ResourceUtils.getURL("classpath:").getPath();
        this.fileName = classpath + "static/dataMap/" + fileName;
        this.memoryCache = new File(this.fileName);
    }

    @Override
    public synchronized Boolean getBit(int offset, int bitOffset) {
        try {
            InputStream inputStream = new FileInputStream(memoryCache);
            byte[] bytes = inputStream.readAllBytes();
            return (bytes[offset] & (1 << bitOffset)) != 0;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public synchronized Boolean setBit(int offset, int bitOffset, boolean value) {
        try {
            InputStream inputStream = new FileInputStream(memoryCache);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            if (value == ((bytes[offset] & (1 << bitOffset)) == 0)){
                bytes[offset] = value?(bytes[offset] += 1<<bitOffset):(bytes[offset] -= 1<<bitOffset);
            }
            OutputStream outputStream = new FileOutputStream(memoryCache);
            outputStream.write(bytes);
            outputStream.close();
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public synchronized Byte getByte(int offset) {
        try {
            InputStream inputStream = new FileInputStream(memoryCache);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            return bytes[offset];
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public synchronized Boolean setByte(int offset, byte value) {
        try {
            InputStream inputStream = new FileInputStream(memoryCache);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            bytes[offset] = value;
            OutputStream outputStream = new FileOutputStream(memoryCache);
            outputStream.write(bytes);
            outputStream.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public synchronized Integer getDWord(int offset) {
        try {
            InputStream inputStream = new FileInputStream(memoryCache);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            int res = DataConvertor.byte2int(Arrays.copyOfRange(bytes,offset,offset+4));
            return res;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public synchronized Boolean setDWord(int offset, int value) {
        try {
            InputStream inputStream = new FileInputStream(memoryCache);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            byte[] convertedBytes = DataConvertor.int2byte(value);
            System.arraycopy(convertedBytes, 0, bytes, offset, 4);
            OutputStream outputStream = new FileOutputStream(memoryCache);
            outputStream.write(bytes);
            outputStream.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public synchronized Float getFloat(int offset) {
        try {
            InputStream inputStream = new FileInputStream(memoryCache);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            float res = DataConvertor.byte2float(Arrays.copyOfRange(bytes,offset,offset+4));
            return res;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public synchronized Boolean setFloat(int offset, float value) {
        try {
            InputStream inputStream = new FileInputStream(memoryCache);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            byte[] convertedBytes = DataConvertor.float2byte(value);
            for (int i = 0; i < 4; i++) {
                bytes[offset+i] = convertedBytes[i];
            }
            OutputStream outputStream = new FileOutputStream(memoryCache);
            outputStream.write(bytes);
            outputStream.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
