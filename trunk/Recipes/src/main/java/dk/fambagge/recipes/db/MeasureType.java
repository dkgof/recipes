/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.fambagge.recipes.db;

import dk.fambagge.recipes.domain.Measure;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

/**
 *
 * @author Gof
 */
public class MeasureType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[] {
            StringType.INSTANCE.sqlType()
        };
    }

    @Override
    public Class returnedClass() {
        return Measure.class;
    }

    @Override
    public boolean equals(Object obj1, Object obj2) throws HibernateException {
        return obj1.toString().equals(obj2.toString());
    }

    @Override
    public int hashCode(Object obj) throws HibernateException {
        return obj.toString().hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor si, Object owner) throws HibernateException, SQLException {
        assert names.length == 1;
        String stringValue = (String) StringType.INSTANCE.get(rs, names[0], si);
        
        return getTypeFromString(stringValue);
    }

    @Override
    public void nullSafeSet(PreparedStatement stm, Object value, int index, SessionImplementor si) throws HibernateException, SQLException {
        if(value == null) {
            StringType.INSTANCE.set(stm, null, index, si);
        } else {
            String stringValue = value.toString();
            StringType.INSTANCE.set(stm, stringValue, index, si);
        }
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
        return value.toString();
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        
        if(!(cached instanceof String)) {
            throw new UnsupportedOperationException("Cannot assemble from: "+cached.getClass());
        }
        
        String stringValue = cached.toString();
        
        return getTypeFromString(stringValue);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public static Measure getTypeFromString(String stringValue) {
        try {
            return Measure.Volume.valueOf(stringValue);
        } catch(IllegalArgumentException e) {
            return Measure.Weight.valueOf(stringValue);
        }
    }
}
