package org.bantsu.devdatasource.api.configuration;

public class SerialPortConfig {
    private Integer port = 0;
    private Boolean salve = false;
    private Integer bitRate = 9600;
    private Integer dataLength = 8;
    private VerifyMode verifyMode = VerifyMode.None;
    private Integer stopBit = 1;
    private Integer objectedDeviceAddress = 1;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getSalve() {
        return salve;
    }

    public void setSalve(Boolean salve) {
        this.salve = salve;
    }

    public Integer getBitRate() {
        return bitRate;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    public Integer getDataLength() {
        return dataLength;
    }

    public void setDataLength(Integer dataLength) {
        this.dataLength = dataLength;
    }

    public VerifyMode getVerifyMode() {
        return verifyMode;
    }

    public void setVerifyMode(VerifyMode verifyMode) {
        this.verifyMode = verifyMode;
    }

    public Integer getStopBit() {
        return stopBit;
    }

    public void setStopBit(Integer stopBit) {
        this.stopBit = stopBit;
    }

    public Integer getObjectedDeviceAddress() {
        return objectedDeviceAddress;
    }

    public void setObjectedDeviceAddress(Integer objectedDeviceAddress) {
        this.objectedDeviceAddress = objectedDeviceAddress;
    }
}
