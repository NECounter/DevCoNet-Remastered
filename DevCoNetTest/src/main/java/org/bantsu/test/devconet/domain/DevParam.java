package org.bantsu.test.devconet.domain;

import org.bantsu.devdatasource.api.anno.annos.DevPoJo;
import org.bantsu.devdatasource.api.anno.annos.DevSource;
import org.bantsu.devdatasource.api.anno.annos.datasourceconfig.DevDataSource;
import org.bantsu.devdatasource.api.anno.annos.datasourceconfig.DevDataSources;
import org.bantsu.devdatasource.api.anno.annos.datasourceconfig.SerialPortConfig;
import org.bantsu.devdatasource.api.anno.annos.datasourceconfig.TCPConfig;

import java.io.Serializable;

@DevPoJo("devParam")
@DevDataSources(DEV_DATA_SOURCE = {
        @DevDataSource(name = "devSim", sourcePackageName = "org.bantsu.devdatasource.devsim",
        TCP_CONFIG = @TCPConfig(ip = "127.0.0.1", port = 8080), SERIAL_PORT_CONFIG = @SerialPortConfig()),
        @DevDataSource(name = "socketSource", sourcePackageName = "org.bantsu.devdatasource.socketsource",
        TCP_CONFIG = @TCPConfig(ip = "192.168.3.71", port = 8000), SERIAL_PORT_CONFIG = @SerialPortConfig())
})
public class DevParam implements Serializable {

    @DevSource(dataSourceName = "socketSource", slot = "M", offset = 0, bitOffset = 0)
    private Boolean M0_0;

    @DevSource(dataSourceName = "socketSource", slot = "M", offset = 1)
    private Byte MB01;

    @DevSource(dataSourceName = "socketSource", slot = "M", offset = 4)
    private Integer MD04;

    @DevSource(dataSourceName = "socketSource", slot = "M", offset = 8)
    private Float MD08f;


    public Boolean getM0_0() {
        return M0_0;
    }

    public void setM0_0(Boolean m0_0) {
        M0_0 = m0_0;
    }

    public Byte getMB01() {
        return MB01;
    }

    public void setMB01(Byte MB01) {
        this.MB01 = MB01;
    }

    public Integer getMD04() {
        return MD04;
    }

    public void setMD04(Integer MD04) {
        this.MD04 = MD04;
    }

    public Float getMD08f() {
        return MD08f;
    }

    public void setMD08f(Float MD08f) {
        this.MD08f = MD08f;
    }

}
