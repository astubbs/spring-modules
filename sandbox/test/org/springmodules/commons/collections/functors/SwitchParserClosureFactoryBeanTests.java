package org.springmodules.commons.collections.functors;

/**
 * @author Steven Devijver
 * @since 29-04-2005
 */
public class SwitchParserClosureFactoryBeanTests extends AbstractOrderTestCase {

	public SwitchParserClosureFactoryBeanTests() {
		super();
	}

	public SwitchParserClosureFactoryBeanTests(String arg0) {
		super(arg0);
	}

	protected String getBeanConfig() {
		return "org/springmodules/commons/collections/functors/switch-parser-closure-tests.xml";
	}
	
	public void test1() {
		Object order = getOrder(500, "bronze");
		getClosure().execute(order);
		assertEquals("low", getPriority(order));
	}

}
