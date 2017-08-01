/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.web.views;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Rolf Bagge
 */
@ManagedBean
@ViewScoped
public class FileUploadView implements Serializable {
    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("Handling upload: "+event);
    }
}
