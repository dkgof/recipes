/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.data;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.db.DomainObject;
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
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author gof
 * @param <T>
 */
public class LazyCustomList<T extends DomainObject> extends LazyDataModel<T> {

    private String customFilter;
    private Map<String, Object> filterMap;
    private final Class<T> domainClass;
    
    public LazyCustomList(Class<T> domainClass) {
        this.domainClass = domainClass;
    }
    
    @SuppressWarnings("Duplicates")
    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, FilterMeta> filters) {
        
        List<T> results = new ArrayList<>();
        
        filterMap = new HashMap<>();
        
        if(filters != null) {
            filters.forEach((key, value)->{
                filterMap.put(value.getFilterField(), value.getFilterValue());
            });
        }
        
        if(customFilter != null) {
            filterMap.put("name", customFilter);
        }
        
        System.out.println(""+filterMap);
        
        Database.execute((session) -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Long> criteriaCount = builder.createQuery(Long.class);
            Root<T> rootCount = criteriaCount.from(domainClass);

            Predicate[] predicatesCount = new Predicate[filterMap.size()];

            //Setup filters
            int i = 0;
            for(Entry<String, Object> entry : filterMap.entrySet()) {
                String filterField = entry.getKey();
                String filterValue = entry.getValue().toString();
                
                predicatesCount[i] = builder.like(builder.lower(rootCount.get(filterField)), "%"+filterValue.toLowerCase()+"%");
                i++;
            }
            
            criteriaCount.select(builder.count(rootCount));
            criteriaCount.where(predicatesCount);
            
            int count = session.createQuery(criteriaCount).uniqueResult().intValue();
            
            this.setRowCount(count);
            
            CriteriaQuery<T> criteria = builder.createQuery(domainClass);
            Root<T> root = criteria.from(domainClass);

            Predicate[] predicates = new Predicate[filterMap.size()];

            //Setup filters
            i = 0;
            for(Entry<String, Object> entry : filterMap.entrySet()) {
                String filterField = entry.getKey();
                String filterValue = entry.getValue().toString();

                predicates[i] = builder.like(builder.lower(root.get(filterField)), "%"+filterValue.toLowerCase()+"%");
                i++;
            }
            
            criteria.select(root);
            criteria.where(predicates);
            
            String sortFieldFixed = sortField==null?"name":sortField;
            SortOrder sortOrderFixed = sortOrder==null?SortOrder.ASCENDING:sortOrder;
            
            switch(sortOrderFixed) {
                case ASCENDING:
                    criteria.orderBy(builder.asc(root.get(sortFieldFixed)));
                    break;
                case DESCENDING:
                    criteria.orderBy(builder.desc(root.get(sortFieldFixed)));
                    break;
            }
            
            Query<T> query = session.createQuery(criteria);
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