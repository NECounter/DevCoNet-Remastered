package org.bantsu.devconet.txmanager.impl;

import org.bantsu.devconet.configuration.ValueHisPair;
import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devconet.devmanager.impl.DevManager;
import org.bantsu.devconet.txmanager.IDevTransactionManager;

import java.util.HashMap;
import java.util.Map;

public class DevTransactionManager implements IDevTransactionManager {

    private DevManager devManager = null;

    public DevTransactionManager(DevManager devManager) {
        this.devManager = devManager;
    }

    @Override
    public void devTransaction() {

    }

    @Override
    public void doTransaction() throws Exception {
        ThreadLocal<Map<String, ValueHisPair>> changeBuffer = new ThreadLocal<>();
        changeBuffer.set(new HashMap<>());
        this.devManager.setChangeBuffer(changeBuffer);
        try {
            this.doCommitTransactionJob();
        }catch (Exception e){
            this.devManager.rollbackChangeBuffer();
            return;
        }finally {
            doFinally();
        }
        this.devManager.updateChangeBuffer();
    }


    private void doCommitTransactionJob(){
        this.devTransaction();
    }

    private void doException(Exception e) throws Exception {
        throw e;
    }

    private void doFinally(){

    }
}
