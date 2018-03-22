/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.converter;

import dk.fambagge.recipes.domain.CustomMeasure;
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

    @SuppressWarnings("EmptyCatchBlock")
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return Measure.Weight.valueOf(value);
        } catch(Exception e) {
            
        }
        try {
            return Measure.Volume.valueOf(value);
        } catch(Exception e) {
            
        }
        try {
            return Measure.Energy.valueOf(value);
        } catch(Exception e) {
            
        }

        try {
            if(value.startsWith("custom")) {
                return CustomMeasure.getFromId(Integer.parseInt(value.substring(6)));
            }
        } catch(Exception e) {
            
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
    
}
