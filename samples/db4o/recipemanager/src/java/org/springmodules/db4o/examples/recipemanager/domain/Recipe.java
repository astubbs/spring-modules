package org.springmodules.db4o.examples.recipemanager.domain;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Daniel Mitterdorfer
 * @since 07.12.2005
 *
 */
public class Recipe implements Supplier {
	private String title;
	private Category category;
	private int servings;
	private String instructions;
	private Collection ingredients;
	
	
	public Recipe(String title) {
		this.title = title;
		this.ingredients = new ArrayList();
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
}
