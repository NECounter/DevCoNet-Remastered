package org.bantsu.devconet.devmanager;

/**
 * A devManager factory, a supervisor of devManagers(to do)
 */
public interface IDevManagerBuilder {
    /**
     * Get normal devManager
     * @return devManager
     */
    IDevManager buildDevManager();

    /**
     * Get concurrent devManager
     * @param coreSize core pool size of the thread pool
     * @return devManager
     */
    IDevManager buildConcurrentDevManager(Integer coreSize);
}
