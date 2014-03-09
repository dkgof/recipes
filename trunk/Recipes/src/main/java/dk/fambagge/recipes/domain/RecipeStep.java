/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.fambagge.recipes.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Gof
 */
@Entity
@Table( name = "RecipeSteps" )
public class RecipeStep implements Serializable {
    private int id;
    private String description;

    public RecipeStep() {
        id = -1;
        this.description = "";
    }
    
    public RecipeStep(String description) {
        id = -1;
        this.description = description;
    }
    
    /**
     * @return the description
     */
    @Column( name = "description", nullable = false)
    public String getDescription() {
        return description;
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
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
