/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.db.DomainObject;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author Rolf Bagge
 */
@Entity
@Table(name = "media")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Media implements DomainObject, Serializable, Comparable<Media> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    private String filename;

    private int sortOrder;

    public String getUrl() {
        return "/images/"+filename;
    }

    public String getThumbnailUrl(int width) {
        File thumbnailFile = new File("./uploads/"+filename.substring(0, filename.lastIndexOf("."))+"-"+width+".jpg");
        
        if(!thumbnailFile.exists()) {
            try {
                //Create scaled version
                BufferedImage origImage = ImageIO.read(new File("./uploads/"+filename));
                
                BufferedImage scaledImage = scaleImage(origImage, width);
                
                ImageIO.write(scaledImage, "jpg", thumbnailFile);                
            } catch (IOException ex) {
                Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "/images/"+thumbnailFile.getName();
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the order
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * @param order the order to set
     */
    public void setSortOrder(int order) {
        this.sortOrder = order;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Media) {
            Media m = (Media) obj;

            return m.id == this.id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public int compareTo(Media m) {
        return Integer.compare(this.sortOrder, m.sortOrder);
    }

    public static BufferedImage scaleImage(BufferedImage image, int wantedWidth) {
        int imageWidth = Math.min(image.getWidth(), wantedWidth);

        Image scaledInstance = image.getScaledInstance(imageWidth, -1, Image.SCALE_SMOOTH);

        BufferedImage scaledImage = new BufferedImage(imageWidth, scaledInstance.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
        scaledImage.getGraphics().drawImage(scaledInstance, 0, 0, null);

        return scaledImage;
    }
    
    public String toString() {
        return "["+id+"] - "+filename;
    }
}
