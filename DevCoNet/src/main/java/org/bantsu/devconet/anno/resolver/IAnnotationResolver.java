package org.bantsu.devconet.anno.resolver;

import java.lang.reflect.Field;
import java.util.Map;

public interface IAnnotationResolver<T>{
    Field[] getFields();
    T getClassAnnotation();
    Map<String, T> getFieldAnnotation();
}
