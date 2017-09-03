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
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SortNatural;

/**
 *
 * @author Gof
 */
@Entity
@Table(name = "recipes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Recipe implements Serializable, DomainObject {

    private int id;

    private String name;

    private int servings;

    private Set<RecipeIngredient> ingredients;

    private Set<RecipeStep> steps;

    private SortedSet<Media> medias;
    
    private Set<RecipeIngredientGroup> ingredientGroups;
    
    public Recipe() {
        name = "";
        servings = 0;
        ingredients = new HashSet<>();
        steps = new HashSet<>();
        medias = new TreeSet<>();
    }

    public Recipe(String name, int servings) {
        this();

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
        return  Math.round(getEnergyInKiloJoule() * Constants.KCAL_PER_KILOJOULE * 10.0) / 10.0;
    }

    @Transient
    public double getEnergyInKiloJoule() {
        double totalEnergy = 0;

        for (RecipeIngredient ingredient : getIngredients()) {
            totalEnergy += ingredient.getEnergyInKiloJoules();
        }

        return Math.round(totalEnergy * 10.0) / 10.0;
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

    public boolean hasImage() {
        return !medias.isEmpty();
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
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
        return Database.get("id='"+id+"'", Recipe.class);
    }
    
    @Transient
    public int getNextStepSortOrder() {
        int nextSortOrder = 0;
        for (RecipeStep step : steps) {
            nextSortOrder = Math.max(nextSortOrder, step.getSortOrder());
        }
        return nextSortOrder + 1;
    }

    public void addMedia(Media m) {
        medias.add(m);
    }
    
    public void removeMedia(Media m) {
        int oldOrder = m.getSortOrder();
        
        medias.remove(m);
        m.delete();
        
        for(Media media : medias) {
            if(media.getSortOrder() > oldOrder) {
                media.setSortOrder(media.getSortOrder()-1);
            }
        }
        
        Database.saveOrUpdate(this);
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
    }

    /**
     * @return the medias
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "recipe_medias",
            joinColumns = {
                @JoinColumn(name = "recipeId")},
            inverseJoinColumns = {
                @JoinColumn(name = "mediaId")}
    )
    @OrderBy("sortOrder ASC")
    @SortNatural
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public SortedSet<Media> getMedias() {
        return medias;
    }

    /**
     * @param medias the medias to set
     */
    public void setMedias(SortedSet<Media> medias) {
        this.medias = medias;
    }
    
    @Transient
    public Media getPrimaryMedia() {
        return medias.first();
    }

    /**
     * @return the ingredientGroups
     */
    @OneToMany
    public Set<RecipeIngredientGroup> getIngredientGroups() {
        return ingredientGroups;
    }

    /**
     * @param ingredientGroups the ingredientGroups to set
     */
    public void setIngredientGroups(Set<RecipeIngredientGroup> ingredientGroups) {
        this.ingredientGroups = ingredientGroups;
    }
}
