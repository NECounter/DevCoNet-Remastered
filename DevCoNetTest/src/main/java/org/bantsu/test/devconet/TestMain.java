package org.bantsu.test.devconet;

import org.bantsu.devconet.anno.resolver.impl.DevParaAnnotationResolver;
import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devconet.devmanager.impl.DevManager;
import org.bantsu.devconet.devmanager.impl.DevManagerBuilder;
import org.bantsu.devconet.txmanager.impl.DevTransactionManager;
import org.bantsu.test.devconet.domain.DevParam;

import javax.sound.midi.Soundbank;

public class TestMain {
    public static void main(String[] args) throws Exception {
       DevManagerBuilder devManagerBuilder =  new DevManagerBuilder();
       DevManager devManager = (DevManager)devManagerBuilder.getDevManager();

       DevParam devParam = (DevParam) devManager.getEnhancedDevPara(DevParam.class);
       DevParaAnnotationResolver devParaAnnotationResolver = new DevParaAnnotationResolver(DevParam.class);
       devParaAnnotationResolver.getFieldAnnotation();
       devParam.setMD08f(3.14f);
       System.out.println(devParam.getMD08f());
       DevTransactionManager transactionManager = new DevTransactionManager(devManager){
           @Override
           public void devTransaction() {

               devParam.setMD08f(3.18f);
               int a = 1/0;

           }
       };
       transactionManager.doTransaction();

       System.out.println(devParam.getMD08f());

    }
}
