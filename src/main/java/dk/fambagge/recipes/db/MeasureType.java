/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.db;

import dk.fambagge.recipes.domain.CustomMeasure;
import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Measure;
import dk.fambagge.recipes.domain.RecipeIngredient;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

/**
 *
 * @author Gof
 */
public class MeasureType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{
            StringType.INSTANCE.sqlType()
        };
    }

    @Override
    public Class returnedClass() {
        return Measure.class;
    }

    @Override
    public boolean equals(Object obj1, Object obj2) throws HibernateException {
        if (!(obj1 instanceof Measure) || !(obj2 instanceof Measure)) {
            throw new UnsupportedOperationException("Cannot equals on non Measure");
        }

        return ((Measure) obj1).toDBString().equals(((Measure) obj2).toDBString());
    }

    @Override
    public int hashCode(Object obj) throws HibernateException {
        return ((Measure) obj).toDBString().hashCode();
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        if (!(value instanceof Measure)) {
            throw new UnsupportedOperationException("Cannot disassemble Measure from: " + value.getClass());
        }

        return ((Measure) value).toDBString();
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {

        if (!(cached instanceof String)) {
            throw new UnsupportedOperationException("Cannot assemble Measure from: " + cached.getClass());
        }

        String stringValue = cached.toString();

        return getTypeFromString(stringValue);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public static Measure getTypeFromString(String stringValue) {
        Measure measure = null;

        if (stringValue.startsWith("custom")) {
            int id = Integer.parseInt(stringValue.substring(6));
            try {
                measure = CustomMeasure.getFromId(id);
            } catch (Exception e) {
                System.out.println("Error looking up custom measure: " + e);
                e.printStackTrace();
            }
        } else {
            try {
                measure = Measure.Volume.valueOf(stringValue);
            } catch (Exception e) {
                try {
                    measure = Measure.Weight.valueOf(stringValue);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return measure;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor si, Object o) throws HibernateException, SQLException {
        assert names.length == 1;
        String stringValue = (String) StringType.INSTANCE.get(rs, names[0], si);

        return getTypeFromString(stringValue);
    }

    @Override
    public void nullSafeSet(PreparedStatement stm, Object value, int index, SharedSessionContractImplementor si) throws HibernateException, SQLException {
        if (value == null) {
            StringType.INSTANCE.set(stm, null, index, si);
        } else {
            if (!(value instanceof Measure)) {
                throw new UnsupportedOperationException("Cannot nullSafeSet Measure from: " + value.getClass());
            }

            String stringValue = ((Measure) value).toDBString();
            StringType.INSTANCE.set(stm, stringValue, index, si);
        }
    }

    public static class LazyCustomMeasure extends CustomMeasure {

        private final RecipeIngredient owner;

        private CustomMeasure wrappedMeasure;

        public LazyCustomMeasure(RecipeIngredient owner) {
            this.owner = owner;
            wrappedMeasure = null;
        }

        private void loadMeasure() {
            if (wrappedMeasure == null) {
                wrappedMeasure = owner.getIngredient().getCustomMeasure();
            }
        }

        @Override
        public double getReferenceToCustomRatio() {
            loadMeasure();
            return wrappedMeasure.getReferenceToCustomRatio(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Measure getReferenceMeasure() {
            loadMeasure();
            return wrappedMeasure.getReferenceMeasure(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getSymbol() {
            loadMeasure();
            return wrappedMeasure.getSymbol();
        }

        @Override
        public double getFactor() {
            loadMeasure();
            return wrappedMeasure.getFactor();
        }

        @Override
        public String toDBString() {
            loadMeasure();
            return wrappedMeasure.toDBString();
        }

        @Override
        public double convertTo(double amount, Measure targetMeasure) {
            loadMeasure();
            return wrappedMeasure.convertTo(amount, targetMeasure);
        }
    }
}
