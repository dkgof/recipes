/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.fambagge.recipes.web;

/**
 *
 * @author Gof
 */
public class CreateIngredientData {
    private String preferedMeasure;
    private String name;
    private double weightToVolume;
    private double energyPerHundred;

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
    public String getPreferedMeasure() {
        return preferedMeasure;
    }

    /**
     * @param preferedMeasure the preferedMeasure to set
     */
    public void setPreferedMeasure(String preferedMeasure) {
        this.preferedMeasure = preferedMeasure;
    }
}
