/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.domain.Measure.Volume;
import dk.fambagge.recipes.domain.Measure.Weight;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import org.hibernate.annotations.Type;

/**
 *
 * @author Gof
 */
@Entity
@Table( name = "RecipeIngredients" )
public class RecipeIngredient implements Serializable {
    
    private int id;
    private Ingredient ingredient;
    private Measure measure;
    private double amount;
    
    private CustomMeasure overrideMeasure;
    
    public RecipeIngredient() {
        id = -1;
        this.amount = 0;
        this.measure = null;
        this.ingredient = null;
        this.overrideMeasure = null;
    }
    
    public RecipeIngredient(Ingredient ingredient, double amount, Measure measure) {
        this.ingredient = ingredient;
        this.measure = measure;
        this.amount = amount;
        this.overrideMeasure = null;
    }

    /**
     * @return the measure
     */
    @Type( type = "dk.fambagge.recipes.db.MeasureType" )
    @Column(name = "measure", nullable = false, length = 16)
    public Measure getMeasure() {
        return measure;
    }

    /**
     * @return the amount
     */
    @Column(name = "amount", nullable = false)
    public double getAmount() {
        return amount;
    }
    
    /**
     * @return the ingredient
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn( name = "ingredientId", nullable = false)
    public Ingredient getIngredient() {
        return ingredient;
    }


    /**
     * @return the id
     */
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param ingredient the ingredient to set
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    /**
     * @param measure the measure to set
     */
    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the overrideMeasure
     */
    @ManyToOne
    @JoinColumn( name = "overrideMeasureId" )
    public CustomMeasure getOverrideMeasure() {
        return overrideMeasure;
    }

    /**
     * @param overrideMeasure the overrideMeasure to set
     */
    public void setOverrideMeasure(CustomMeasure overrideMeasure) {
        this.overrideMeasure = overrideMeasure;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof RecipeIngredient)) {
            return false;
        }
        
        RecipeIngredient other = (RecipeIngredient) obj;
        
        if(Double.doubleToLongBits(this.getAmount(Measure.Weight.GRAM)) != Double.doubleToLongBits(other.getAmount(Measure.Weight.GRAM))) {
            return false;
        }
        
        if(this.getIngredient() != other.getIngredient()) {
            return false;
        }
        
        if(this.getId() != other.getId()) {
            return false;
        }
        
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.ingredient);
        hash = 29 * hash + Objects.hashCode(this.measure);
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
        return hash;
    }

    private double convertWeight(double inputAmount, Weight inputMeasure, Weight outputMeasure) {
        double inputAsGrams = inputAmount * inputMeasure.getFactor();
        
        return inputAsGrams / outputMeasure.getFactor();
    }

    private double convertVolume(double inputAmount, Volume inputMeasure, Volume outputMeasure) {
        double inputAsLiters = inputAmount * inputMeasure.getFactor();
        
        return inputAsLiters / outputMeasure.getFactor();
    }
    
    public double getAmount(Measure outputMeasure) {
        Measure inputMeasure = this.measure;
        
        if(inputMeasure instanceof Weight && outputMeasure instanceof Weight) {
            //Both are Weight
            return convertWeight(amount, (Weight)inputMeasure, (Weight)outputMeasure);
        } else if(inputMeasure instanceof Volume && outputMeasure instanceof Volume) {
            //Both are Volume
            return convertVolume(amount, (Volume)inputMeasure, (Volume)outputMeasure);
        }
        
        if(ingredient.getWeightToVolume() == -1) {
            throw new UnsupportedOperationException("Cannot convert between weight and volume on: "+ingredient.getName());
        }
        
        if(inputMeasure instanceof Weight) {
            //Input is Weight, Output is Volume
            double inputAsGrams = convertWeight(amount, (Weight) this.getMeasure(), Weight.GRAM);
            
            double inputInLiter = inputAsGrams / ingredient.getWeightToVolume();
            
            return convertVolume(inputInLiter, Volume.LITER, (Volume)outputMeasure);
        } else {
            //Input is Volume, Output is Weight
            double inputAsLiter = convertVolume(amount, (Volume) this.getMeasure(), Volume.LITER);
            
            double inputAsGrams = inputAsLiter * ingredient.getWeightToVolume();
            
            return convertWeight(inputAsGrams, Weight.GRAM, (Weight)outputMeasure);
        }
    }

    @Transient
    public double getEnergyInKiloJoules() {
        double amountInGrams = this.getAmount(Measure.Weight.GRAM);
        
        return (amountInGrams/100.0) * ingredient.getEnergyPerHundred();
    }
}
