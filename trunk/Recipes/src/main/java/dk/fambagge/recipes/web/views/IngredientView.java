/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.domain.Ingredient;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gof
 */
@ManagedBean(name="dtIngredientView")
@ViewScoped
public class IngredientView implements Serializable {
    private List<Ingredient> ingredients;
    
    @PostConstruct
    public void init() {
        this.ingredients = Ingredient.getAll();
    }
    
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
