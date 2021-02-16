package org.bantsu.devdatasource.api.anno.resolver;

import org.bantsu.devdatasource.api.configuration.DevParaConfiguration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Interface of the Resolver of Class and Field Annotations
 */
public interface IAnnotationResolver {
    /**
     * Get all field variables
     * @return Array of field variables
     */
    Field[] getFields();

    /**
     * Get all annotations of the Class domain
     * @return
     */
    Annotation[] getClassAnnotation();

    /**
     * Get DevParaConfigurations of all field variables
     * @return DevParaConfigurationsMap<full qualified field name, DevParaConfiguration>
     * @throws Exception Class not found
     */
    Map<String, DevParaConfiguration> getDevParaConfigMap() throws Exception;
}
