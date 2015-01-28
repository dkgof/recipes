/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.domain.Ingredient;
import dk.fambagge.recipes.domain.Measure;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Gof
 */
@ManagedBean
@ViewScoped
public class IngredientsView implements Serializable {
    private List<Ingredient> ingredients;
    
    @PostConstruct
    public void init() {
        this.ingredients = Ingredient.getAll();
    }
    
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    
    public List<Measure> getMeasures() {
        List<Measure> measures = new LinkedList<>();
        
        measures.addAll(Arrays.asList(Measure.Weight.values()));
        measures.addAll(Arrays.asList(Measure.Volume.values()));
        
        return measures;
    }
    
    public void onRowEdit(RowEditEvent event) {
        if(event.getObject() instanceof Ingredient) {
            Ingredient ingredient = (Ingredient) event.getObject();
            ingredient.update();
        }
    }

    public void onRowCancel(RowEditEvent event) {
    }
}
