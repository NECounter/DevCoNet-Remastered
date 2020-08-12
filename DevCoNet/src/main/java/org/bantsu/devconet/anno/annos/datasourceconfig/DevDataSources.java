package org.bantsu.devconet.anno.annos.datasourceconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DevDataSources {
    DevDataSource[] DEV_DATA_SOURCE();
}