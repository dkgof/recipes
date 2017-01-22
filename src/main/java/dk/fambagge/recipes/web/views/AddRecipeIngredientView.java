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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.validation.constraints.DecimalMin;

/**
 *
 * @author Gof
 */
@ManagedBean
@ViewScoped
public class AddRecipeIngredientView implements Serializable {
    @DecimalMin(value="0", message="Ingredient amount cannot be less than zero", inclusive = false)
    private double ingredientAmount = 0;
    
    private Ingredient ingredient;
    
    private Measure ingredientMeasure;

    /*
    public void addRecipeIngredient(Collection<RecipeIngredient> ingredients) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(getIngredient(), getIngredientAmount(), getIngredientMeasure());
        ingredients.add(recipeIngredient);
    }
    */
    
    public void addRecipeIngredient(Recipe recipe) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(getIngredient(), getIngredientAmount(), getIngredientMeasure());
        recipe.addIngredient(recipeIngredient);
        
        Database.saveOrUpdate(recipe);
        Database.refresh(recipe);
    }

    public void reset() {
        ingredient = null;
        ingredientAmount = 0;
        ingredientMeasure = null;
    }
    
    public void recipeIngredientChange(ValueChangeEvent event) {
        Ingredient selectedIngredient = (Ingredient) event.getNewValue();
        
        if(selectedIngredient.getCustomMeasure() != null) {
            ingredientMeasure = selectedIngredient.getCustomMeasure();
        } else {
            ingredientMeasure = selectedIngredient.getPreferredMeasure();
        }
    }
    
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> allIngredients = new LinkedList<>(Ingredient.getAll());
        
        Collections.sort(allIngredients, (Ingredient o1, Ingredient o2) -> {
            return o1.getName().compareTo(o2.getName());
        });
        
        return allIngredients;
    }
    
    public List<Measure> getMeasures() {
        List<Measure> measures = new LinkedList<>();
        
        if(ingredient != null && ingredient.getCustomMeasure() != null) {
            measures.add(ingredient.getCustomMeasure());
        }

        measures.addAll(Arrays.asList(Measure.Weight.values()));
        measures.addAll(Arrays.asList(Measure.Volume.values()));
        
        return measures;
    }
    
    /**
     * @return the ingredientAmount
     */
    public double getIngredientAmount() {
        return ingredientAmount;
    }

    /**
     * @param ingredientAmount the ingredientAmount to set
     */
    public void setIngredientAmount(double ingredientAmount) {
        this.ingredientAmount = ingredientAmount;
    }

    /**
     * @return the ingredient
     */
    public Ingredient getIngredient() {
        return ingredient;
    }

    /**
     * @param ingredient the ingredient to set
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    /**
     * @return the ingredientMeasure
     */
    public Measure getIngredientMeasure() {
        return ingredientMeasure;
    }

    /**
     * @param ingredientMeasure the ingredientMeasure to set
     */
    public void setIngredientMeasure(Measure ingredientMeasure) {
        this.ingredientMeasure = ingredientMeasure;
    }
}
