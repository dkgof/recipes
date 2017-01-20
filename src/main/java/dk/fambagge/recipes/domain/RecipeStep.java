/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.db.DomainObject;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author Gof
 */
@Entity
@Table( name = "recipesteps" )
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecipeStep implements Serializable, DomainObject {
    private int id;
    private String description;
    private int sortOrder;

    public RecipeStep() {
        this.description = "";
    }
    
    public RecipeStep(String description) {
        this();
        
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

    /**
     * @return the order
     */
    @Column( name = "sortOrder", nullable = false)
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * @param order the order to set
     */
    public void setSortOrder(int order) {
        this.sortOrder = order;
    }
}
