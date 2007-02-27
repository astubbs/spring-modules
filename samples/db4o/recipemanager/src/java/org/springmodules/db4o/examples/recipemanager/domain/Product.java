package org.springmodules.db4o.examples.recipemanager.domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
	private String name;
	private List validUnits;
	private List suppliers;
	
	public Product(String name, List suppliers) {
		this.name = name;
		this.validUnits = new ArrayList();
		this.suppliers = new ArrayList();
	}
	
	public void add(Supplier supplier) {
		suppliers.add(supplier);
	}
	
	public String toString() {
		return name;
	}
}
