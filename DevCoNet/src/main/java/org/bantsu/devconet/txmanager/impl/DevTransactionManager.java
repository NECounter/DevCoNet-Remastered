package org.bantsu.devconet.txmanager.impl;

import org.bantsu.devconet.devmanager.impl.DevManager;
import org.bantsu.devconet.txmanager.IDevTransactionManager;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class DevTransactionManager implements IDevTransactionManager {

    /**
     * A devManager should be provided,
     * as to say, all parameters used in one transaction
     * must be enhanced by the same devManager
     */
    private DevManager devManager = null;

    public DevTransactionManager(DevManager devManager) {
        this.devManager = devManager;
        // init trans related thread local parameters
        if (this.devManager.getChangeBuffer().get() == null){
            this.devManager.setChangeBuffer(new HashMap<>());
        }
        this.devManager.setInTransIndicator(false);
    }


    /**
     * Transaction body to be override.
     */
    @Override
    public void devTransaction() {
        //Override it to with your transaction
    }

    /**
     * A wrapper of transaction
     * @throws Exception
     */
    @Override
    public void doTransaction() throws Exception {
        try {
            // One changeBuffer for one Thread
            this.devManager.getChangeBuffer().get().clear();
            // Tell devManager the trans begins
            this.devManager.setInTransIndicator(true);
            // Before advice
            this.doCommitTransactionJob();
            // After advice
            this.doCommit();
        }catch (Exception e){
            // Exception advice
            this.doException(e);
        }finally {
            // Final advice
            this.doFinally();
        }
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
        System.out.println(e.getMessage());
        //rollback
        this.devManager.rollbackChangeBuffer();
        throw e;
    }

    /**
     * Final advice
     */
    private void doFinally(){
        this.devManager.getChangeBuffer().get().clear();
        this.devManager.setLatch(new CountDownLatch(0));
        this.devManager.setInTransIndicator(false);
    }

    /**
     * After advice
     */
    private void doCommit() throws Exception {
        //commit
        this.devManager.updateChangeBuffer();
//        System.out.println("updateChangeBuffer");
    }
}
