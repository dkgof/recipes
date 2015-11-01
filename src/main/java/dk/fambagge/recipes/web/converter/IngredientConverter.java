/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.converter;

import dk.fambagge.recipes.domain.Ingredient;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Gof
 */
@FacesConverter("dk.fambagge.recipes.web.converter.IngredientConverter")
public class IngredientConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        int id = Integer.parseInt(value);
        
        return Ingredient.get(id);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ""+((Ingredient) value).getId();
    }
    
}
