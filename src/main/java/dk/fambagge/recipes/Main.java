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
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
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

            ContextHandlerCollection contexts = new ContextHandlerCollection();

            WebAppContext webContext = new AliasEnhancedWebAppContext();

            webContext.setContextPath("/");

            webContext.setConfigurations(new Configuration[]{
                new AnnotationConfiguration(), new WebXmlConfiguration(),
                new WebInfConfiguration(),
                new PlusConfiguration(), new MetaInfConfiguration(),
                new FragmentConfiguration(), new EnvConfiguration()
            });

            File webappDir = new File("./webapp");

            if (webappDir.exists() && webappDir.isDirectory()) {
                //Running from deployed zipfile
                System.out.println("Deployed zipfile!");
                webContext.setBaseResource(new ResourceCollection(new String[]{
                    "./webapp"
                }));
                webContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/*.jar");
            } else {
                //Running from within netbeans
                System.out.println("Netbeans run!");
                webContext.setBaseResource(new ResourceCollection(new String[]{
                    "./src/main/webapp",
                    "./target"
                }));
                webContext.setResourceAlias("/WEB-INF/classes/", "/classes/");
                webContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/classes/.*");
            }

            ServletContextHandler imageHandler = new ServletContextHandler();
            imageHandler.setContextPath("/images");

            ServletHolder holderHome = new ServletHolder("static-home", DefaultServlet.class);
            holderHome.setInitParameter("resourceBase", "./uploads");
            holderHome.setInitParameter("dirAllowed","true");
            holderHome.setInitParameter("pathInfoOnly","true");
            imageHandler.addServlet(holderHome, "/*");
        
            contexts.setHandlers(new Handler[]{webContext, imageHandler});

            server.setHandler(contexts);

            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
