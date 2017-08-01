/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.db.DomainObject;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

/**
 *
 * @author Gof
 */
@Entity
@Table(name = "ingredients")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ingredient implements Serializable, DomainObject {

    private int id;

    private String name;
    private double weightToVolume; //Gram to Liter ratio
    private Measure preferredMeasure;
    private double energyPerHundred; //Kilojoule per 100 gram

    private CustomMeasure customMeasure;

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
     * @return the weightToVolume
     */
    @Column(name = "weightToVolume", nullable = false)
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
     * @return the energyPerHoundred
     */
    @Column(name = "energyPerHundred", nullable = false)
    public double getEnergyPerHundred() {
        return energyPerHundred;
    }

    /**
     * @param energyPerHundred the energyPerHoundred to set
     */
    public void setEnergyPerHundred(double energyPerHundred) {
        this.energyPerHundred = energyPerHundred;
    }

    /**
     * @return the name
     */
    @Column(name = "name", nullable = false, length = 64, unique = true)
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
     * @return the preferedMeasure
     */
    @Type(type = "dk.fambagge.recipes.db.MeasureType")
    @Column(name = "preferredMeasure", nullable = false, length = 16)
    public Measure getPreferredMeasure() {
        return preferredMeasure;
    }

    /**
     * @param preferredMeasure the preferedMeasure to set
     */
    public void setPreferredMeasure(Measure preferredMeasure) {
        this.preferredMeasure = preferredMeasure;
    }

    /**
     * @return the customMeasures
     */
    @OneToOne(cascade = {CascadeType.ALL})
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "ingredient_custommeasures",
            joinColumns = {
                @JoinColumn(name = "ingredientId")},
            inverseJoinColumns = {
                @JoinColumn(name = "customMeasureId")}
    )
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public CustomMeasure getCustomMeasure() {
        return customMeasure;
    }

    /**
     * @param customMeasure the customMeasure to set
     */
    public void setCustomMeasure(CustomMeasure customMeasure) {
        this.customMeasure = customMeasure;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ingredient)) {
            return false;
        }

        Ingredient other = (Ingredient) obj;

        if (Double.doubleToLongBits(this.getEnergyPerHundred()) != Double.doubleToLongBits(other.getEnergyPerHundred())) {
            return false;
        }

        if (Double.doubleToLongBits(this.getWeightToVolume()) != Double.doubleToLongBits(other.getWeightToVolume())) {
            return false;
        }

        if (this.getId() != other.getId()) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.id;
        hash = 89 * hash + Objects.hashCode(this.preferredMeasure);
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.weightToVolume) ^ (Double.doubleToLongBits(this.weightToVolume) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.energyPerHundred) ^ (Double.doubleToLongBits(this.energyPerHundred) >>> 32));
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }

    public static Set<Ingredient> getAll() {
        return Database.getAll(Ingredient.class);
    }

    public static Ingredient get(int id) {
        return Database.get("id='"+id+"'", Ingredient.class);
    }
}
