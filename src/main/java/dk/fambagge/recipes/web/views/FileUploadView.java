/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.Media;
import dk.fambagge.recipes.domain.Recipe;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Rolf Bagge
 */
@Named
@ViewScoped
public class FileUploadView implements Serializable {
    
    @Inject
    private RecipeView recipeView;
    
    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("Handling upload: "+event);
        
        File saveDir = new File("./uploads");
        saveDir.mkdirs();
        
        System.out.println("Upload directory: "+saveDir.getAbsolutePath());
        
        try {
            BufferedImage image = ImageIO.read(event.getFile().getInputstream());
            
            File outputFile = File.createTempFile("recepies", ".jpg", saveDir);
            Media.scaleImage(image, 1920, outputFile);

            //Find order of next media
            int order = -1;
            for(Media m : getRecipeView().getSelectedRecipe().getMedias()) {
                order = Math.max(order, m.getSortOrder());
            }
            
            Media media = new Media();
            media.setSortOrder(order+1);
            media.setFilename(outputFile.getName());
            
            Database.save(media);
            
            Recipe selectedRecipe = getRecipeView().getSelectedRecipe();
            
            selectedRecipe.addMedia(media);
            Database.saveOrUpdate(selectedRecipe);
            
        } catch (Exception ex) {
            Logger.getLogger(FileUploadView.class.getName()).log(Level.SEVERE, "Error during file upload!", ex);
        }
    }

    /**
     * @return the recipeView
     */
    public RecipeView getRecipeView() {
        return recipeView;
    }

    /**
     * @param recipeView the recipeView to set
     */
    public void setRecipeView(RecipeView recipeView) {
        this.recipeView = recipeView;
    }
}
