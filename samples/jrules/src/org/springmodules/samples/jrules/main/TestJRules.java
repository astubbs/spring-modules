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

package org.springmodules.samples.jrules.main;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.samples.jrules.model.Car;
import org.springmodules.samples.jrules.services.CarsService;

/**
 * Sample class to test Spring / Hivemind support
 * 
 * @author Thierry Templier
 */
public class TestJRules {
	private static final Log log = LogFactory.getLog(TestJRules.class);

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
	 * Main method to launch the sample application
	 * @param args main arguments
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx=null;
		try {
			ctx=new ClassPathXmlApplicationContext("/rulesource.xml");
			CarsService service=(CarsService)ctx.getBean("carsService");
			showGoodBargainCars(service);
		} catch(Exception ex) {
			log.error("Error during execution",ex);
		} finally {
			if( ctx!=null ) {
				ctx.close();
			}
		}
	}
}
