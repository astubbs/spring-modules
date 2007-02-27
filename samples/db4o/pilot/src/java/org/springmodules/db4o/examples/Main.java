/**
 * Created on Nov 5, 2005
 *
 * $Id: Main.java,v 1.1 2007/02/27 16:42:22 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.db4o.examples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Starting point for the db4o spring support example. It loads the application context and
 * fires the example.
 * 
 * @author Costin Leau
 *
 */
public class Main {

	private static final Log log = LogFactory.getLog(Main.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"/applicationContext-db4o.xml");
        Db4oService service = (Db4oService)context.getBean("db4oService");

        log.info("list of pilots before saving");
        service.listPilots();
        log.info("calling saveSmth");
        service.saveSmth();

        log.info("before saveWithRollback");
        try {
            service.saveWithRollback();
        } catch (RuntimeException e) {
            // it's okay
        }	
        log.info("after saveWithRollback");
        service.listPilots();
		context.close();
	}

}
