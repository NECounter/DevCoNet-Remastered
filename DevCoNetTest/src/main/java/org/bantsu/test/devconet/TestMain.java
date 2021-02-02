package org.bantsu.test.devconet;

import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devconet.devmanager.IDevManagerBuilder;
import org.bantsu.devconet.devmanager.impl.DevManager;
import org.bantsu.devconet.devmanager.impl.DevManagerBuilder;
import org.bantsu.devconet.txmanager.impl.DevTransactionManager;
import org.bantsu.test.devconet.domain.DevParam;

public class TestMain {
    public static void main(String[] args) throws Exception {
//        for(int i=0; i<3; i++){
//            System.out.println(i);
//        }

        IDevManagerBuilder devManagerBuilder =  new DevManagerBuilder();
        DevManager devManager = (DevManager) devManagerBuilder.buildConcurrentDevManager(10);
//        DevManager devManager = (DevManager) devManagerBuilder.buildDevManager();
        DevParam devParam = (DevParam) devManager.getEnhancedDevPara(DevParam.class);
        devParam.setMD08f(3.15f);
        System.out.println(devParam.getMD08f());
        DevTransactionManager transactionManager =
                new DevTransactionManager(devManager){
           @Override
           public void devTransaction() {

               devParam.setMD08f(3.18f);
               devParam.setMD08f(3.17f);
               devParam.setMD08f(3.16f);
//               System.out.println("In Trans: " + devParam.getMD08f());
//               int a = 1/0;

           }
        };
        transactionManager.doTransaction();



        System.out.println(devParam.getMD08f());

        DevTransactionManager transactionManager1 =
                new DevTransactionManager(devManager){
                    @Override
                    public void devTransaction() {

                        devParam.setMD08f(3.18f);
                        devParam.setMD08f(3.17f);
                        devParam.setMD08f(3.1323f);
//               System.out.println("In Trans: " + devParam.getMD08f());
                        int a = 1/0;

                    }
                };

        transactionManager1.doTransaction();

        System.out.println(devParam.getMD08f());
        devManager.dispose();

    }
}
