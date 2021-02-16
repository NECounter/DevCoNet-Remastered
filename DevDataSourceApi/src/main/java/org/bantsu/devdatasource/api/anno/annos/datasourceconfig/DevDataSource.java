package org.bantsu.devdatasource.api.anno.annos.datasourceconfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Config of a data source
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DevDataSource {
    /**
     * The name(alias) of this data source
     * @return
     */
    String name();

    /**
     * The fully-qualified package name of a DevDataSource Implementation
     * @return
     */
    String sourcePackageName();

    /**
     * TCP parameters of the data source
     * @return
     */
    TCPConfig TCP_CONFIG();

    /**
     * SerialPort parameters of the data source
     * @return
     */
    SerialPortConfig SERIAL_PORT_CONFIG();
}
