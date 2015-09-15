/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.db.Database;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.Session;

/**
 *
 * @author Gof
 */
@Entity
@Table(name = "Recipes")
public class Recipe implements Serializable {

    private int id;

    private String name;

    private int servings;
    
    private Set<RecipeIngredient> ingredients;

    private Set<RecipeStep> steps;

    public Recipe() {
        id = -1;
        name = "";
        servings = 0;
        ingredients = new HashSet<>();
        steps = new HashSet<>();
    }

    public Recipe(String name, int servings) {
        this.name = name;
        this.servings = servings;
    }
    
    public void addIngredient(RecipeIngredient ingredient) {
        getIngredients().add(ingredient);
    }

    public void addStep(RecipeStep step) {
        steps.add(step);
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
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    /**
     * @return the ingredients
     */
    @OneToMany( cascade = CascadeType.ALL )
    @JoinTable(name = "Recipe_RecipeIngredients",
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
    @OneToMany( cascade = CascadeType.ALL )
    @JoinTable(name = "Recipe_RecipeSteps",
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
    @Column( name = "servings", nullable = false )
    public int getServings() {
        return servings;
    }

    /**
     * @param servings the servings to set
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    public static List<Recipe> getAll() {
        final Session session = Database.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        final List result = session.createQuery("from Recipe").list();
        session.getTransaction().commit();
        final List<Recipe> namedResult = new LinkedList<>();
        for (final Object resultObj : result) {
            namedResult.add((Recipe) resultObj);
        }
        return namedResult;
    }
}
