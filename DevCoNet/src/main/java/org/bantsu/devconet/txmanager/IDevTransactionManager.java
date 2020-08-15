package org.bantsu.devconet.txmanager;

public interface IDevTransactionManager {
    /**
     * The transaction code block
     */
    void devTransaction();

    /**
     * Transaction control logic
     * @throws Exception
     */
    void doTransaction() throws Exception;
}
