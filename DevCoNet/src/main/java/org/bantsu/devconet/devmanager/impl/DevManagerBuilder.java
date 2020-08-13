package org.bantsu.devconet.devmanager.impl;

import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devconet.devmanager.IDevManagerBuilder;

public class DevManagerBuilder implements IDevManagerBuilder {
    @Override
    public IDevManager buildDevManager() {
        return new DevManager();
    }

    @Override
    public IDevManager buildConcurrentDevManager(Integer coreSize) {
        return new DevManager(true, coreSize);
    }
}
