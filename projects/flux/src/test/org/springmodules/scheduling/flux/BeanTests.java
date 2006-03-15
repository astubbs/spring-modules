package org.springmodules.scheduling.flux;

import junit.framework.TestCase;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springmodules.scheduling.flux.EngineBean;

public class BeanTests extends TestCase {

  public void testBeanCreation() throws Exception {
  
  /*
    Resource res = new FileSystemResource("beans.xml");
    XmlBeanFactory factory = new XmlBeanFactory(res);

    EngineBean fluxEngineBean = (EngineBean) factory.getBean("fluxEngineBean");

    //EngineBean fluxEngineBean = (EngineBean) factory.getBean("fluxEngineBeanFromConfigurationFile");

    System.out.println("we appear to have an EngineBean here");

    System.out.println("db type: " + fluxEngineBean.getConfiguration().getDatabaseType());
    System.out.println("concurrency level: " + fluxEngineBean.getConfiguration().getConcurrencyLevel());

    fluxEngineBean.dispose();
    */
  } // main()

} // class BeanTests