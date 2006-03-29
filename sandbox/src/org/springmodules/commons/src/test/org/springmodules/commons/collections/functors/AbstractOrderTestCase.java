package org.springmodules.commons.collections.functors;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.Closure;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.validation.functions.BeanPropertyFunction;
import org.springmodules.validation.functions.Function;

import junit.framework.TestCase;

/**
 * @author Steven Devijver
 * @since 29-0-2005
 */
public abstract class AbstractOrderTestCase extends TestCase {

	private static ThreadLocal tl = new ThreadLocal();
	
	public AbstractOrderTestCase() {
		super();
	}

	public AbstractOrderTestCase(String arg0) {
		super(arg0);
	}

	protected final Map getOrder(int totalAmount, String qualification) {
		Map customer = new HashMap();
		customer.put("qualification", qualification);
		Map order = new HashMap();
		order.put("totalAmount", new Integer(totalAmount));
		order.put("customer", customer);
		return order;
	}
	
	protected final String getPriority(Object order) {
		Function f = new BeanPropertyFunction("priority");
		return (String)f.getResult(order);
	}
	
	protected final Closure getClosure() {
		if (tl.get() == null) { 
			tl.set(new ClassPathXmlApplicationContext(getBeanConfig()));
		}
		ApplicationContext appCtx = (ApplicationContext)tl.get();
		return (Closure)appCtx.getBean("applyPriority");
	}

	protected abstract String getBeanConfig();

}
