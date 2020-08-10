package org.bantsu.devconet.devmanager.impl;

import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devconet.devmanager.IDevManagerBuilder;

public class DevManagerBuilder implements IDevManagerBuilder {
    @Override
    public IDevManager getDevManager() {
        return new DevManager();
    }
}
