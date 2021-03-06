/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Recipe;
import dk.fambagge.recipes.web.data.LazyCustomList;
import java.io.Serializable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author Gof
 */
@Named
@ViewScoped
public class RecipesView implements Serializable {

    private LazyCustomList<Recipe> lazyModel;
    
    @PostConstruct
    public void init() {
        lazyModel = new LazyCustomList(Recipe.class);
    }
    
    public LazyCustomList<Recipe> getRecipesLazy() {
        return lazyModel;
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
