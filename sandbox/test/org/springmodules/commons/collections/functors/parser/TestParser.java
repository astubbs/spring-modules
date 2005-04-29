package org.springmodules.commons.collections.functors.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.springmodules.validation.valang.ParseException;
import org.springmodules.validation.valang.ValangParser;

/**
 * @author Steven Devijver
 * @since 28-04-2005
 */
public class TestParser extends TestCase {

	public TestParser() {
		super();
	}

	public TestParser(String arg0) {
		super(arg0);
	}

	public class Order {
		private Collection orderLines = new ArrayList();
		private double reduction = 0;
		
		public class OrderLine {
			private int quantity = 0;
			private String description = null;
			private double unitPrice = 0;
			
			public OrderLine(int quantity, String description, double unitPrice) {
				this.quantity = quantity;
				this.description = description;
				this.unitPrice = unitPrice;
			}
			
			public double getTotal() { return quantity * unitPrice; }
		}
		
		public void addOrderLine(int quantity, String description, double unitPrice) {
			this.orderLines.add(new OrderLine(quantity, description, unitPrice));
		}
		
		public double getTotal() {
			double total = 0;
			for (Iterator iter = this.orderLines.iterator(); iter.hasNext();) {
				OrderLine orderLine = (OrderLine)iter.next();
				total += orderLine.getTotal();
			}
			return total;
		}
		
		public double getNetTotal() { return getTotal() - (getTotal() * this.reduction); }
		
		public void setReduction(double reduction) { this.reduction = reduction; }
		
		public double getReduction() { return this.reduction; }
		
		public int getOrderLineCount() { return this.orderLines.size(); }
	}
	
	private void assertTrue(String text, Order order) {
		assertTrue(parse(text, order));
	}
	
	private void assertFalse(String text, Order order) {
		assertFalse(parse(text, order));
	}
	
	private boolean parse(String text, Order order) {
		try {
			return new ValangParser(new StringReader(text)).parseExpression().evaluate(order);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void testParser1() {
		Order order = new Order();
		order.addOrderLine(1, "Apple iPod 20Gb", 350);
		order.addOrderLine(1, "Skin iPod click wheel green", 20);
		
		assertTrue("total < 500", order);
		assertFalse("total >= 500", order);
		
		assertTrue("total < 200 or (200 <= total and orderLineCount > 1)", order);
		assertTrue("not total not between 200 and 500", order);
		
		assertFalse("orderLineCount in 1, 3, 5", order);
	}
}
