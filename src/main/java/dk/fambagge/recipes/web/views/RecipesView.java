/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Recipe;
import dk.fambagge.recipes.web.data.LazyRecipeList;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gof
 */
@ManagedBean
@ViewScoped
public class RecipesView implements Serializable {

    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new LinkedList<>(Recipe.getAll());;
        Collections.sort(recipes, (Recipe recipe1, Recipe recipe2) -> {
            return recipe1.getName().compareTo(recipe2.getName());
        });
        return recipes;
    }
    
    public LazyRecipeList getRecipesLazy() {
        return new LazyRecipeList();
    }

    public Set<Ingredient> getAvailableIngredients() {
        return Ingredient.getAll();
    }
    
    public void delete(Recipe recipe) {
        Logger.getLogger("Recipes").log(Level.INFO, "Delete: {0}", recipe.getId());
        
        Database.delete(recipe);
        
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Recipe deleted"));
    }
}
