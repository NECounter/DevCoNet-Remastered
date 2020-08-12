package org.bantsu.devconet.devmanager;

public interface IDevManager<T> {
    T getEnhancedDevPara(Class c) throws Exception;

    void updateChangeBuffer() throws Exception;
    void rollbackChangeBuffer() throws ClassNotFoundException, NoSuchFieldException, Exception;
}
