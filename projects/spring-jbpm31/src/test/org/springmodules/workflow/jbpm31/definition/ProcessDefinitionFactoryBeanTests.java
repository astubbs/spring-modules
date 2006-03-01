/**
 * Created on Feb 22, 2006
 *
 * $Id: ProcessDefinitionFactoryBeanTests.java,v 1.1 2006/03/01 16:55:28 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.jbpm31.definition;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Costin Leau
 *
 */
public class ProcessDefinitionFactoryBeanTests extends TestCase {

	private ProcessDefinitionFactoryBean definition;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		definition = new ProcessDefinitionFactoryBean();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		definition = null;
	}

	public void testAfterPropertiesSet() {
		try {
			definition.afterPropertiesSet();
			fail("expected exception");
		}
		catch (Exception e) {
			// expected
		}
	}
	
	public void testLoadResource() throws Exception
	{
		Resource config = new ClassPathResource("org/springmodules/workflow/jbpm31/simpleWorkflow.xml");
		definition.setDefinitionLocation(config);
		definition.afterPropertiesSet();
		assertNotNull(definition.getObject());
		// can't verify since equals is not yet implemented
		//assertEquals(ProcessDefinition.parseXmlReader(new FileReader(configurationResource.getFile())), definition.getObject());
	}
}
