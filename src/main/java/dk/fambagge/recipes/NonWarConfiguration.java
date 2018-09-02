/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes;

import java.util.List;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.AbstractConfiguration;
import org.eclipse.jetty.webapp.MetaData;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 *
 * @author rolf
 */
public class NonWarConfiguration extends AbstractConfiguration {

    private List<String> webInfJarsToInclude;
    
    public NonWarConfiguration() {
        webInfJarsToInclude = null;
    }

    public NonWarConfiguration(List<String> webInfJars) {
        webInfJarsToInclude = webInfJars;
    }
    
    @Override
    public void preConfigure(WebAppContext context) throws Exception {
        super.preConfigure(context); //To change body of generated methods, choose Tools | Templates.
        
        //Add extra jar to annotation scanner
        MetaData meta = context.getMetaData();
        
        Resource baseResource = context.getBaseResource();
        
        addFiles(baseResource, meta);
        
    }

    private void addFiles(Resource dir, MetaData meta) {
        for(String filePath : dir.list()) {
            try {
                Resource file = dir.addPath(filePath);
                
                if(file.getName().endsWith(".jar")) {
                    if(webInfJarsToInclude == null || webInfJarsToInclude.contains(file.getFile().getName())) {
                        System.out.println("Adding: "+file);
                        meta.addWebInfJar(file);
                    }
                } else if(file.isDirectory()) {
                    addFiles(file, meta);
                }
            } catch(Exception e) {
                System.out.println(""+e);
            }
        }
    }
    
}
