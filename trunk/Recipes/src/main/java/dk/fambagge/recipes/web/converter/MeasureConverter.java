/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.converter;

import dk.fambagge.recipes.domain.Measure;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Gof
 */
@FacesConverter("dk.fambagge.recipes.web.converter.MeasureConverter")
public class MeasureConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            Measure measure = Measure.Weight.valueOf(value);
            
            return measure;
        } catch(Exception e) {
            
        }
        try {
            Measure measure = Measure.Volume.valueOf(value);
            
            return measure;
        } catch(Exception e) {
            
        }
        
        throw new IllegalStateException("Cannot convert ["+value+"] to neither Weight nor Volume!");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
    
}
