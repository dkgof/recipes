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
import org.apache.commons.text.StringEscapeUtils;

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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;

/**
 *
 * @author Gof
 */
@Named
@ViewScoped
public class RecipeView  implements Serializable {
    private Recipe selectedRecipe = null;
    
    private int customServings;
    private boolean showInGram;
    
    private String groupName;
    
    @Inject @Param(name="recipe")
    private int recipeId;
    
    private Map<RecipeIngredientGroup, Set<RecipeIngredient>> groupIngredientCache = new HashMap<>();
    
    /**
     * @return the selectedRecipe
     */
    public Recipe getSelectedRecipe() {
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
    
    public Set<RecipeIngredient> getIngredientGroupIngredients(RecipeIngredientGroup group) {
        if(!groupIngredientCache.containsKey(group)) {
            groupIngredientCache.put(group, RecipeIngredient.getAllInGroup(group));
        }
        
        return groupIngredientCache.get(group);
    }
    

    public List<RecipeIngredient> getIngredientGroupIngredientsSorted(RecipeIngredientGroup group) {
        List<RecipeIngredient> sortedIngredients = new LinkedList<>(getIngredientGroupIngredients(group));
        
        Collections.sort(sortedIngredients, (RecipeIngredient ingredient1, RecipeIngredient ingredient2) -> {
            return ingredient1.getIngredient().getName().compareTo(ingredient2.getIngredient().getName());
        });
        
        return sortedIngredients;
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
        
        for(RecipeIngredient ingredient : getIngredientGroupIngredients(group)) {
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
    
    public String getScaledIngredientAmountFormatted(RecipeIngredient ingredient) {
        double value = getScaledIngredientAmount(ingredient);
        
        String formattedValue = String.format("%.1f", value);

        if(Math.floor(value) == value) {
            formattedValue = Integer.toString((int) value);
        }
        
        boolean doFraction = true;
        
        if(ingredient.getMeasure() == Measure.Weight.GRAM
            || ingredient.getMeasure() == Measure.Weight.KILOGRAM
            || ingredient.getMeasure() == Measure.Weight.MILLIGRAM
            || isShowInGram()
        ) {
            doFraction = false;
        }
        
        if(doFraction) {
            if(isCloseTo(value,0.25)) {
                formattedValue = "1/4";
            } else if(isCloseTo(value,0.5)) {
                formattedValue = "1/2";
            } else if(isCloseTo(value,0.2)) {
                formattedValue = "1/5";
            } else if(isCloseTo(value,0.75)) {
                formattedValue = "3/4";
            } else if(isCloseTo(value,0.66666666)) {
                formattedValue = "2/3";
            } else if(isCloseTo(value,0.33333333)) {
                formattedValue = "1/3";
            } else if(isCloseTo(value,0.16666666)) {
                formattedValue = "1/6";
            } else if(isCloseTo(value,0.125)) {
                formattedValue = "1/8";
            }
        }
        
        return formattedValue;
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
        RecipeIngredientGroup group = ingredient.getGroup();
        getSelectedRecipe().removeIngredient(ingredient);
        Database.delete(ingredient);
        
        if(group != null && groupIngredientCache.containsKey(group)) {
            groupIngredientCache.get(group).remove(ingredient);
        }
        
        saveRecipe();
    }
    
    public void inlineIngredientSave(RecipeIngredient ingredient) {
        System.out.println("Saving ingredient: "+ingredient+" - "+ingredient.getAmount());
        Database.saveOrUpdate(ingredient);
    }
    
    public void inlineSaveRecipeStep(RecipeStep step) {
        if(step.getDescription().trim().length() == 0) {
            Recipe recipe = getSelectedRecipe();
            recipe.removeStep(step);
            Database.delete(step);
            Database.saveOrUpdate(recipe);
            Database.refresh(recipe);
        } else {
            //Update recipe step
            Database.saveOrUpdate(step);
        }
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
            RecipeIngredientGroup oldGroup = ingredient.getGroup();
            
            group = RecipeIngredientGroup.fromId(groupId);
            ingredient.setGroup(group);
            groupIngredientCache.remove(group);
            
            if(oldGroup != null && groupIngredientCache.containsKey(oldGroup)) {
            groupIngredientCache.remove(oldGroup);
            }
            
        } else {
            group = ingredient.getGroup();
            ingredient.setGroup(null);
            groupIngredientCache.remove(group);
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

        return value > (target-someSmallAmount) && value < (target+someSmallAmount);
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
        
        for(RecipeIngredient ingredient : getIngredientGroupIngredients(group)) {
            ingredient.setGroup(null);
            Database.saveOrUpdate(ingredient);
        }
        
        groupIngredientCache.remove(group);
        
        Database.delete(group);
        Database.saveOrUpdate(recipe);
        Database.refresh(recipe);
    }
}
