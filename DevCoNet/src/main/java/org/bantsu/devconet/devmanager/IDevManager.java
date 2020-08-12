package org.bantsu.devconet.devmanager;

import org.bantsu.devconet.configuration.ValueHisPair;

import java.util.Map;

public interface IDevManager<T> {
    T getEnhancedDevPara(Class c) throws Exception;

    void updateChangeBuffer() throws Exception;
    void rollbackChangeBuffer() throws Exception;

}
