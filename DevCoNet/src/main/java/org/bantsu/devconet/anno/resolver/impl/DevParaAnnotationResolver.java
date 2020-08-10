package org.bantsu.devconet.anno.resolver.impl;

import org.bantsu.devconet.anno.annos.DevSource;
import org.bantsu.devconet.anno.resolver.IAnnotationResolver;
import org.bantsu.devconet.configuration.DevParaConfiguration;
import org.bantsu.devconet.configuration.ParaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DevParaAnnotationResolver implements IAnnotationResolver {
    private Class devParaClass = null;

    public DevParaAnnotationResolver(Class devParaClass) {
        this.devParaClass = devParaClass;
    }

    @Override
    public Field[] getFields() {
        return devParaClass.getDeclaredFields();
    }

    @Override
    public Annotation[] getClassAnnotation() {
        return devParaClass.getAnnotations();
    }

    @Override
    public Map<String, DevParaConfiguration> getFieldAnnotation() {
        Map<String, DevParaConfiguration> annotationMap = new HashMap<>();
        for(Field field: this.getFields()){
            field.setAccessible(true);
            DevParaConfiguration devParaConfiguration = new DevParaConfiguration();
            DevSource devSource = field.getAnnotation(DevSource.class);
            if(devSource != null){
                ParaType paraType = ParaType.Undefined;
                Class fieldType = field.getType();
                if(fieldType.equals(Integer.class)) paraType = ParaType.Integer;
                if(fieldType.equals(Byte.class)) paraType = ParaType.Byte;
                if(fieldType.equals(Boolean.class)) paraType = ParaType.Boolean;
                if(fieldType.equals(Float.class)) paraType = ParaType.Float;
                devParaConfiguration.setParaType(paraType);
                devParaConfiguration.setHost(devSource.host());
                devParaConfiguration.setPort(devSource.port());
                devParaConfiguration.setSlot(devSource.slot());
                devParaConfiguration.setOffset(devSource.offset());
                devParaConfiguration.setBitOffset(devSource.bitOffset());
            }
            annotationMap.put(this.devParaClass.getName()+"."+field.getName(), devParaConfiguration);
        }

        return annotationMap;
    }
}
