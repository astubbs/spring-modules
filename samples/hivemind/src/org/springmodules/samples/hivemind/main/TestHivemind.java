package org.springmodules.samples.hivemind.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.samples.hivemind.service.ISampleService;

/**
 * Sample class to test Spring / Hivemind support
 * 
 * @author ttemplier
 */
public class TestHivemind {
	private static final Log log = LogFactory.getLog(TestHivemind.class);

	/**
	 * Main method to launch the sample application
	 * @param args main arguments
	 */
	public static void main(String[] args) {
		try {
			ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext(
															"/applicationContext.xml");
			ISampleService service=(ISampleService)ctx.getBean("hivemindService");
			service.executeService("sample");
		} catch(Exception ex) {
			log.error("Error during execution",ex);
		}
	}
}
