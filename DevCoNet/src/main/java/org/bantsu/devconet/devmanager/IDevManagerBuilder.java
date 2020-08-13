package org.bantsu.devconet.devmanager;

public interface IDevManagerBuilder {
    IDevManager buildDevManager();
    IDevManager buildConcurrentDevManager(Integer coreSize);
}
