package org.bantsu.devdatasource.api.anno.annos.datasourceconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A combination annotation of DevDataSource
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DevDataSources {
    /**
     * A collection of DevDataSources
     * @return
     */
    DevDataSource[] DEV_DATA_SOURCE();
}
