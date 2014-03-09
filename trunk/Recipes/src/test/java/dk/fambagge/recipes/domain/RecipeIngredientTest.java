/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.domain.Measure.Volume;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gof
 */
public class RecipeIngredientTest {
    
    public RecipeIngredientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getMeasure method, of class RecipeIngredient.
     */
    @Test
    public void testGetMeasure() {
        System.out.println("getMeasure");
        RecipeIngredient instance = new RecipeIngredient(null, 0, Measure.Volume.TEASPOON);
        Measure expResult = Measure.Volume.TEASPOON;
        Measure result = instance.getMeasure();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAmount method, of class RecipeIngredient.
     */
    @Test
    public void testGetAmount() {
        System.out.println("getAmount");
        RecipeIngredient instance = new RecipeIngredient(null, 15.23, Measure.Volume.TEASPOON);
        double expResult = 15.23;
        double result = instance.getAmount();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getIngredient method, of class RecipeIngredient.
     */
    @Test
    public void testGetIngredient() {
        System.out.println("getIngredient");
        Ingredient ingredient = new Ingredient();
        RecipeIngredient instance = new RecipeIngredient(ingredient, 15.23, Measure.Volume.TEASPOON);
        Ingredient expResult = ingredient;
        Ingredient result = instance.getIngredient();
        assertEquals(expResult, result);
    }

    /**
     * Test of convertUnit method, of class RecipeIngredient.
     */
    @Test
    public void getAmountMeasure() {
        System.out.println("getAmount(Measure)");
        Ingredient ingredient = new Ingredient();
        ingredient.setWeightToVolume(1000);
        Measure outputMeasure = Volume.LITER;
        RecipeIngredient instance = new RecipeIngredient(ingredient, 1, Measure.Weight.KILOGRAM);;
        double expResult = 1;
        double result = instance.getAmount(outputMeasure);
        assertEquals(expResult, result, 0.0);
    }
    
}
