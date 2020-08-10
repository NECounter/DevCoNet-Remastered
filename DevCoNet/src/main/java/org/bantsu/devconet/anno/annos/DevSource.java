package org.bantsu.devconet.anno.annos;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DevSource {
    String host();
    int port();

    String slot();
    int offset();
    int bitOffset() default -1;
}
