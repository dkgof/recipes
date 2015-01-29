/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Measure;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Gof
 */
@ManagedBean
public class AddIngredientView implements Serializable {
    @Size(min=2, max=25, message="Ingredient name must be between 2-25 characters long")
    private String name;
    @DecimalMin(value="0", message="Density can not be less than 0")
    private double weightToVolume;
    @DecimalMin(value="0", message="Energy can not be less than 0")
    private double energyPerHundred;
    @NotNull(message="You must select a prefered measure")
    private Measure preferedMeasure;
    private Measure energyMeasure;

    public void submitIngredient() {
        try {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(name);
            ingredient.setEnergyPerHundred(energyPerHundred);
            ingredient.setPreferredMeasure(preferedMeasure);
            ingredient.setWeightToVolume(weightToVolume);
            ingredient.save();

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Ingredient added"));
            
            //Reset data for next dialog
            name = "";
            weightToVolume = 0.0;
            energyPerHundred = 0.0;
            preferedMeasure = null;
        } catch(Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error saving ingredient"));
            RequestContext.getCurrentInstance().addCallbackParam("errorSaving", true);
        }
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
}
