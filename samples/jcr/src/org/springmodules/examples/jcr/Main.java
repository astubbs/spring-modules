/**
 * Created on Oct 9, 2005
 *
 * $Id: Main.java,v 1.2 2005/12/06 10:37:09 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.examples.jcr;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Starting point for the Jcr example. It loads the applicationContext and calls
 * method on the defined beans.
 * 
 * @author Costin Leau
 *
 */
public class Main {

    public static void main(String[] args) {
        
    	System.setProperty(LogFactory.FACTORY_PROPERTY, "org.apache.commons.logging.impl.Log4jFactory");
        PropertyConfigurator.configure("log4j.properties");
        
    	AbstractApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext-repository.xml");
        
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("test.file");
        
        JcrService service = (JcrService)context.getBean("jcrService");

        service.saveSmth();
        

        try {
            //service.saveWithRollback();
        } catch (RuntimeException e) {
            // it's okay
        }
        
        try {
        	Thread.sleep(5000);
		}
		catch (InterruptedException e) {
			//
		}
        context.close();

    }

}
