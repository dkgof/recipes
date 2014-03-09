/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author Gof
 */
@Entity
@Table(name = "Recipes")
public class Recipe implements Serializable {

    private int id;

    private String name;

    private Set<RecipeIngredient> ingredients;

    private Set<RecipeStep> steps;

    private Set<Recipe> sidedishRecipes;

    public Recipe() {
        ingredients = new HashSet<>();
        steps = new HashSet<>();
        sidedishRecipes = new HashSet<>();
    }

    public void addIngredient(RecipeIngredient ingredient) {
        getIngredients().add(ingredient);
    }

    @Transient
    public double getEnergyInCalories() {
        return getEnergyInKiloJoule() * Constants.KCAL_PER_KILOJOULE;
    }

    @Transient
    public double getEnergyInKiloJoule() {
        double totalEnergy = 0;

        for (RecipeIngredient ingredient : getIngredients()) {
            totalEnergy += ingredient.getEnergyInKiloJoules();
        }

        return totalEnergy;
    }

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    @Column(name = "name", nullable = true)
    public String getName() {
        return name;
    }

    /**
     * @return the ingredients
     */
    @OneToMany
    @JoinTable(name = "Recipe_RecipeIngredients",
            joinColumns = {
                @JoinColumn(name = "RecipeId")},
            inverseJoinColumns = {
                @JoinColumn(name = "RecipeIngredientId")}
    )
    public Set<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    /**
     * @return the steps
     */
    @Transient
    public Set<RecipeStep> getSteps() {
        return steps;
    }

    /**
     * @return the sidedishRecipes
     */
    @Transient
    public Set<Recipe> getSidedishRecipes() {
        return sidedishRecipes;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param ingredients the ingredients to set
     */
    public void setIngredients(Set<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * @param steps the steps to set
     */
    public void setSteps(Set<RecipeStep> steps) {
        this.steps = steps;
    }

    /**
     * @param sidedishRecipes the sidedishRecipes to set
     */
    public void setSidedishRecipes(Set<Recipe> sidedishRecipes) {
        this.sidedishRecipes = sidedishRecipes;
    }
}
