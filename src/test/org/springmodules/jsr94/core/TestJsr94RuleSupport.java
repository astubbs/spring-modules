/**
 * 
 */
package org.springmodules.jsr94.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.jsr94.rulesource.RuleSource;

import junit.framework.TestCase;

/**
 * Tests the Jsr94RuleSupport operations
 * @author janm
 */
public class TestJsr94RuleSupport extends TestCase {
	private ApplicationContext context = new ClassPathXmlApplicationContext("org/springmodules/jsr94/rulesource.xml");

	/**
	 * afterPropertiesSet() test
	 * @throws Exception
	 */
	public void testAfterPropertiesSet() throws Exception {
		Jsr94RuleSupport support = new Jsr94RuleSupport();
		try {
			support.afterPropertiesSet();
			fail("No ruleSource or template set");
		} catch (IllegalArgumentException ex) {
			// expected
		}
		support.setRuleSource((RuleSource)context.getBean("ruleSource"));
		support.setTemplate((Jsr94Template)context.getBean("jsr94Template"));
		try {
			support.afterPropertiesSet();
			fail("Both ruleSource and template set");
		} catch (IllegalArgumentException ex) {
			// expected
		}
		support.setTemplate(null);
		support.afterPropertiesSet();
		assertNotNull("Template not constructed", support.getTemplate());
		
		support.setRuleSource(null);
		support.setTemplate((Jsr94Template)context.getBean("jsr94Template"));
		support.afterPropertiesSet();
		assertNotNull("RuleSource not retrieved", support.getRuleSource());
	}
	
	/**
	 * Testing business interface
	 * @author janm
	 */
	interface BusinessInterface {
		List businessMethod();
	}
	
	/**
	 * BusinessInterface implementation as subclass of Jsr94RuleSupport
	 * @author janm
	 */
	class SubclassedBusinessBean extends Jsr94RuleSupport implements BusinessInterface {
		
		public List businessMethod() {
			List inputList = new ArrayList();
			inputList.add("Gecko");
			return executeStateless("test", inputList);
		}
	}
	
	/**
	 * BusinessInterface implementation using Jsr94RuleSupport as subclass
	 * @author janm
	 */
	class DependencyBusinessBean implements BusinessInterface {
		private Jsr94RuleSupport support;
		
		public DependencyBusinessBean(Jsr94RuleSupport support) {
			this.support = support;
		}
		
		public List businessMethod() {
			List inputList = new ArrayList();
			inputList.add("Gecko");
			return support.executeStateless("test", inputList);
		}
		
	}

	/**
	 * Tests the result of the business bean execution
	 * @param intf The BusinessInterface implementation
	 */
	private void assertBusinessInterface(BusinessInterface intf) {
		List result = intf.businessMethod();
		assertEquals("Result should contain two facts", 2, result.size());
		assertTrue("Result does not contain Gecko", result.contains("Gecko"));
		assertTrue("Result does not contain a:Gecko", result.contains("a:Gecko"));
		
	}
	
	/**
	 * Test the stateless execution using Jsr94RuleSupport injected as dependency
	 * @throws Exception
	 */
	public void testDependency() throws Exception {
		Jsr94RuleSupport support = new Jsr94RuleSupport();
		support.setRuleSource((RuleSource)context.getBean("ruleSource"));
		support.afterPropertiesSet();
		assertBusinessInterface(new DependencyBusinessBean(support));
	}
	
	/**
	 * Test the stateless execution using Jsr94RuleSupport used as superclass
	 * @throws Exception
	 */
	public void testSubclass() throws Exception {
		SubclassedBusinessBean bean = new SubclassedBusinessBean();
		bean.setRuleSource((RuleSource)context.getBean("ruleSource"));
		bean.afterPropertiesSet();
		assertBusinessInterface(bean);
	}
}
