package org.bantsu.test.devconet;

import org.bantsu.devconet.devmanager.IDevManagerBuilder;
import org.bantsu.devconet.devmanager.impl.DevManager;
import org.bantsu.devconet.devmanager.impl.DevManagerBuilder;
import org.bantsu.devconet.txmanager.impl.DevTransactionManager;
import org.bantsu.test.devconet.domain.DevParam;


public class TestMain {
    public static void main(String[] args) throws Exception {

        IDevManagerBuilder devManagerBuilder =  new DevManagerBuilder();
        DevManager devManager = (DevManager) devManagerBuilder.buildConcurrentDevManager(10);
//        DevManager devManager = (DevManager) devManagerBuilder.buildDevManager();
        DevParam devParam = (DevParam) devManager.getEnhancedDevPara(DevParam.class);
        long timeStart = System.currentTimeMillis();

        devParam.setM0_0(true);
        System.out.println(devParam.getM0_0());
        devParam.setMD04(520);
        System.out.println(devParam.getMD04());

        DevTransactionManager transactionManager =
            new DevTransactionManager(devManager){
                //override devTransaction() with your codes
                @Override
                public void devTransaction() {
                    int res = devParam.getMD04();
                    res -= 100;
                    devParam.setMD04(res);
                    //Here comes an error
                    int divByZero = 1 / 0;
                    devParam.setM0_0(res == 42);
                }
            };
        //start the transaction
        for (int i=0; i<10;i++){
            try {
                transactionManager.doTransaction();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }

        System.out.println(devParam.getM0_0());
        System.out.println(devParam.getMD04());


        devParam.setMD08f(3.14f);
        System.out.println(devParam.getMD08f());

        System.out.println(System.currentTimeMillis() - timeStart);
        devManager.dispose();

    }
}