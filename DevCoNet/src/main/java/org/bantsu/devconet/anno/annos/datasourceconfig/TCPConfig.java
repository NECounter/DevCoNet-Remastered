package org.bantsu.devconet.anno.annos.datasourceconfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Config of a TCP
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TCPConfig {
    String ip();
    int port();
}
