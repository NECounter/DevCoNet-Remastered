package org.bantsu.devdatasource.api.anno.resolver.impl;

import org.bantsu.devdatasource.api.anno.annos.DevSource;
import org.bantsu.devdatasource.api.anno.annos.datasourceconfig.DevDataSource;
import org.bantsu.devdatasource.api.anno.annos.datasourceconfig.DevDataSources;
import org.bantsu.devdatasource.api.anno.resolver.IAnnotationResolver;
import org.bantsu.devdatasource.api.configuration.DevParaConfiguration;
import org.bantsu.devdatasource.api.configuration.ParaType;
import org.bantsu.devdatasource.api.configuration.SerialPortConfig;
import org.bantsu.devdatasource.api.configuration.TCPConfig;
import org.bantsu.devdatasource.api.datasource.IDevDataSource;
import org.bantsu.devdatasource.api.datasource.impl.DefaultDevDataSource;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * An AnnotationResolver
 */
public class DevParaAnnotationResolver implements IAnnotationResolver {

    /**
     * The class type of a POJO
     */
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

    /**
     * Get a map of defined data sources
     * @return A map of DataSources<data source name, DevDataSource>
     * @throws Exception Class not found
     */
    private Map<String, IDevDataSource> getDevDataSourcesMap() throws Exception {
        Map<String, IDevDataSource> devDataSourcesMap = new HashMap<>();
        //Get the collection of DevDataSource defined on the class
        DevDataSources devDataSourcesAnno = (DevDataSources) devParaClass.getAnnotation(DevDataSources.class);
        DevDataSource[] devDataSourceAnnos = devDataSourcesAnno.DEV_DATA_SOURCE();

        for (DevDataSource devDataSourceAnno : devDataSourceAnnos) {
            String dataSourceName = devDataSourceAnno.name();
            //It's appointed that data source is defined as DevDataSource in the package of datasource.
            String sourceClassName = devDataSourceAnno.sourcePackageName();
            //Get the TCP Config
            TCPConfig tcpConfig = new TCPConfig();
            tcpConfig.setIp(devDataSourceAnno.TCP_CONFIG().ip());
            tcpConfig.setPort(devDataSourceAnno.TCP_CONFIG().port());
            //Get SerialPort Config
            SerialPortConfig serialPortConfig = new SerialPortConfig();
            serialPortConfig.setBitRate(devDataSourceAnno.SERIAL_PORT_CONFIG().bitRate());
            serialPortConfig.setDataLength(devDataSourceAnno.SERIAL_PORT_CONFIG().dataLength());
            serialPortConfig.setObjectedDeviceAddress(devDataSourceAnno.SERIAL_PORT_CONFIG().objectedDeviceAddress());
            serialPortConfig.setPort(devDataSourceAnno.SERIAL_PORT_CONFIG().port());
            serialPortConfig.setSalve(devDataSourceAnno.SERIAL_PORT_CONFIG().salve());
            serialPortConfig.setStopBit(devDataSourceAnno.SERIAL_PORT_CONFIG().stopBit());
            serialPortConfig.setVerifyMode(devDataSourceAnno.SERIAL_PORT_CONFIG().verifyMode());
            //Get the data source class by reflection
//            Class dataSourceClass = Class.forName(sourceClassName);
//            Constructor constructor = dataSourceClass.getConstructor(TCPConfig.class, SerialPortConfig.class);
//            IDevDataSource dataSource = (IDevDataSource) constructor.newInstance(new Object[]{tcpConfig, serialPortConfig});
            IDevDataSource dataSource = new DefaultDevDataSource(tcpConfig, serialPortConfig, sourceClassName);
            System.out.println("Create DefaultDevDataSource, Name: "+ dataSourceName);
            //Put pair<name, dataSource> into map
            devDataSourcesMap.put(dataSourceName, dataSource);
            }
        return devDataSourcesMap;
    }

    @Override
    public Map<String, DevParaConfiguration> getDevParaConfigMap() throws Exception {
        Map<String, IDevDataSource> devDataSourcesMap = this.getDevDataSourcesMap();
        Map<String, DevParaConfiguration> devParaConfigurationHashMap = new HashMap<>();
        for(Field field: this.getFields()){
            field.setAccessible(true);
            DevParaConfiguration devParaConfiguration = new DevParaConfiguration();
            //Get the DevSource annotation of each field
            DevSource devSource = field.getAnnotation(DevSource.class);
            if(devSource != null){
                ParaType paraType = ParaType.Undefined;
                //Get the type of each field
                Class fieldType = field.getType();
                if(fieldType.equals(Integer.class)) paraType = ParaType.Integer;
                if(fieldType.equals(Byte.class)) paraType = ParaType.Byte;
                if(fieldType.equals(Boolean.class)) paraType = ParaType.Boolean;
                if(fieldType.equals(Float.class)) paraType = ParaType.Float;
                //Gen the configuration of each field
                devParaConfiguration.setParaType(paraType);
                devParaConfiguration.setSlot(devSource.slot());
                devParaConfiguration.setOffset(devSource.offset());
                devParaConfiguration.setBitOffset(devSource.bitOffset());
                devParaConfiguration.setConnectionType(devSource.CONNECTION_TYPE());
                devParaConfiguration.setDataSource(devDataSourcesMap.get(devSource.dataSourceName()));
            }
            //Put pair<full qualified field name, configuration> into config map
            devParaConfigurationHashMap.put(this.devParaClass.getName()+"."+field.getName(), devParaConfiguration);
        }
        return devParaConfigurationHashMap;
    }
}
