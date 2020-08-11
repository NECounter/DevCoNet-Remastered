package org.bantsu.devconet.txmanager.impl;

import org.bantsu.devconet.devmanager.impl.DevManager;
import org.bantsu.devconet.txmanager.IDevTransactionManager;

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
        try {
            doTryTransactionJob();
        }catch (Exception e){
            doException(e);
        }finally {
            doFinally();
        }
        doCommitTransactionJob();
    }

    private void doTryTransactionJob(){
        this.devTransaction();
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
