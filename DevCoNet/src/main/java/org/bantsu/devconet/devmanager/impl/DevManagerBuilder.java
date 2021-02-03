package org.bantsu.devconet.devmanager.impl;

import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devconet.devmanager.IDevManagerBuilder;

import java.util.HashMap;
import java.util.Map;


public class DevManagerBuilder implements IDevManagerBuilder {
    private final Map<Integer, DevManager> devManagerPool;

    public DevManagerBuilder() {
        this.devManagerPool = new HashMap<>();
    }

    @Override
    public IDevManager buildDevManager() {
        if (devManagerPool.get(0) == null){
            synchronized (this){
                if (devManagerPool.get(0) == null){
                    devManagerPool.put(0, new DevManager());
                    System.out.println("Create DevManager");
                }
            }
        }
        return devManagerPool.get(0);
    }

    @Override
    public IDevManager buildConcurrentDevManager(Integer coreSize) {
        if (devManagerPool.get(coreSize) == null){
            synchronized (this){
                if (devManagerPool.get(coreSize) == null){
                    devManagerPool.put(coreSize, new DevManager(coreSize));
                    System.out.println("Create ConcurrentDevManager, CoreSize: " + coreSize);
                }
            }
        }
        return devManagerPool.get(coreSize);
    }
}
