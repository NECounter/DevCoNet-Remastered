package org.bantsu.devconet.anno.resolver;

import org.bantsu.devdatasource.api.datasource.IDevDataSource;

import java.lang.reflect.Field;
import java.util.Map;

public interface IAnnotationResolver<T>{
    Field[] getFields();
    T getClassAnnotation();
    Map<String, T> getFieldAnnotation() throws Exception;
}
