package org.bantsu.devconet.configuration;

public class DevParaConfiguration {
    private String host = null;
    private Integer port = null;

    private String slot = null;
    private Integer offset = null;
    private Integer bitOffset = null;

    private ParaType paraType = null;

    public ParaType getParaType() {
        return paraType;
    }

    public void setParaType(ParaType paraType) {
        this.paraType = paraType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getBitOffset() {
        return bitOffset;
    }

    public void setBitOffset(Integer bitOffset) {
        this.bitOffset = bitOffset;
    }
}
