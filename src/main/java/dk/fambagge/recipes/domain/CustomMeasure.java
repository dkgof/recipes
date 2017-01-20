/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.db.DomainObject;
import java.util.Set;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 *
 * @author Gof
 */
@Entity
@Table( name = "custommeasures" )
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomMeasure implements Measure, DomainObject {

    private int id;
    private String name;
    private double referenceToCustomRatio;
    private Measure referenceMeasure;

    public CustomMeasure() {
    }

    public CustomMeasure(String name, double customToReferenceRatio, Measure referenceMeasure) {
        this();
        
        this.name = name;
        this.referenceToCustomRatio = customToReferenceRatio;
        this.referenceMeasure = referenceMeasure;
    }

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column( name = "name", nullable = false, length = 32 )
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
    @Transient
    public String getSymbol() {
        return name;
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

    @Override
    @Transient
    public double getFactor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static CustomMeasure getFromId(int id) {
        return Database.get("from "+CustomMeasure.class.getName()+" where id='"+id+"'", CustomMeasure.class);
    }

    public static Set<CustomMeasure> getAll() {
        return Database.getAll(CustomMeasure.class);
    }

    @Override
    public String toDBString() {
        return "custom"+this.getId();
    }

    @Override
    public String toString() {
        return "custom"+this.getId();
    }

    @Override
    public double convertTo(double amount, Measure targetMeasure) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomMeasure other = (CustomMeasure) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public String toDescriptionString() {
        return name+" ("+referenceToCustomRatio+" "+referenceMeasure.getSymbol()+")";
    }
}
