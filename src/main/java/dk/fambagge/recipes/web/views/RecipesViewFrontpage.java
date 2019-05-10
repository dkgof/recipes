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
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author Gof
 */
@Named
@ViewScoped
public class RecipesViewFrontpage implements Serializable {

    private LazyCustomList<Recipe> lazyModel;
    
    @Inject
    RecipesViewSession recipesViewSession;
    
    @PostConstruct
    public void init() {
        lazyModel = new LazyCustomList(Recipe.class);
        lazyModel.setPageSize(recipesViewSession.getRowPerPage());
        lazyModel.setFilter(recipesViewSession.getFilterString());
    }
    
    public LazyCustomList<Recipe> getRecipesLazy() {
        return lazyModel;
    }

    public void setFilter(String filterString) {
        recipesViewSession.setFilterString(filterString);
        lazyModel.setFilter(filterString);
    }
    
    public String getFilter() {
        return recipesViewSession.getFilterString();
    }
    
    public String createGridCacheKey(Recipe recipe) {
        StringBuilder sb = new StringBuilder();
        
        return "Recipe#"+(recipe!=null?recipe.getId():-1);
    }
}
