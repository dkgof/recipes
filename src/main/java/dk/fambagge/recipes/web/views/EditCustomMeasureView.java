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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gof
 */
@ManagedBean
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
        CustomMeasure custom = new CustomMeasure(customMeasureName, customMeasureName, customMeasureAmount, customMeasureReference);
        
        CustomMeasure oldCustom = selectedIngredient.getCustomMeasure();
        
        selectedIngredient.setCustomMeasure(custom);
        Database.saveOrUpdate(selectedIngredient);
        
        if(oldCustom != null) {
            Database.delete(oldCustom);
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
