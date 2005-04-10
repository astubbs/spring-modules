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

package org.springmodules.samples.hivemind.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.samples.hivemind.service.ISampleService;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Sample class to test Spring / Hivemind support
 *
 * @author Thierry Templier
 */
public class TestHivemind {

	private static final Log log = LogFactory.getLog(TestHivemind.class);

	/**
	 * Main method to launch the sample application
	 *
	 * @param args main arguments
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = null;
		try {
			ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
			ISampleService service = (ISampleService) ctx.getBean("hivemindService");
			service.executeService("sample");

		}
		catch (Exception ex) {
			log.error("Error during execution", ex);
		}
		finally {
			if (ctx != null) {
				ctx.close();
			}
		}
	}
}
