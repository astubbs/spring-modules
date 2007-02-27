package org.springmodules.db4o.examples.recipemanager.persistence;

import java.util.List;

import org.springmodules.db4o.examples.recipemanager.domain.Recipe;
import org.springmodules.db4o.examples.recipemanager.domain.Unit;

public interface RecipeManager {
	public List getAllUnits();
	public void saveUnit(Unit unit);
	public List getAllRecipes();
	public void saveRecipe(Recipe r);
	public long getId(Object persistable);
	public Recipe getById(long id); 
}
