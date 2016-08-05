/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Measure;
import dk.fambagge.recipes.domain.RecipeIngredient;
import java.util.Collection;
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
public class AddRecipeIngredientView {
    @DecimalMin(value="0", message="Ingredient amount cannot be less than zero", inclusive = false)
    
    private double ingredientAmount = 0;
    
    private Ingredient ingredient;
    
    private Measure ingredientMeasure;

    public void addRecipeIngredient(Collection<RecipeIngredient> ingredients) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(getIngredient(), getIngredientAmount(), getIngredientMeasure());
        ingredients.add(recipeIngredient);
    }
    
    public void reset() {
        ingredient = null;
        ingredientAmount = 0;
        ingredientMeasure = null;
    }
    
    public void recipeIngredientChange(ValueChangeEvent event) {
        Ingredient selectedIngredient = (Ingredient) event.getNewValue();
        ingredientMeasure = selectedIngredient.getPreferredMeasure();
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
