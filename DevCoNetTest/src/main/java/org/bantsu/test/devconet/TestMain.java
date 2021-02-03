package org.bantsu.test.devconet;

import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devconet.devmanager.IDevManagerBuilder;
import org.bantsu.devconet.devmanager.impl.DevManager;
import org.bantsu.devconet.devmanager.impl.DevManagerBuilder;
import org.bantsu.devconet.txmanager.impl.DevTransactionManager;
import org.bantsu.test.devconet.domain.DevParam;

import javax.xml.crypto.Data;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.Timer;

public class TestMain {
    public static void main(String[] args) throws Exception {

        IDevManagerBuilder devManagerBuilder =  new DevManagerBuilder();
        DevManager devManager = (DevManager) devManagerBuilder.buildConcurrentDevManager(10);
//        DevManager devManager = (DevManager) devManagerBuilder.buildDevManager();
        DevParam devParam = (DevParam) devManager.getEnhancedDevPara(DevParam.class);
        int i=0;

        long timeStart = System.currentTimeMillis();
        while (i<1000){
            devParam.setM0_0(true);
            System.out.println(devParam.getM0_0());
            devParam.setMD04(520);
            System.out.println(devParam.getMD04());
            devParam.setMD08f(3.14f);
            System.out.println(devParam.getMD08f());
            i++;
        }
        System.out.println(System.currentTimeMillis() - timeStart);

        devManager.dispose();

    }
}