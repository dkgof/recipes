/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.fambagge.recipes.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Gof
 */
@SuppressWarnings("EmptyMethod")
public class RecipeTest {
    
    public RecipeTest() {
    }
    
    /**
     * Test of getEnergyInCalories method, of class Recipe.
     */
    @Test
    public void testGetEnergyInCalories() {
        System.out.println("getEnergyInCalories");
        Recipe instance = new Recipe();
        
        Ingredient water = new Ingredient();
        water.setName("Water");
        water.setPreferredMeasure(Measure.Volume.DECILITER);
        water.setEnergyPerHundred(0);
        water.setWeightToVolume(1000);
        
        Ingredient chocolate = new Ingredient();
        chocolate.setName("Marabou - Schweizer nød");
        chocolate.setEnergyPerHundred(2365);
        chocolate.setWeightToVolume(-1);
        chocolate.setPreferredMeasure(Measure.Weight.GRAM);
        
        Ingredient cream = new Ingredient();
        cream.setName("Fløde");
        cream.setEnergyPerHundred(1500);
        cream.setWeightToVolume(984);
        cream.setPreferredMeasure(Measure.Volume.DECILITER);

        instance.addIngredient(new RecipeIngredient(water, 5, Measure.Volume.DECILITER));
        instance.addIngredient(new RecipeIngredient(chocolate, 200, Measure.Weight.GRAM));
        instance.addIngredient(new RecipeIngredient(cream, 1, Measure.Volume.CUP));
        
        double expResult = (3690+2365*2.0) * Constants.KCAL_PER_KILOJOULE;
        double result = instance.getEnergyInCalories();
        
        System.out.println("KiloCalories: "+result);
        
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getEnergyInKiloJoule method, of class Recipe.
     */
    @Test
    public void testGetEnergyInKiloJoule() {
        System.out.println("getEnergyInKiloJoule");
        Recipe instance = new Recipe();
        
        Ingredient water = new Ingredient();
        water.setName("Water");
        water.setPreferredMeasure(Measure.Volume.DECILITER);
        water.setEnergyPerHundred(0);
        water.setWeightToVolume(1000);
        
        Ingredient chocolate = new Ingredient();
        chocolate.setName("Marabou - Schweizer nød");
        chocolate.setEnergyPerHundred(2365);
        chocolate.setWeightToVolume(-1);
        chocolate.setPreferredMeasure(Measure.Weight.GRAM);
        
        Ingredient cream = new Ingredient();
        cream.setName("Fløde");
        cream.setEnergyPerHundred(1500);
        cream.setWeightToVolume(984);
        cream.setPreferredMeasure(Measure.Volume.DECILITER);

        instance.addIngredient(new RecipeIngredient(water, 5, Measure.Volume.DECILITER));
        instance.addIngredient(new RecipeIngredient(chocolate, 200, Measure.Weight.GRAM));
        instance.addIngredient(new RecipeIngredient(cream, 1, Measure.Volume.CUP));
        
        double expResult = (3690+2365*2.0);
        double result = instance.getEnergyInKiloJoule();

        System.out.println("KiloJoules: "+result);
        
        assertEquals(expResult, result, 0.0);
    }
    
}
