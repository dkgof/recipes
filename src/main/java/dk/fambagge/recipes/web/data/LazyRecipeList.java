/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.data;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Recipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.query.Query;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author gof
 */
public class LazyRecipeList extends LazyDataModel<Recipe> {

    private String customFilter;
    private Map<String, Object> filterMap;
    
    @Override
    public List<Recipe> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        
        List<Recipe> results = new ArrayList<>();
        
        filterMap = new HashMap<String, Object>();
        
        if(filters != null) {
            filterMap.putAll(filters);
        }
        
        if(customFilter != null) {
            filterMap.put("name", customFilter);
        }
        
        Database.execute((session) -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Long> criteriaCount = builder.createQuery(Long.class);
            Root<Recipe> recipeRootCount = criteriaCount.from(Recipe.class);

            Predicate[] predicatesCount = new Predicate[filterMap.size()];

            //Setup filters
            int i = 0;
            for(Entry<String, Object> entry : filterMap.entrySet()) {
                String filterField = entry.getKey();
                String filterValue = entry.getValue().toString();

                predicatesCount[i] = builder.like(builder.lower(recipeRootCount.get(filterField)), "%"+filterValue.toLowerCase()+"%");
                i++;
            }
            
            criteriaCount.select(builder.count(recipeRootCount));
            criteriaCount.where(predicatesCount);
            
            int count = session.createQuery(criteriaCount).uniqueResult().intValue();
            
            this.setRowCount(count);
            
            CriteriaQuery<Recipe> criteria = builder.createQuery(Recipe.class);
            Root<Recipe> recipeRoot = criteria.from(Recipe.class);

            Predicate[] predicates = new Predicate[filterMap.size()];

            //Setup filters
            i = 0;
            for(Entry<String, Object> entry : filterMap.entrySet()) {
                String filterField = entry.getKey();
                String filterValue = entry.getValue().toString();

                predicates[i] = builder.like(builder.lower(recipeRoot.get(filterField)), "%"+filterValue.toLowerCase()+"%");
                i++;
            }
            
            criteria.select(recipeRoot);
            criteria.where(predicates);
            
            String sortFieldFixed = sortField==null?"name":sortField;
            SortOrder sortOrderFixed = sortOrder==null?SortOrder.ASCENDING:sortOrder;
            
            switch(sortOrderFixed) {
                case ASCENDING:
                    criteria.orderBy(builder.asc(recipeRoot.get(sortFieldFixed)));
                    break;
                case DESCENDING:
                    criteria.orderBy(builder.desc(recipeRoot.get(sortFieldFixed)));
                    break;
            }
            
            Query<Recipe> query = session.createQuery(criteria);
            query.setFirstResult(first);
            query.setMaxResults(pageSize);
                    
            results.addAll(query.getResultList());
        });
        
        return results;
    }

    /**
     * @return the filter
     */
    public String getFilter() {
        return customFilter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(String filter) {
        this.customFilter = filter;
    }
}