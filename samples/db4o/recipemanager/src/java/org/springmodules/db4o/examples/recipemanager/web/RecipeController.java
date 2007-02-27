package org.springmodules.db4o.examples.recipemanager.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springmodules.db4o.examples.recipemanager.persistence.RecipeManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * @author Daniel Mitterdorfer
 *
 */
public class RecipeController extends MultiActionController {
	private RecipeManager recipeManager;
	
	public void setRecipeManager(RecipeManager recipeManager) {
		this.recipeManager = recipeManager;
	}
	
	public ModelAndView welcomeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return new ModelAndView("welcomeView");
	}
	
	public ModelAndView recipesHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return new ModelAndView("recipesView", "recipes", recipeManager.getAllRecipes());
	}
}
