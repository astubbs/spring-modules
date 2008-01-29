/*
 * Copyright 2005-2008 the original author or authors.
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
package org.springmodules.examples.jcr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
