package org.bantsu.sim.devsim.controller;

import org.bantsu.sim.devsim.mapper.IDataMapper;
import org.bantsu.sim.devsim.mapper.impl.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DataController {

    @Autowired
    @Qualifier("dataMappers")
    private Map<String, DataMapper> dataMappers;


    @RequestMapping("/getBit")
    public String getBit(@PathParam("slot") String slot, @PathParam("offset") int offset,
                         @PathParam("bitOffset") int bitOffset){
        return dataMappers.get(slot).getBit(offset, bitOffset).toString();
    }

    @RequestMapping("/setBit")
    public String setBit(@PathParam("slot") String slot, @PathParam("offset") int offset,
                         @PathParam("bitOffset") int bitOffset,
                         @PathParam("value") boolean value){
        return dataMappers.get(slot).setBit(offset, bitOffset, value).toString();
    }

    @RequestMapping("/getDWord")
    public String getDWord(@PathParam("slot") String slot, @PathParam("offset") int offset){
        return dataMappers.get(slot).getDWord(offset).toString();
    }

    @RequestMapping("/setDWord")
    public String setDWord(@PathParam("slot") String slot, @PathParam("offset") int offset,
                           @PathParam("value") int value){
        return dataMappers.get(slot).setDWord(offset, value).toString();
    }

    @RequestMapping("/getFloat")
    public String getFloat(@PathParam("slot") String slot, @PathParam("offset") int offset){
        return dataMappers.get(slot).getFloat(offset).toString();
    }

    @RequestMapping("/setFloat")
    public String setFloat(@PathParam("slot") String slot, @PathParam("offset") int offset,
                           @PathParam("value") float value){
        return dataMappers.get(slot).setFloat(offset, value).toString();
    }

}
