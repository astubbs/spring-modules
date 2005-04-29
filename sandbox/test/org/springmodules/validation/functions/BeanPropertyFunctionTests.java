package org.springmodules.validation.functions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springmodules.validation.functions.BeanPropertyFunction;

import junit.framework.TestCase;

/**
 * @author Steven Devijver
 * @since 29-04-2005
 */
public class BeanPropertyFunctionTests extends TestCase {

	public BeanPropertyFunctionTests() {
		super();
	}

	public BeanPropertyFunctionTests(String arg0) {
		super(arg0);
	}

	public class Customer {
		private String name = null;
		
		public Customer() { super(); };
		public Customer(String name) { this(); setName(name); }
		
		public String getName() { return this.name; }
		public void setName(String name) { this.name = name; }
	}
	
	public void test1() {
		BeanPropertyFunction f = new BeanPropertyFunction("name");
		assertEquals("Steven", f.getResult(new Customer("Steven")));
	}
	
	public void test2() {
		BeanPropertyFunction f = new BeanPropertyFunction("name");
		BeanWrapper bw = new BeanWrapperImpl(new Customer("Steven"));
		assertEquals("Steven", f.getResult(bw));
	}
	
	public void test3() {
		BeanPropertyFunction f = new BeanPropertyFunction("customer.address.city");
		Map address = new HashMap();
		address.put("city", "Antwerpen");
		Map customer = new HashMap();
		customer.put("address", address);
		Map target = new HashMap();
		target.put("customer", customer);
		assertEquals("Antwerpen", f.getResult(target));
	}
}
