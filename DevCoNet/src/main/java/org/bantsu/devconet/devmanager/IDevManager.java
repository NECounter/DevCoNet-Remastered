package org.bantsu.devconet.devmanager;

import org.bantsu.devconet.configuration.ValueHisPair;

import java.util.Map;

/**
 * An interface of the DevManager
 * @param <T>
 */
public interface IDevManager<T> {
    /**
     * Get an enhanced instance using POJO class
     * @param c Class of a POJO
     * @return An enhanced instance
     * @throws Exception Class not found
     */
    T getEnhancedDevPara(Class<T> c) throws Exception;

    /**
     * Commit changes if no error occurs
     * @throws Exception Class not found
     */
    void updateChangeBuffer() throws Exception;

    /**
     * Rollback changes if error occurs
     * @throws Exception lass not found
     */
    void rollbackChangeBuffer() throws Exception;

    /**
     * Shutdown Thread pools manually
     */
    void dispose();

}
