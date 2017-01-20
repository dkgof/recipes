/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes;

import java.io.File;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

/**
 *
 * @author gof
 */
public class Main {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(System.getProperty("jetty.port", "8080"));

            Server server = new Server(port);

            WebAppContext context = new AliasEnhancedWebAppContext();

            context.setContextPath("/");

            context.setConfigurations(new Configuration[]{
                new AnnotationConfiguration(), new WebXmlConfiguration(),
                new WebInfConfiguration(),
                new PlusConfiguration(), new MetaInfConfiguration(),
                new FragmentConfiguration(), new EnvConfiguration()
            });

            File webappDir = new File("./webapp");

            if (webappDir.exists() && webappDir.isDirectory()) {
                //Running from deployed zipfile
                System.out.println("Deployed zipfile!");
                context.setBaseResource(new ResourceCollection(new String[]{
                    "./webapp"
                }));
                context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/*.jar");
            } else {
                //Running from within netbeans
                System.out.println("Netbeans run!");
                context.setBaseResource(new ResourceCollection(new String[]{
                    "./src/main/webapp",
                    "./target"
                }));
                context.setResourceAlias("/WEB-INF/classes/", "/classes/");
                context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/classes/.*");
            }

            server.setHandler(context);

            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
