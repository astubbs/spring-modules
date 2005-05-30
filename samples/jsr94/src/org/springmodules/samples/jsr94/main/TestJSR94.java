/*
 * Copyright 2002-2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.samples.jsr94.main;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.samples.jsr94.model.Car;
import org.springmodules.samples.jsr94.services.CarsService;

/**
 * Sample class to test JSR94 support
 * 
 * @author Thierry Templier
 */
public class TestJSR94 {
	private static final Log log = LogFactory.getLog(TestJSR94.class);

	private static void showCars(List cars) {
		for(Iterator i=cars.iterator();i.hasNext();) {
			Car car=(Car)i.next();
			log.info("## "+car.getName()+" ("+car.getId()+")");
			log.info("    - "+car.getMark());
			log.info("    - "+car.getModel());
			log.info("    - "+car.getYear());
			log.info("    - "+car.getPrice());
		}
	}

	private static void showGoodBargainCars(CarsService service) {
		List goodBargainCars=service.getGoodBargainCars();
		log.info("List of the goodbargain cars:");
		showCars(goodBargainCars);
	}

	/**
	 * Method that selects the spring context to use according to
	 * the application parameters.
	 * @param args main arguments
	 * @return the spring context file to use
	 */
	private static String getContextEngine(String[] args) {
		String engine="drools";
		if( args.length==1 ) {
			engine=args[0];
		} else {
			System.out.println("The rule engine is not specified.");
		}
		System.out.println("The '"+engine+"' rule engine will be used...");

		return "/rulesource-"+engine+".xml";
	}

	/**
	 * Main method to launch the sample application
	 * @param args main arguments
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx=null;
		try {
			ctx=new ClassPathXmlApplicationContext(getContextEngine(args));
			CarsService service=(CarsService)ctx.getBean("carsService");
			showGoodBargainCars(service);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error("Error during execution",ex);
		} finally {
			if( ctx!=null ) {
				ctx.close();
			}
		}
	}
}
