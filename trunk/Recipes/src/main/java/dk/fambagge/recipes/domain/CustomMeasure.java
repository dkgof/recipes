/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Type;

/**
 *
 * @author Gof
 */
@Entity
@Table( name = "CustomMeasures" )
public class CustomMeasure implements Serializable {

    private int id;
    private String name;
    private String symbol;
    private double referenceToCustomRatio;
    private Measure referenceMeasure;

    public CustomMeasure() {
        id = -1;
    }

    public CustomMeasure(String name, String symbol, double customToReferenceRatio, Measure referenceMeasure) {
        this.name = name;
        this.symbol = symbol;
        this.referenceToCustomRatio = customToReferenceRatio;
        this.referenceMeasure = referenceMeasure;

    }

    /**
     * @return the id
     */
    @Id
    @Column( name = "id" )
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
     * @return the name
     */
    @Column( name = "name", nullable = false, length = 32, unique = true )
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
     * @return the symbol
     */
    @Column( name = "symbol", nullable = false, length = 16, unique = true )
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the referenceToCustomRatio
     */
    @Column( name = "referenceToCustomRatio", nullable = false )
    public double getReferenceToCustomRatio() {
        return referenceToCustomRatio;
    }

    /**
     * @param referenceToCustomRatio the customToReferenceRatio to set
     */
    public void setReferenceToCustomRatio(double referenceToCustomRatio) {
        this.referenceToCustomRatio = referenceToCustomRatio;
    }

    /**
     * @return the referenceMeasure
     */
    @Type( type = "dk.fambagge.recipes.db.MeasureType" )
    @Column(name = "measure", nullable = false, length = 16)
    public Measure getReferenceMeasure() {
        return referenceMeasure;
    }

    /**
     * @param referenceMeasure the referenceMeasure to set
     */
    public void setReferenceMeasure(Measure referenceMeasure) {
        this.referenceMeasure = referenceMeasure;
    }
}
