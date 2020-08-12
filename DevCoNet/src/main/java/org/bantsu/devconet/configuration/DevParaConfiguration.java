package org.bantsu.devconet.configuration;

import org.bantsu.devdatasource.api.configuration.ConnectionType;
import org.bantsu.devdatasource.api.datasource.IDevDataSource;

public class DevParaConfiguration {
    private String slot = null;
    private Integer offset = null;
    private Integer bitOffset = null;


    private ParaType paraType = null;

    private IDevDataSource dataSource = null;
    private ConnectionType connectionType = null;


    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public IDevDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(IDevDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ParaType getParaType() {
        return paraType;
    }

    public void setParaType(ParaType paraType) {
        this.paraType = paraType;
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
