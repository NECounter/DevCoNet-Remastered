package org.bantsu.devconet.anno.annos.datasourceconfig;

import org.bantsu.devdatasource.api.configuration.VerifyMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SerialPortConfig {
    int port() default 0;
    boolean salve() default false;
    int bitRate() default 9600;
    int dataLength() default 8;
    VerifyMode verifyMode() default VerifyMode.None;
    int stopBit() default 1;
    int objectedDeviceAddress() default 1;
}
