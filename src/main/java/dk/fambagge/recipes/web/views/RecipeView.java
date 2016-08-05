/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Constants;
import dk.fambagge.recipes.domain.Recipe;
import dk.fambagge.recipes.domain.RecipeIngredient;
import dk.fambagge.recipes.domain.RecipeStep;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gof
 */
@ManagedBean
@ViewScoped
public class RecipeView  implements Serializable {
    private Recipe selectedRecipe = null;
    
    /**
     * @return the selectedRecipe
     */
    public Recipe getSelectedRecipe() {
        int recipeId = -1;
        try {
            recipeId = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("recipe"));
        } catch(Exception e) {
            //Ignore
        }
        
        if(selectedRecipe == null) {
            if(recipeId != -1) {
                //We didnt have any recipe
                selectedRecipe = Recipe.get(recipeId);
            }
        } else {
            if(recipeId != -1 && selectedRecipe.getId() != recipeId) {
                //We had a different recipe, update to new
                selectedRecipe = Recipe.get(recipeId);
            }
        }
        
        return selectedRecipe;
    }

    public List<RecipeStep> getStepsSorted() {
        List<RecipeStep> sortedSteps = new LinkedList<>();
        
        Set<RecipeStep> steps = getSelectedRecipe().getSteps();
        
        sortedSteps.addAll(steps);
        
        Collections.sort(sortedSteps, (RecipeStep step1, RecipeStep step2) -> Integer.compare(step1.getSortOrder(), step2.getSortOrder()));
        
        return sortedSteps;
    }
    
    public List<RecipeIngredient> getIngredientsSorted() {
        List<RecipeIngredient> sortedIngredients = new LinkedList<>();
        
        Set<RecipeIngredient> ingredients = getSelectedRecipe().getIngredients();
        
        sortedIngredients.addAll(ingredients);
        
        Collections.sort(sortedIngredients, (RecipeIngredient ingredient1, RecipeIngredient ingredient2) -> {
            return ingredient1.getIngredient().getName().compareTo(ingredient2.getIngredient().getName());
        });
        
        return sortedIngredients;
    }

    public void addStep() {
        RecipeStep step = new RecipeStep();
        getSelectedRecipe().addStep(step);
        
        step.setSortOrder(getSelectedRecipe().getNextStepSortOrder());
        
        Database.saveOrUpdate(getSelectedRecipe());
    }
    
    public int getCaloriesPerServing(RecipeIngredient ingredient) {
        
        double calories = ingredient.getEnergyInKiloJoules() * Constants.KCAL_PER_KILOJOULE;
        
        calories /= selectedRecipe.getServings();
        
        return (int) Math.round(calories);
    }
    
    public void inlineIngredientSave(RecipeIngredient ingredient) {
        Database.saveOrUpdate(ingredient);
    }
}
