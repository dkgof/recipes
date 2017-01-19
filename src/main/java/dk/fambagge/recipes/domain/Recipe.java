/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.db.DomainObject;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Gof
 */
@Entity
@Table(name = "recipes")
public class Recipe implements Serializable, DomainObject {

    private int id;

    private String name;

    private int servings;

    private double energyInKiloJoule;
    
    private Set<RecipeIngredient> ingredients;

    private Set<RecipeStep> steps;
    
    private String imgUrl;

    public Recipe() {
        name = "";
        imgUrl = null;
        servings = 0;
        energyInKiloJoule = 0;
        ingredients = new HashSet<>();
        steps = new HashSet<>();
    }

    public Recipe(String name, int servings) {
        this();

        this.name = name;
        this.servings = servings;
    }

    public void addIngredient(RecipeIngredient ingredient) {
        getIngredients().add(ingredient);
        recalculateEnergyInKiloJoule();
    }

    public void addStep(RecipeStep step) {
        steps.add(step);
    }

    @Transient
    public double getEnergyInCalories() {
        return  Math.round(getEnergyInKiloJoule() * Constants.KCAL_PER_KILOJOULE * 10.0) / 10.0;
    }

    public void recalculateEnergyInKiloJoule() {
        double totalEnergy = 0;

        for (RecipeIngredient ingredient : getIngredients()) {
            totalEnergy += ingredient.getEnergyInKiloJoules();
        }

        setEnergyInKiloJoule(Math.round(totalEnergy * 10.0) / 10.0);
    }
    
    @Column(name = "energyInKiloJoule", nullable = false)
    public double getEnergyInKiloJoule() {
        return energyInKiloJoule;
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
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    /**
     * @return the ingredients
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "recipe_recipeingredients",
            joinColumns = {
                @JoinColumn(name = "recipeId")},
            inverseJoinColumns = {
                @JoinColumn(name = "recipeIngredientId")}
    )
    public Set<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    /**
     * @return the steps
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "recipe_recipesteps",
            joinColumns = {
                @JoinColumn(name = "recipeId")},
            inverseJoinColumns = {
                @JoinColumn(name = "recipeStepId")}
    )
    public Set<RecipeStep> getSteps() {
        return steps;
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
     * @return the servings
     */
    @Column(name = "servings", nullable = false)
    public int getServings() {
        return servings;
    }

    /**
     * @param servings the servings to set
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    public static Set<Recipe> getAll() {
        return Database.getAll(Recipe.class);
    }

    public static Recipe get(int id) {
        return Database.get("from "+Recipe.class.getName()+" where id='"+id+"'", Recipe.class);
    }
    
    @Transient
    public int getNextStepSortOrder() {
        int nextSortOrder = 0;
        for (RecipeStep step : steps) {
            nextSortOrder = Math.max(nextSortOrder, step.getSortOrder());
        }
        return nextSortOrder + 1;
    }

    /**
     * @return the imgUrl
     */
    @Column(name = "imgUrl", nullable = true)
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl the imgUrl to set
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Transient
    public int getCaloriesPerServing() {
        return (int) Math.round(getEnergyInCalories() / getServings());
    }

    public void removeIngredient(RecipeIngredient ingredientToDelete) {
        Iterator<RecipeIngredient> it = ingredients.iterator();
        while(it.hasNext()) {
            RecipeIngredient currentIngredient = it.next();
            
            if(currentIngredient.getId() == ingredientToDelete.getId()) {
                it.remove();
                break;
            }
        }
        
        recalculateEnergyInKiloJoule();
    }

    /**
     * @param energyInKiloJoule the energyInKiloJoule to set
     */
    public void setEnergyInKiloJoule(double energyInKiloJoule) {
        this.energyInKiloJoule = energyInKiloJoule;
    }
}
