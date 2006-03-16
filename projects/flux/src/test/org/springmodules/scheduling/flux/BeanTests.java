package org.springmodules.scheduling.flux;

import junit.framework.TestCase;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springmodules.scheduling.flux.EngineBean;

/**
 * Simple tests for creating engine beans in different ways.
 *
 * @author Copyright 2000-2006 Flux Corporation. All rights reserved.
 */
public class BeanTests extends TestCase {

  private EngineBean fluxEngineBean;

  public void setUp() {
    fluxEngineBean = null;
  } // setUp()
  
  public void tearDown() throws Exception {
System.out.println("here tearDown");  
    if (fluxEngineBean != null) {
    System.out.println("here tearDown 2");  
      fluxEngineBean.dispose();
System.out.println("here tearDown 3");        
    } // if
System.out.println("here tearDown 4");      
  } // tearDown()

  public void testBeanCreation() throws Exception {
    Resource res = new FileSystemResource("src/test/beans.xml");
    XmlBeanFactory factory = new XmlBeanFactory(res);

System.out.println("here");
    fluxEngineBean = (EngineBean) factory.getBean("fluxEngineBean");
System.out.println("here 2");
    assertNotNull(fluxEngineBean);
System.out.println("here 3");    
    assertEquals(1, fluxEngineBean.getConfiguration().getConcurrencyLevel());
System.out.println("here 4");    
  } // testBeanCreation()
  
  public void testBeanCreationFromConfigurationFile() throws Exception {
    Resource res = new FileSystemResource("beans.xml");
    XmlBeanFactory factory = new XmlBeanFactory(res);

    fluxEngineBean = (EngineBean) factory.getBean("fluxEngineBeanFromConfigurationFile");

    assertNotNull(fluxEngineBean);
    
    assertEquals(7, fluxEngineBean.getConfiguration().getConcurrencyLevel());
  } // testBeanCreationFromConfigurationFile()  

} // class BeanTests