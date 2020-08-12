package org.bantsu.devconet.anno.annos.datasourceconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
public @interface DevDataSource {
    String name();
    String sourcePackageName();
    TCPConfig TCP_CONFIG();
    SerialPortConfig SERIAL_PORT_CONFIG();
}
