package org.bantsu.devconet.txmanager.impl;

import org.bantsu.devconet.configuration.ValueHisPair;
import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devconet.devmanager.impl.DevManager;
import org.bantsu.devconet.txmanager.IDevTransactionManager;

import java.util.HashMap;
import java.util.Map;

public class DevTransactionManager implements IDevTransactionManager {

    /**
     * A devManager should be provided,
     * as to say, all parameters used in one transaction
     * must be enhanced by the same devManager
     */
    private DevManager devManager = null;

    public DevTransactionManager(DevManager devManager) {
        this.devManager = devManager;
    }


    @Override
    public void devTransaction() {
        //Override it to realize transaction
    }

    @Override
    public void doTransaction() throws Exception {
        try {
            // Before advice
            this.doCommitTransactionJob();
        }catch (Exception e){
            // Exception advice
            this.doException(e);
            return;
        }finally {
            // Final advice
            this.doFinally();
        }
        // After advice
        this.doCommit();

    }


    /**
     * Before advice
     */
    private void doCommitTransactionJob(){
        this.devTransaction();
    }

    /**
     * Exception advice
     */
    private void doException(Exception e) throws Exception {
        //rollback
        this.devManager.rollbackChangeBuffer();
    }

    /**
     * Final advice
     */
    private void doFinally(){

    }

    /**
     * After advice
     */
    private void doCommit() throws Exception {
        //commit
        this.devManager.updateChangeBuffer();
    }
}
