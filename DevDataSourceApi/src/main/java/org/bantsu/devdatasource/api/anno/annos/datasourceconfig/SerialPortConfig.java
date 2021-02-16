package org.bantsu.devdatasource.api.anno.annos.datasourceconfig;

import org.bantsu.devdatasource.api.configuration.VerifyMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Config of a SerialPort
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SerialPortConfig {
    /**
     * Physical port name
     * @return
     */
    int port() default 0;

    /**
     * Is the device a Master
     * @return
     */
    boolean salve() default false;

    /**
     * bit Rate
     * @return
     */
    int bitRate() default 9600;

    /**
     * data Length
     * @return
     */
    int dataLength() default 8;

    /**
     * verify Mode
     * @return
     */
    VerifyMode verifyMode() default VerifyMode.None;

    /**
     * stop Bit
     * @return
     */
    int stopBit() default 1;

    /**
     * objected Device Address
     * @return
     */
    int objectedDeviceAddress() default 1;
}
