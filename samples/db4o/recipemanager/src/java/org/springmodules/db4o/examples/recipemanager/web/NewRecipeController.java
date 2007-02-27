package org.springmodules.db4o.examples.recipemanager.web;

import javax.servlet.http.HttpServletRequest;

import org.springmodules.db4o.examples.recipemanager.domain.Recipe;
import org.springmodules.db4o.examples.recipemanager.persistence.RecipeManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;


public class NewRecipeController extends SimpleFormController{
	private static final String RECIPE_ID_ATTRIBUTE = "id";
    private RecipeManager recipeManager;

    public void setRecipeManager(RecipeManager recipeManager) {
		this.recipeManager = recipeManager;
	}
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
      String idAsString = request.getParameter(RECIPE_ID_ATTRIBUTE);
      if (idAsString != null) {
	      long id = Long.parseLong(idAsString);
	      return recipeManager.getById(id);
      } else {
    	  return new Recipe(""); 
      }
    }
    
    protected ModelAndView onSubmit(Object command) throws Exception {
    	Recipe r = (Recipe) command;
    	recipeManager.saveRecipe(r);
    
    	System.out.println("Saving recipe: " + r.getTitle());
    	
		RedirectView view = new RedirectView(getSuccessView());
		view.addStaticAttribute(RECIPE_ID_ATTRIBUTE, Long.valueOf(recipeManager.getId(r)));
		
		System.out.println("Save recipe successfully. id=" + Long.valueOf(recipeManager.getId(r)));
		System.out.println(recipeManager.getAllRecipes().size() + " recipes saved.");
		
		return new ModelAndView(view);
    }
}
