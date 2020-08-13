package org.bantsu.test.devconet;

import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devconet.devmanager.IDevManagerBuilder;
import org.bantsu.devconet.devmanager.impl.DevManager;
import org.bantsu.devconet.devmanager.impl.DevManagerBuilder;
import org.bantsu.devconet.txmanager.impl.DevTransactionManager;
import org.bantsu.test.devconet.domain.DevParam;

public class TestMain {
    public static void main(String[] args) throws Exception {

        IDevManagerBuilder devManagerBuilder =  new DevManagerBuilder();
        DevManager devManager = (DevManager) devManagerBuilder.buildConcurrentDevManager(10);
        DevParam devParam = (DevParam) devManager.getEnhancedDevPara(DevParam.class);
        devParam.setMD08f(3.14f);
        System.out.println(devParam.getMD08f());
        DevTransactionManager transactionManager =
                new DevTransactionManager(devManager){
           @Override
           public void devTransaction() {

               devParam.setMD08f(3.18f);
//               int a = 1/0;

           }
        };
        transactionManager.doTransaction();

        System.out.println(devParam.getMD08f());
        devManager.dispose();

    }
}
