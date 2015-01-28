/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Measure;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

/**
 *
 * @author Gof
 */
@ManagedBean
public class AddIngredientView implements Serializable {
    private String name;
    private double weightToVolume;
    private double energyPerHundred;
    private Measure preferedMeasure;

    public void submitIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setEnergyPerHundred(energyPerHundred);
        ingredient.setPreferredMeasure(preferedMeasure);
        ingredient.setWeightToVolume(weightToVolume);
        ingredient.save();

        //Reset data for next dialog
        name = "";
        weightToVolume = 0.0;
        energyPerHundred = 0.0;
        preferedMeasure = null;
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
}
