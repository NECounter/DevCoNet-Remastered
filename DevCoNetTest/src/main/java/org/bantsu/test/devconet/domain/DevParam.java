package org.bantsu.test.devconet.domain;

import org.bantsu.devconet.anno.annos.DevPoJo;
import org.bantsu.devconet.anno.annos.DevSource;

import java.io.Serializable;

@DevPoJo("devParam")
public class DevParam implements Serializable {
    @DevSource(host = "127.0.0.1", slot = "M", port = 8080, offset = 0, bitOffset = 0)
    private Boolean M0_0;

    @DevSource(host = "127.0.0.1", slot = "V", port = 8080, offset = 0, bitOffset = 0)
    private Boolean V0_0;

    @DevSource(host = "127.0.0.1", slot = "M", port = 8080, offset = 1)
    private Byte MB01;

    @DevSource(host = "127.0.0.1", slot = "V", port = 8080, offset = 1)
    private Byte VB01;

    @DevSource(host = "127.0.0.1", slot = "M", port = 8080, offset = 4)
    private Integer MD04;

    @DevSource(host = "127.0.0.1", slot = "V", port = 8080, offset = 4)
    private Integer VD04;

    @DevSource(host = "127.0.0.1", slot = "M", port = 8080, offset = 8)
    private Float MD08f;

    @DevSource(host = "127.0.0.1", slot = "V", port = 8080, offset = 8)
    private Float VD08f;


    public Boolean getM0_0() {
        return M0_0;
    }

    public void setM0_0(Boolean m0_0) {
        M0_0 = m0_0;
    }

    public Boolean getV0_0() {
        return V0_0;
    }

    public void setV0_0(Boolean v0_0) {
        V0_0 = v0_0;
    }

    public Byte getMB01() {
        return MB01;
    }

    public void setMB01(Byte MB01) {
        this.MB01 = MB01;
    }

    public Byte getVB01() {
        return VB01;
    }

    public void setVB01(Byte VB01) {
        this.VB01 = VB01;
    }

    public Integer getMD04() {
        return MD04;
    }

    public void setMD04(Integer MD04) {
        this.MD04 = MD04;
    }

    public Integer getVD04() {
        return VD04;
    }

    public void setVD04(Integer VD04) {
        this.VD04 = VD04;
    }

    public Float getMD08f() {
        return MD08f;
    }

    public void setMD08f(Float MD08f) {
        this.MD08f = MD08f;
    }

    public Float getVD08f() {
        return VD08f;
    }

    public void setVD08f(Float VD08f) {
        this.VD08f = VD08f;
    }

}
