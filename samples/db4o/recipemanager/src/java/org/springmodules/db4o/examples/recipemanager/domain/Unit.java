package org.springmodules.db4o.examples.recipemanager.domain;

public class Unit {
	public String name;
	
	public Unit(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
}
