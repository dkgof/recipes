/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Constants;
import dk.fambagge.recipes.domain.Measure;
import dk.fambagge.recipes.domain.Media;
import dk.fambagge.recipes.domain.Recipe;
import dk.fambagge.recipes.domain.RecipeIngredient;
import dk.fambagge.recipes.domain.RecipeIngredientGroup;
import dk.fambagge.recipes.domain.RecipeStep;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Gof
 */
@ManagedBean
@ViewScoped
public class RecipeView  implements Serializable {
    private Recipe selectedRecipe = null;
    
    private int customServings;
    private boolean showInGram;
    
    private String groupName;
    
    /**
     * @return the selectedRecipe
     */
    public Recipe getSelectedRecipe() {
        int recipeId = -1;
        try {
            recipeId = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("recipe"));
        } catch(Exception e) {
            //Ignore
        }
        
        if(selectedRecipe == null) {
            if(recipeId != -1) {
                //We didnt have any recipe
                selectedRecipe = Recipe.get(recipeId);
                setCustomServings(selectedRecipe.getServings());
                setShowInGram(false);
            }
        } else {
            if(recipeId != -1 && selectedRecipe.getId() != recipeId) {
                //We had a different recipe, update to new
                selectedRecipe = Recipe.get(recipeId);
                setCustomServings(selectedRecipe.getServings());
                setShowInGram(false);
            }
        }
        
        return selectedRecipe;
    }

    public List<RecipeStep> getStepsSorted() {
        List<RecipeStep> sortedSteps = new LinkedList<>();
        
        Set<RecipeStep> steps = getSelectedRecipe().getSteps();
        
        sortedSteps.addAll(steps);
        
        Collections.sort(sortedSteps, (RecipeStep step1, RecipeStep step2) -> Integer.compare(step1.getSortOrder(), step2.getSortOrder()));
        
        return sortedSteps;
    }
    
    public List<RecipeIngredient> getIngredientsSorted() {
        List<RecipeIngredient> sortedIngredients = new LinkedList<>();
        
        for(RecipeIngredient ingredient : getSelectedRecipe().getIngredients()) {
            if(ingredient.getGroup() == null) {
                sortedIngredients.add(ingredient);
            }
        }
        
        Collections.sort(sortedIngredients, (RecipeIngredient ingredient1, RecipeIngredient ingredient2) -> {
            return ingredient1.getIngredient().getName().compareTo(ingredient2.getIngredient().getName());
        });
        
        return sortedIngredients;
    }

    public List<RecipeIngredientGroup> getIngredientGroupsSorted() {
        List<RecipeIngredientGroup> sortedGroups = new LinkedList<>(getSelectedRecipe().getIngredientGroups());
        
        Collections.sort(sortedGroups, (RecipeIngredientGroup group1, RecipeIngredientGroup group2) -> {
            return group1.getName().compareTo(group2.getName());
        });

        return sortedGroups;
    }
    
    public void addStep() {
        RecipeStep step = new RecipeStep();
        getSelectedRecipe().addStep(step);
        
        step.setSortOrder(getSelectedRecipe().getNextStepSortOrder());
        
        Database.saveOrUpdate(getSelectedRecipe());
    }
    
    private double getCaloriesPerServingDouble(RecipeIngredient ingredient) {
        double calories = ingredient.getEnergyInKiloJoules() * Constants.KCAL_PER_KILOJOULE;
        
        calories /= getSelectedRecipe().getServings();
        
        return calories;
    }

    public int getCaloriesPerServing(RecipeIngredient ingredient) {
        return (int) Math.round(getCaloriesPerServingDouble(ingredient));
    }
    
    public int getCaloriesPerServingForGroup(RecipeIngredientGroup group) {
        double calories = 0;
        
        for(RecipeIngredient ingredient : group.getIngredients()) {
            calories += getCaloriesPerServingDouble(ingredient);
        }
        
        return (int) Math.round(calories);
    }
    
    public double getScaledIngredientAmount(RecipeIngredient ingredient) {
        double amount = ingredient.getAmount();
        
        if(showInGram) {
            amount = ingredient.getAmount(Measure.Weight.GRAM);
        }
        
        return amount * (getCustomServings() / (double)getSelectedRecipe().getServings());
    }
    
    public String getMeasureSymbol(RecipeIngredient ingredient) {
        if(showInGram) {
            return Measure.Weight.GRAM.getSymbol();
        } else {
            return ingredient.getMeasure().getSymbol();
        }
    }
    
    public String escapeTextWithLineBreaks(String s) {
        String escaped = StringEscapeUtils.escapeHtml4(s);
        
        return escaped.replace("\n", "<br />");
    }
    
    public void deleteIngredient(RecipeIngredient ingredient) {
        getSelectedRecipe().removeIngredient(ingredient);
        saveRecipe();
    }
    
    public void inlineIngredientSave(RecipeIngredient ingredient) {
        Database.saveOrUpdate(ingredient);
    }
    
    public void inlineSaveRecipeStep(RecipeStep step) {
        Database.saveOrUpdate(step);
    }
    
    public void saveRecipe() {
        Database.saveOrUpdate(getSelectedRecipe());
    }

    /**
     * @return the customServings
     */
    public int getCustomServings() {
        return customServings;
    }

    /**
     * @param customServings the customServings to set
     */
    public void setCustomServings(int customServings) {
        this.customServings = customServings;
    }
    
    public void cloneRecipe() {
        Recipe clonedRecipe = new Recipe(getSelectedRecipe().getName()+" copy", getSelectedRecipe().getServings());
        
        for(RecipeStep step : getSelectedRecipe().getSteps()) {
            RecipeStep clonedStep = new RecipeStep(step.getDescription());
            clonedStep.setSortOrder(step.getSortOrder());
            
            clonedRecipe.addStep(clonedStep);
            Database.saveOrUpdate(clonedStep);
        }
        
        Map<Long, RecipeIngredientGroup> groupMap = new HashMap<>();
        
        for(RecipeIngredient ingredient : getSelectedRecipe().getIngredients()) {
            RecipeIngredient clonedIngredient = new RecipeIngredient(ingredient.getIngredient(), ingredient.getAmount(), ingredient.getMeasure());
            
            RecipeIngredientGroup group = ingredient.getGroup();
            
            if(group != null) {
                RecipeIngredientGroup clonedGroup = groupMap.get(group.getId());
                
                if(clonedGroup == null) {
                    clonedGroup = new RecipeIngredientGroup();
                    clonedGroup.setName(group.getName());
                    Database.saveOrUpdate(clonedGroup);
                    groupMap.put(group.getId(), clonedGroup);
                }
                
                clonedIngredient.setGroup(clonedGroup);
                clonedRecipe.addIngredientGroup(clonedGroup);
            }
            
            clonedRecipe.addIngredient(clonedIngredient);
            Database.saveOrUpdate(clonedIngredient);
        }
        
        Database.saveOrUpdate(clonedRecipe);
        
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("recipe.xhtml?recipe="+clonedRecipe.getId());
        } catch (IOException ex) {
            Logger.getLogger(RecipeView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String formatNumber(double value) {
        String formattedValue = String.format("%.1f", value);

        if(Math.floor(value) == value) {
            formattedValue = Integer.toString((int) value);
        }
        
        if(!isShowInGram()) {
            if(isCloseTo(value,0.2)) {
                formattedValue = "1/5";
            } else if(isCloseTo(value,0.25)) {
                formattedValue = "1/4";
            } else if(isCloseTo(value,0.4)) {
                formattedValue = "2/5";
            } else if(isCloseTo(value,0.5)) {
                formattedValue = "1/2";
            } else if(isCloseTo(value,0.6)) {
                formattedValue = "3/5";
            } else if(isCloseTo(value,0.75)) {
                formattedValue = "3/4";
            } else if(isCloseTo(value,0.8)) {
                formattedValue = "4/5";
            } else if(isCloseTo(value,0.66666666)) {
                formattedValue = "2/3";
            } else if(isCloseTo(value,0.33333333)) {
                formattedValue = "1/3";
            }
        }
        
        return formattedValue;
    }

    /**
     * @return the showInGram
     */
    public boolean isShowInGram() {
        return showInGram;
    }

    /**
     * @param showInGram the showInGram to set
     */
    public void setShowInGram(boolean showInGram) {
        this.showInGram = showInGram;
    }
    
    public void doDropIngredient() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        int ingredientId = Integer.parseInt(params.get("ingredient"));
        int groupId = Integer.parseInt(params.get("group"));

        RecipeIngredient ingredient = RecipeIngredient.fromId(ingredientId);
        RecipeIngredientGroup group = null;

        if(groupId != -1) {
            group = RecipeIngredientGroup.fromId(groupId);
            ingredient.setGroup(group);
        } else {
            group = ingredient.getGroup();
            ingredient.setGroup(null);
        }
        
        Database.saveOrUpdate(ingredient);
        
        Database.refresh(getSelectedRecipe());
        
        if(group != null) {
            Database.refresh(group);
        }
    }

    public void doDropStep() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        int fromId = Integer.parseInt(params.get("from"));
        int toId = Integer.parseInt(params.get("to"));
        
        RecipeStep fromStep = null;
        RecipeStep toStep = null;
        
        for(RecipeStep step : getSelectedRecipe().getSteps()) {
            if(step.getId() == fromId) {
                fromStep = step;
            } else  if(step.getId() == toId) {
                toStep = step;
            }
        }
        
        if(fromStep != null && toStep != null) {
            int toOrder = toStep.getSortOrder();
            int fromOrder = fromStep.getSortOrder();
            
            if(toOrder < fromOrder) {
                //Move from up to to, and move all other down 1
                
                for(RecipeStep step : getSelectedRecipe().getSteps()) {
                    if(step.getSortOrder() >= toOrder && step.getId() != fromStep.getId()) {
                        step.setSortOrder(step.getSortOrder()+1);
                        Database.saveOrUpdate(step);
                    }
                }
                fromStep.setSortOrder(toOrder);
                Database.saveOrUpdate(fromStep);
            } else {
                //Move from down to to-1, and move all between from and to up 1
                for(RecipeStep step : getSelectedRecipe().getSteps()) {
                    if(step.getSortOrder() <= toOrder && step.getSortOrder() > fromOrder) {
                        step.setSortOrder(step.getSortOrder()-1);
                        Database.saveOrUpdate(step);
                    }
                }
                fromStep.setSortOrder(toOrder);
                Database.saveOrUpdate(fromStep);
            }
        } else {
            System.out.println("Error dragging, from or to was null!");
        }
    }

    public List<Media> getMedias() {
        return new LinkedList<>(getSelectedRecipe().getMedias());
    }
    
    private boolean isCloseTo(double value, double target) {
        double someSmallAmount = 0.0001;
        
        boolean result = value > (target-someSmallAmount) && value < (target+someSmallAmount);
        
        return result;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public void addRecipeGroup() {
        RecipeIngredientGroup group = new RecipeIngredientGroup();
        group.setName(groupName);
        
        groupName = "";
        
        Recipe recipe = getSelectedRecipe();
        
        recipe.addIngredientGroup(group);
        
        Database.save(group);
        Database.saveOrUpdate(recipe);
        Database.refresh(recipe);
    }
    
    public void deleteRecipeGroup(RecipeIngredientGroup group) {
        Recipe recipe = getSelectedRecipe();
        recipe.removeRecipeGroup(group);
        
        for(RecipeIngredient ingredient : group.getIngredients()) {
            ingredient.setGroup(null);
            Database.saveOrUpdate(ingredient);
        }
        
        Database.delete(group);
        Database.saveOrUpdate(recipe);
        Database.refresh(recipe);
    }
}
