package org.bantsu.test.devconet;

import org.bantsu.devconet.anno.resolver.impl.DevParaAnnotationResolver;
import org.bantsu.devconet.devmanager.IDevManager;
import org.bantsu.devconet.devmanager.impl.DevManager;
import org.bantsu.devconet.devmanager.impl.DevManagerBuilder;
import org.bantsu.test.devconet.domain.DevParam;

public class TestMain {
    public static void main(String[] args) {
       DevManagerBuilder devManagerBuilder =  new DevManagerBuilder();
       DevManager devManager = (DevManager)devManagerBuilder.getDevManager();

       DevParam devParam = (DevParam) devManager.getEnhancedDevPara(DevParam.class);
       DevParaAnnotationResolver devParaAnnotationResolver = new DevParaAnnotationResolver(DevParam.class);
       devParaAnnotationResolver.getFieldAnnotation();
       devParam.setMD08f(3.14f);
       System.out.println(devParam.getMD08f());

    }
}
