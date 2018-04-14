/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Constants;
import dk.fambagge.recipes.domain.CustomMeasure;
import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Measure;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Gof
 */
@Named
@ViewScoped
public class AddIngredientView implements Serializable {
    @Size(min=2, max=64, message="Ingredient name must be between 2-64 characters long")
    private String name;
    @DecimalMin(value="0", message="Density can not be less than 0")
    private double weightToVolume;
    @DecimalMin(value="0", message="Energy can not be less than 0")
    private double energyPerHundred;
    @NotNull(message="You must select a prefered measure")
    private Measure preferedMeasure;
    private Measure energyMeasure;
    
    private String customMeasureName;
    private double customMeasureAmount;
    private Measure customMeasureReference;

    public void submitIngredient() {
        try {
            
            double correctedEnergy = energyPerHundred;
            
            if(energyMeasure == Measure.Energy.CALORIES) {
                correctedEnergy = Math.round(energyPerHundred / Constants.KCAL_PER_KILOJOULE * 10.0) / 10.0;
            }
            
            Ingredient ingredient = new Ingredient();
            ingredient.setName(name);
            ingredient.setEnergyPerHundred(correctedEnergy);
            ingredient.setPreferredMeasure(preferedMeasure);
            ingredient.setWeightToVolume(weightToVolume);
            
            //Custom measure
            if(!customMeasureName.trim().isEmpty() && customMeasureReference != null) {
                CustomMeasure custom = new CustomMeasure(customMeasureName, customMeasureAmount, customMeasureReference);
                ingredient.setCustomMeasure(custom);
                Database.save(custom);
            }
            
            Database.save(ingredient);

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Ingredient added"));
            
            //Reset data for next dialog
            name = "";
            weightToVolume = 0.0;
            energyPerHundred = 0.0;
            preferedMeasure = null;
            customMeasureAmount = 0.0;
            customMeasureName = "";
            customMeasureReference = null;
        } catch(Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error saving ingredient", e.getMessage()));
            PrimeFaces.current().ajax().addCallbackParam("errorSaving", true);
            e.printStackTrace();
        }
    }
    
    @DecimalMin(value="0", message="Weight can not be less than 0")
    private double calculateDensityWeight;
    
    @DecimalMin(value="0", message="Volume can not be less than 0")
    private double calculateDensityVolume;
    
    @NotNull(message="You must select a weight unit")
    private Measure calculateDensityWeightUnit;

    @NotNull(message="You must select a volume unit")
    private Measure calculateDensityVolumeUnit;
    
    public void calculateDensity() {
            double grams = calculateDensityWeightUnit.convertTo(calculateDensityWeight, Measure.Weight.GRAM);
            double liters = calculateDensityVolumeUnit.convertTo(calculateDensityVolume, Measure.Volume.LITER);

            weightToVolume = grams / liters;
            
            calculateDensityVolume = 0.0;
            calculateDensityWeight = 0.0;
            calculateDensityWeightUnit = null;
            calculateDensityVolumeUnit = null;
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
     * @return the weightToVolume
     */
    public double getWeightToVolume() {
        return weightToVolume;
    }

    /**
     * @param weightToVolume the weightToVolume to set
     */
    public void setWeightToVolume(double weightToVolume) {
        this.weightToVolume = weightToVolume;
    }

    /**
     * @return the energyPerHundred
     */
    public double getEnergyPerHundred() {
        return energyPerHundred;
    }

    /**
     * @param energyPerHundred the energyPerHundred to set
     */
    public void setEnergyPerHundred(double energyPerHundred) {
        this.energyPerHundred = energyPerHundred;
    }

    /**
     * @return the preferedMeasure
     */
    public Measure getPreferedMeasure() {
        return preferedMeasure;
    }

    /**
     * @param preferedMeasure the preferedMeasure to set
     */
    public void setPreferedMeasure(Measure preferedMeasure) {
        this.preferedMeasure = preferedMeasure;
    }
    
    public List<Measure> getEnergyMeasures() {
        List<Measure> measures = new LinkedList<>();
        measures.addAll(Arrays.asList(Measure.Energy.values()));
        return measures;
    }

    public List<SelectItem> getMeasures() {
        List<SelectItem> measures = new LinkedList<>();
        
        SelectItemGroup g1 = new SelectItemGroup("Weight");
        SelectItemGroup g2 = new SelectItemGroup("Volume");
        
        List<SelectItem> g1Items = new LinkedList<>();
        for(Measure m : Measure.Weight.values()) {
            g1Items.add(new SelectItem(m, m.getSymbol()));
        }
        g1.setSelectItems(g1Items.toArray(new SelectItem[0]));

        List<SelectItem> g2Items = new LinkedList<>();
        for(Measure m : Measure.Volume.values()) {
            g2Items.add(new SelectItem(m, m.getSymbol()));
        }
        g2.setSelectItems(g2Items.toArray(new SelectItem[0]));
        
        measures.add(g1);
        measures.add(g2);
        
        return measures;
    }

    public List<SelectItem> getWeightMeasures() {
        List<SelectItem> measures = new LinkedList<>();
        
        for(Measure m : Measure.Weight.values()) {
            measures.add(new SelectItem(m, m.getSymbol()));
        }
        
        return measures;
    }

    public List<SelectItem> getVolumeMeasures() {
        List<SelectItem> measures = new LinkedList<>();
        
        for(Measure m : Measure.Volume.values()) {
            measures.add(new SelectItem(m, m.getSymbol()));
        }
        
        return measures;
    }

    /**
     * @return the energyMeasure
     */
    public Measure getEnergyMeasure() {
        return energyMeasure;
    }

    /**
     * @param energyMeasure the energyMeasure to set
     */
    public void setEnergyMeasure(Measure energyMeasure) {
        this.energyMeasure = energyMeasure;
    }

    /**
     * @return the calculateDensityWeight
     */
    public double getCalculateDensityWeight() {
        return calculateDensityWeight;
    }

    /**
     * @param calculateDensityWeight the calculateDensityWeight to set
     */
    public void setCalculateDensityWeight(double calculateDensityWeight) {
        this.calculateDensityWeight = calculateDensityWeight;
    }

    /**
     * @return the calculateDensityVolume
     */
    public double getCalculateDensityVolume() {
        return calculateDensityVolume;
    }

    /**
     * @param calculateDensityVolume the calculateDensityVolume to set
     */
    public void setCalculateDensityVolume(double calculateDensityVolume) {
        this.calculateDensityVolume = calculateDensityVolume;
    }

    /**
     * @return the calculateDensityWeightUnit
     */
    public Measure getCalculateDensityWeightUnit() {
        return calculateDensityWeightUnit;
    }

    /**
     * @param calculateDensityWeightUnit the calculateDensityWeightUnit to set
     */
    public void setCalculateDensityWeightUnit(Measure calculateDensityWeightUnit) {
        this.calculateDensityWeightUnit = calculateDensityWeightUnit;
    }

    /**
     * @return the calculateDensityVolumeUnit
     */
    public Measure getCalculateDensityVolumeUnit() {
        return calculateDensityVolumeUnit;
    }

    /**
     * @param calculateDensityVolumeUnit the calculateDensityVolumeUnit to set
     */
    public void setCalculateDensityVolumeUnit(Measure calculateDensityVolumeUnit) {
        this.calculateDensityVolumeUnit = calculateDensityVolumeUnit;
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
