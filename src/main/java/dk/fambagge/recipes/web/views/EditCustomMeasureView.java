/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.CustomMeasure;
import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Measure;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Gof
 */
@Named
@ViewScoped
public class EditCustomMeasureView implements Serializable {
    private String customMeasureName;
    private double customMeasureAmount;
    private Measure customMeasureReference;
    
    private Ingredient selectedIngredient;
    
    public void setupCustomEditor(Ingredient ingredient) {
        selectedIngredient = ingredient;
        
        System.out.println("Loading custom editor for: "+ingredient.getName());
        
        if(ingredient.getCustomMeasure() != null) {
            this.setCustomMeasureAmount(ingredient.getCustomMeasure().getReferenceToCustomRatio());
            this.setCustomMeasureName(ingredient.getCustomMeasure().getName());
            this.setCustomMeasureReference(ingredient.getCustomMeasure().getReferenceMeasure());
        } else {
            this.setCustomMeasureAmount(0.0);
            this.setCustomMeasureName("");
            this.setCustomMeasureReference(null);
        }
    }

    public void saveCustomMeasure() {
        if(selectedIngredient.getCustomMeasure() == null) {
            //No old custom measure
            CustomMeasure custom = new CustomMeasure(customMeasureName, customMeasureAmount, customMeasureReference);
            selectedIngredient.setCustomMeasure(custom);
            Database.saveOrUpdate(selectedIngredient);
        } else {
            //Update old
            CustomMeasure custom = selectedIngredient.getCustomMeasure();
            custom.setReferenceMeasure(customMeasureReference);
            custom.setReferenceToCustomRatio(customMeasureAmount);
            custom.setName(customMeasureName);
            Database.saveOrUpdate(custom);
        }
    }
    
    /**
     * @return the customMeasureName
     */
    public String getCustomMeasureName() {
        return customMeasureName;
    }

    /**
     * @param customMeasureName the customMeasureName to set
     */
    public void setCustomMeasureName(String customMeasureName) {
        this.customMeasureName = customMeasureName;
    }

    /**
     * @return the customMeasureAmount
     */
    public double getCustomMeasureAmount() {
        return customMeasureAmount;
    }

    /**
     * @param customMeasureAmount the customMeasureAmount to set
     */
    public void setCustomMeasureAmount(double customMeasureAmount) {
        this.customMeasureAmount = customMeasureAmount;
    }

    /**
     * @return the customMeasureReference
     */
    public Measure getCustomMeasureReference() {
        return customMeasureReference;
    }

    /**
     * @param customMeasureReference the customMeasureReference to set
     */
    public void setCustomMeasureReference(Measure customMeasureReference) {
        this.customMeasureReference = customMeasureReference;
    }
}
