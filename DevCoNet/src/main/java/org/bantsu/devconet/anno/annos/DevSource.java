package org.bantsu.devconet.anno.annos;


import org.bantsu.devdatasource.api.configuration.ConnectionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DevSource {
    String dataSourceName() default "devSim";
    ConnectionType CONNECTION_TYPE() default ConnectionType.TCP;

    String slot();
    int offset();
    int bitOffset() default -1;
}
