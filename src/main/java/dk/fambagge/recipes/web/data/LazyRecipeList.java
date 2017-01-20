/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.data;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Recipe;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author gof
 */
public class LazyRecipeList extends LazyDataModel<Recipe> {

    private List<Recipe> datasource;

    @Override
    public Recipe getRowData(String rowKey) {
        System.out.println("Getting row: "+rowKey);
        
        int rowId = Integer.parseInt(rowKey);
        
        for(Recipe recipe : datasource) {
            if(recipe.getId() == rowId) {
                return recipe;
            }
        }
 
        return null;
    }
 
    @Override
    public Object getRowKey(Recipe recipe) {
        return recipe.getId();
    }
    
    @Override
    public List<Recipe> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if(datasource == null) {
            System.out.println("Lazy loading from "+first+" - "+pageSize);
            System.out.println("Filters: "+filters);
            System.out.println("SortField: "+sortField);
            System.out.println("SortOrder: "+sortOrder);

            datasource = new ArrayList<>(Database.queryAll("from " + Recipe.class.getName(), Recipe.class));
            this.setRowCount(datasource.size());
        }
        
        List<Recipe> results = new ArrayList<>(datasource.subList(first, first+pageSize));

        return results;
    }
}
