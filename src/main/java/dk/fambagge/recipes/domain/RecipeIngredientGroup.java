/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.db.DomainObject;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Rolf Bagge
 */
@Entity
@Table(name = "recipeingredientgroups")
public class RecipeIngredientGroup implements DomainObject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Set<RecipeIngredient> ingredients;

    public RecipeIngredientGroup() {
        ingredients = new HashSet<>();
    }
    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
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
     * @return the ingredients
     */
    public Set<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    /**
     * @param ingredients the ingredients to set
     */
    public void setIngredients(Set<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
    
    public List<RecipeIngredient> getIngredientsSorted() {
        List<RecipeIngredient> sortedIngredients = new LinkedList<>(getIngredients());
        
        Collections.sort(sortedIngredients, (RecipeIngredient ingredient1, RecipeIngredient ingredient2) -> {
            return ingredient1.getIngredient().getName().compareTo(ingredient2.getIngredient().getName());
        });
        
        return sortedIngredients;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RecipeIngredientGroup) {
            RecipeIngredientGroup group = (RecipeIngredientGroup) obj;
            
            return this.id == group.id;
        }
        
        return false;
    }
}
