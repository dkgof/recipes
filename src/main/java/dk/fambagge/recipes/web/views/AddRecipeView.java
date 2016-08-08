/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Measure;
import dk.fambagge.recipes.domain.Recipe;
import dk.fambagge.recipes.domain.RecipeIngredient;
import dk.fambagge.recipes.domain.RecipeStep;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

/**
 *
 * @author Gof
 */
@ManagedBean
@ViewScoped
public class AddRecipeView implements Serializable {
    @Size(min=2, max=25, message="Recipe name must be between 2-25 characters long")
    private String name;

    @DecimalMin(value="1", message="Servings must be at least 1")
    private int servings = 4;
    
    @Size(min=1, max=255, message="You must have at least 1 ingredient in a recipe")
    private final List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    
    @Size(min=1, max=255, message="You must have at least 1 step in a recipe")
    private final List<RecipeStep> recipeSteps = new ArrayList<>();

    public void submitRecipe() {
        Recipe recipe = new Recipe(name, servings);
        recipeIngredients.stream().forEach((i) -> {
            recipe.addIngredient(i);
        });
        
        recipeSteps.stream().forEach((step) -> {
            recipe.addStep(step);
        });
        
        Database.save(recipe);

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Recipe added"));
        
        reset();
    }

    public void reset() {
        name = "";
        servings = 4;
        recipeIngredients.clear();
        recipeSteps.clear();
    }
    
    @Size(min=2, max=512, message="Step description must be between 2-512 characters")
    private String recipeStepDescription;
    
    public void addRecipeStep() {
        RecipeStep recipeStep = new RecipeStep(getRecipeStepDescription());
        getRecipeSteps().add(recipeStep);
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the servings
     */
    public int getServings() {
        return servings;
    }

    /**
     * @param servings the servings to set
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    /**
     * @return the ingredients
     */
    public List<RecipeIngredient> getIngredients() {
        return getRecipeIngredients();
    }

    /**
     * @param ingredients the ingredients to set
     */
    /*
    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.recipeIngredients = ingredients;
    }
    */

    /**
     * @return the recipeIngredients
     */
    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    /**
     * @return the recipeSteps
     */
    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    /**
     * @return the recipeStepDescription
     */
    public String getRecipeStepDescription() {
        return recipeStepDescription;
    }

    /**
     * @param recipeStepDescription the recipeStepDescription to set
     */
    public void setRecipeStepDescription(String recipeStepDescription) {
        this.recipeStepDescription = recipeStepDescription;
    }
}
