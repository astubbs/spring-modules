/**
 * Created on Oct 9, 2005
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.examples.jcr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springmodules.examples.jcr.JcrService;

/**
 * Starting point for the Jcr example. It loads the applicationContext and calls
 * method on the defined beans.
 * 
 * @author Costin Leau
 *
 */
public class Main {

	private static final Log log = LogFactory.getLog(Main.class);
	
    public static void main(String[] args) {
        
    	AbstractApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext-repository.xml");
        
        //ResourceLoader loader = new DefaultResourceLoader();
        //Resource resource = loader.getResource("test.file");
        
        JcrService service = (JcrService)context.getBean("jcrService");

        //String node1Path = service.saveSmth("node1", "property1");
        
        String node2Path = null;
        try {
            node2Path = service.saveWithRollback("node2", "property2");
        } catch (RuntimeException e) {
            log.info("found exception " + e);
        }
        
        //log.info("is node 1 still committed "+ service.checkNode(node1Path));
        log.info("is node 2 still rolled back " + !service.checkNode("/node2"));
        
        context.close();

    }

}
