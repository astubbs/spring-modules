package org.springmodules.commons.collections.functors;


/**
 * @author Steven Devijver
 * @since 29-04-2005
 *
 */
public class IfParserClosureFactoryBeanTests extends AbstractOrderTestCase {

	
	public IfParserClosureFactoryBeanTests() {
		super();
	}

	public IfParserClosureFactoryBeanTests(String arg0) {
		super(arg0);
	}

	protected String getBeanConfig() { return "org/springmodules/commons/collections/functors/if-parser-closure-tests.xml"; }
	
	public void test1() {
		Object order = getOrder(500, "bronze");
		getClosure().execute(order);
		assertEquals("low", getPriority(order));
	}
	
	public void test2() {
		Object order = getOrder(1000, "bronze");
		getClosure().execute(order);
		assertEquals("medium", getPriority(order));
	}
	
	public void test3() {
		Object order = getOrder(500, "silver");
		getClosure().execute(order);
		assertEquals("medium", getPriority(order));
	}
	
	public void test4() {
		Object order = getOrder(500, "gold");
		getClosure().execute(order);
		assertEquals("high", getPriority(order));
	}
	
	public void test5() {
		Object order = getOrder(5000, "bronze");
		getClosure().execute(order);
		assertEquals("high", getPriority(order));
	}
}
