package org.springmodules.validation.valang.functions;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

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

        public Customer() { super(); }
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

    public void test4() {
        BeanPropertyFunction f = new BeanPropertyFunction("test.customer.name");
        Map test = new HashMap();
        test.put("customer", new Customer("Steven"));
        Map target = new HashMap();
        target.put("test", test);
        assertEquals("Steven", f.getResult(target));
    }

    public void test5() {
        LengthOfFunction f = new LengthOfFunction(
            new Function[] {
                new BeanPropertyFunction("test.customer.name")
            },
            0, 2
        );
        Map test = new HashMap();
        test.put("customer", new Customer("Uri"));
        Map target = new HashMap();
        target.put("test", test);
        assertEquals(new Integer(3), f.getResult(target));
    }
}
