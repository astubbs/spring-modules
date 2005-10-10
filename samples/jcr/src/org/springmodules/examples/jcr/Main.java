/**
 * Created on Oct 9, 2005
 *
 * $Id: Main.java,v 1.1 2005/10/10 09:20:53 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.examples.jcr;

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
        
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext-repository.xml");
        
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("test.file");
        
        JcrService service = (JcrService)context.getBean("jcrService");

        service.saveSmth();

        try {
            service.saveWithRollback();
        } catch (RuntimeException e) {
            // it's okay
        }
        context.close();

    }

}
