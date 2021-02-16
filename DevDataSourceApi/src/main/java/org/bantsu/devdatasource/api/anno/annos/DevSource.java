package org.bantsu.devdatasource.api.anno.annos;


import org.bantsu.devdatasource.api.configuration.ConnectionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Config the data source and address of a field variable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DevSource {
    /**
     * Data source name(alias)
     * @return
     */
    String dataSourceName() default "devSim";

    /**
     * The access method(to the device) of this variable
     * @return
     */
    ConnectionType CONNECTION_TYPE() default ConnectionType.TCP;

    /**
     * Partition Name of this variable
     * @return
     */
    String slot();

    /**
     * Offset of address
     * @return
     */
    int offset();

    /**
     * Bit offset of the address(if variable is Boolean)
     * @return
     */
    int bitOffset() default -1;
}
