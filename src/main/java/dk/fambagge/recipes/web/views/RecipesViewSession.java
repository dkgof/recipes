/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import java.io.Serializable;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.data.PageEvent;

/**
 *
 * @author Gof
 */
@Named
@SessionScoped
public class RecipesViewSession implements Serializable {

    private int page = 0;
    private int rowPerPage = 8;
    private String filterString = "";

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return the rowPerPage
     */
    public int getRowPerPage() {
        return rowPerPage;
    }

    /**
     * @param rowPerPage the rowPerPage to set
     */
    public void setRowPerPage(int rowPerPage) {
        this.rowPerPage = rowPerPage;
    }

    /**
     * @return the filterString
     */
    public String getFilterString() {
        return filterString;
    }

    /**
     * @param filterString the filterString to set
     */
    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    public void saveRowsPerPage() {
        Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        rowPerPage = Integer.parseInt(paramMap.get("rows"));
    }
    
    public int getFirst() {
        return this.page * this.rowPerPage;
    }
    
    public void onPage(PageEvent evt) {
        this.page = evt.getPage();
    }
}
