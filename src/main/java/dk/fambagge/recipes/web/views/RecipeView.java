/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.domain.Recipe;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gof
 */
@ManagedBean
@ViewScoped
public class RecipeView {
    private Recipe selectedRecipe;

    @PostConstruct
    private void init() {
        selectedRecipe = Recipe.getAll().get(0);
    }
    
    /**
     * @return the selectedRecipe
     */
    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }

    /**
     * @param selectedRecipe the selectedRecipe to set
     */
    public void setSelectedRecipe(Recipe selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
    }
    
    
}
