package org.springmodules.commons.collections.functors;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.collections.Closure;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.validation.functions.BeanPropertyFunction;
import org.springmodules.validation.functions.Function;

/**
 * @author Steven Devijver
 * @since 29-04-2005
 *
 */
public class IfParserClosureFactoryBeanTests extends TestCase {

	private static ThreadLocal tl = new ThreadLocal();
	
	public IfParserClosureFactoryBeanTests() {
		super();
	}

	public IfParserClosureFactoryBeanTests(String arg0) {
		super(arg0);
	}

	private Map getOrder(int totalAmount, String qualification) {
		Map customer = new HashMap();
		customer.put("qualification", qualification);
		Map order = new HashMap();
		order.put("totalAmount", new Integer(totalAmount));
		order.put("customer", customer);
		return order;
	}
	
	private String getPriority(Object order) {
		Function f = new BeanPropertyFunction("priority");
		return (String)f.getResult(order);
	}
	
	private Closure getClosure() {
		if (tl.get() == null) { 
			tl.set(new ClassPathXmlApplicationContext("org/springmodules/commons/collections/functors/if-parser-closure-tests.xml"));
		}
		ApplicationContext appCtx = (ApplicationContext)tl.get();
		return (Closure)appCtx.getBean("applyPriority");
	}
	
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
