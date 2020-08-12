package org.bantsu.devconet.anno.resolver.impl;

import org.bantsu.devconet.anno.annos.DevSource;
import org.bantsu.devconet.anno.annos.datasourceconfig.DevDataSource;
import org.bantsu.devconet.anno.annos.datasourceconfig.DevDataSources;
import org.bantsu.devconet.anno.resolver.IAnnotationResolver;
import org.bantsu.devconet.configuration.DevParaConfiguration;
import org.bantsu.devconet.configuration.ParaType;
import org.bantsu.devdatasource.api.configuration.SerialPortConfig;
import org.bantsu.devdatasource.api.configuration.TCPConfig;
import org.bantsu.devdatasource.api.datasource.IDevDataSource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
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

    private Map<String, IDevDataSource> getDevDataSourcesMap() throws Exception {
        Map<String, IDevDataSource> devDataSourcesMap = new HashMap<>();
        DevDataSources devDataSourcesAnno = (DevDataSources) devParaClass.getAnnotation(DevDataSources.class);
        DevDataSource[] devDataSourceAnnos = devDataSourcesAnno.DEV_DATA_SOURCE();

        for (DevDataSource devDataSourceAnno : devDataSourceAnnos) {
            String dataSourceName = devDataSourceAnno.name();
            String sourceClassName = devDataSourceAnno.sourcePackageName() + ".datasource.DevDataSource";
            TCPConfig tcpConfig = new TCPConfig();
            tcpConfig.setIp(devDataSourceAnno.TCP_CONFIG().ip());
            tcpConfig.setPort(devDataSourceAnno.TCP_CONFIG().port());
            SerialPortConfig serialPortConfig = new SerialPortConfig();
            serialPortConfig.setBitRate(devDataSourceAnno.SERIAL_PORT_CONFIG().bitRate());
            serialPortConfig.setDataLength(devDataSourceAnno.SERIAL_PORT_CONFIG().dataLength());
            serialPortConfig.setObjectedDeviceAddress(devDataSourceAnno.SERIAL_PORT_CONFIG().objectedDeviceAddress());
            serialPortConfig.setPort(devDataSourceAnno.SERIAL_PORT_CONFIG().port());
            serialPortConfig.setSalve(devDataSourceAnno.SERIAL_PORT_CONFIG().salve());
            serialPortConfig.setStopBit(devDataSourceAnno.SERIAL_PORT_CONFIG().stopBit());
            serialPortConfig.setVerifyMode(devDataSourceAnno.SERIAL_PORT_CONFIG().verifyMode());

            Class dataSourceClass = Class.forName(sourceClassName);
            Constructor constructor = dataSourceClass.getConstructor(TCPConfig.class, SerialPortConfig.class);
            IDevDataSource dataSource = (IDevDataSource) constructor.newInstance(new Object[]{tcpConfig, serialPortConfig});
            devDataSourcesMap.put(dataSourceName, dataSource);
            }
        return devDataSourcesMap;
    }

    @Override
    public Map<String, DevParaConfiguration> getFieldAnnotation() throws Exception {
        Map<String, IDevDataSource> devDataSourcesMap = this.getDevDataSourcesMap();
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
                devParaConfiguration.setSlot(devSource.slot());
                devParaConfiguration.setOffset(devSource.offset());
                devParaConfiguration.setBitOffset(devSource.bitOffset());
                devParaConfiguration.setConnectionType(devSource.CONNECTION_TYPE());
                devParaConfiguration.setDataSource(devDataSourcesMap.get(devSource.dataSourceName()));
            }
            annotationMap.put(this.devParaClass.getName()+"."+field.getName(), devParaConfiguration);
        }

        return annotationMap;
    }
}
