/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.data;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Recipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.query.Query;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author gof
 */
public class LazyRecipeList extends LazyDataModel<Recipe> {

    private String filter = "";
    
    @Override
    public List<Recipe> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        
        List<Recipe> results = new ArrayList<>();
        
        System.out.println("Filter: "+filter);
        
        Database.execute((session) -> {
            long count = (Long) session.createQuery("select count(*) from Recipe where name like '%"+filter+"%'").uniqueResult();
            this.setRowCount((int)count);
            
            Query query = session.createQuery("from Recipe where name like '%"+filter+"%' order by name");
            query.setFirstResult(first);
            query.setMaxResults(pageSize);
            query.setCacheable(true);

            results.addAll((List<Recipe>) query.getResultList());
        });
        
        return results;
    }

    /**
     * @return the filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }
}
