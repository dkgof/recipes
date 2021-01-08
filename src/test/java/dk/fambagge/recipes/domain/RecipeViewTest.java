/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.web.views.RecipeView;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rolf
 */
public class RecipeViewTest {
    
    public RecipeViewTest() {
    }
    
    @Test
    public void getFormattedValueTest() {
        Recipe recipe = new Recipe();
        recipe.setServings(1);
        
        Ingredient ingredient = new Ingredient();
        RecipeIngredient ri = new RecipeIngredient(ingredient, 0, Measure.Weight.GRAM);
        
        RecipeView view = new RecipeView();
        
        String scaledAmountFormatted = view.getFormattedValue(ri, 0.125);
        assertEquals("0,125", scaledAmountFormatted);

        scaledAmountFormatted = view.getFormattedValue(ri, 0.12);
        assertEquals("0,12", scaledAmountFormatted);
        
        scaledAmountFormatted = view.getFormattedValue(ri, 0.1);
        assertEquals("0,1", scaledAmountFormatted);
        
        scaledAmountFormatted = view.getFormattedValue(ri, 0);
        assertEquals("0", scaledAmountFormatted);
    }
}
