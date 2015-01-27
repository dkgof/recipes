/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.servlets.beans;

/**
 *
 * @author Gof
 */
public class CalculateDensityData {

    private String calculateDensityWeightUnit;
    private String calculateDensityVolumeUnit;
    private double calculateDensityWeight;
    private double calculateDensityVolume;

    /**
     * @return the calculateDensityWeightUnit
     */
    public String getCalculateDensityWeightUnit() {
        return calculateDensityWeightUnit;
    }

    /**
     * @param calculateDensityWeightUnit the calculateDensityWeightUnit to set
     */
    public void setCalculateDensityWeightUnit(String calculateDensityWeightUnit) {
        this.calculateDensityWeightUnit = calculateDensityWeightUnit;
    }

    /**
     * @return the calculateDensityVolumeUnit
     */
    public String getCalculateDensityVolumeUnit() {
        return calculateDensityVolumeUnit;
    }

    /**
     * @param calculateDensityVolumeUnit the calculateDensityVolumeUnit to set
     */
    public void setCalculateDensityVolumeUnit(String calculateDensityVolumeUnit) {
        this.calculateDensityVolumeUnit = calculateDensityVolumeUnit;
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
}
