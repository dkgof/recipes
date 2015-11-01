/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Recipe;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
    private List<Recipe> recipes;
    
    @PostConstruct
    public void init() {
        reload();
    }
    
    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void reload() {
        this.recipes = Recipe.getAll();
    }
    
    public List<Ingredient> getAvailableIngredients() {
        return Ingredient.getAll();
    }
    
    public void delete(Recipe recipe) {
        Logger.getLogger("Recipes").log(Level.INFO, "Delete: "+recipe.getId());
        
        Database.delete(recipe);
        reload();
        
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Recipe deleted"));
    }
}
