package org.bantsu.sim.devsim.config;


import org.bantsu.sim.devsim.mapper.IDataMapper;
import org.bantsu.sim.devsim.mapper.impl.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MapperConfig {

    @Autowired
    private DataMapper dataMapperM;
    @Autowired
    private DataMapper dataMapperV;

    @Bean
    public DataMapper dataMapperM() throws Exception {
        return new DataMapper("MMap.mio");
    }

    @Bean
    public DataMapper dataMapperV() throws Exception {
        return new DataMapper("VMap.mio");
    }

    @Bean("dataMappers")
    public Map<String, DataMapper> dataMappers() {
        Map<String, DataMapper> dataMappers = new HashMap<>();
        dataMappers.put("M", dataMapperM);
        dataMappers.put("V", dataMapperV);
        return dataMappers;
    }

}
