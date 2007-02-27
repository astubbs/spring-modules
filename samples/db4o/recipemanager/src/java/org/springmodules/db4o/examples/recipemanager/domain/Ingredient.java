package org.springmodules.db4o.examples.recipemanager.domain;

public class Ingredient {
	private double amount;
	private Unit unit;
	
	public Ingredient(double amount, Unit unit) {
		this.amount = amount;
		this.unit = unit;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(amount);
		sb.append(unit);
		
		return sb.toString();
	}
}
