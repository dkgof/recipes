/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Recipe;
import dk.fambagge.recipes.domain.RecipeStep;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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

    public List<RecipeStep> getStepsSorted() {
        List<RecipeStep> sortedSteps = new LinkedList<>();
        
        Set<RecipeStep> steps = selectedRecipe.getSteps();
        
        sortedSteps.addAll(steps);
        
        Collections.sort(sortedSteps, (RecipeStep step1, RecipeStep step2) -> Integer.compare(step1.getSortOrder(), step2.getSortOrder()));
        
        return sortedSteps;
    }
    
    /**
     * @param selectedRecipe the selectedRecipe to set
     */
    public void setSelectedRecipe(Recipe selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
    }
    
    public void addStep() {
        RecipeStep step = new RecipeStep();
        selectedRecipe.addStep(step);
        
        step.setSortOrder(selectedRecipe.getNextStepSortOrder());
        
        Database.saveOrUpdate(selectedRecipe);
    }
}
