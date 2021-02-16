package org.bantsu.devdatasource.api.anno.annos.datasourceconfig;

import org.bantsu.devdatasource.api.configuration.ConnectionType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * mark the specific connection type of a operator when implementing it
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DevConnectionType {
    ConnectionType connectionType();
}
