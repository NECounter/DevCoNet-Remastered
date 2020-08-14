package org.bantsu.devconet.anno.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Make a POJO as the device parameter POJO.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DevPoJo {
    /**
     * The alias of this POJO
     * @return
     */
    String value();
}
