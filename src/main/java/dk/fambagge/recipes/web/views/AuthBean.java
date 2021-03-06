package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.User;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.hibernate.SessionFactory;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * Allows the user to navigate between instances
 *
 * @author jbk
 */
@Named
@SessionScoped
public class AuthBean implements Serializable {
    public static final String REDIRECT_KEY = "RECIPES_REDIRECT";
    public static final String USER_KEY = "RECIPES_USER";

    private String username;
    private String password;
    
    private String redirectRequestUrl;
    
    public boolean hasPermission(String permission) {
        System.out.println("Checking permission: "+permission);
        
        User loggedInUser = getLoggedInUser();
        
        if(loggedInUser == null) {
            return false;
        }
        
        return loggedInUser.hasPermission(permission);
    }
    
    public void login() {
        //Make sure database is created
        SessionFactory sessionFactory = Database.getSessionFactory();
        
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        User user = User.getFromUsername(username);
        
        if(user != null && passwordEncryptor.checkPassword(password, user.getPassword())) {
            username = "";
            password = "";
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(USER_KEY, user);
            
            try {
                String redirect = null;
                
                if(redirectRequestUrl != null && !redirectRequestUrl.isBlank()) {
                    redirect = URLDecoder.decode(redirectRequestUrl, Charset.forName("UTF-8"));
                } else {
                    redirect = this.getRedirectUrl();
                }
                
                System.out.println(redirect);

                if(redirect == null || redirect.isEmpty()) {
                    redirect = "/";
                }
                
                FacesContext.getCurrentInstance().getExternalContext().redirect(redirect);
            } catch (IOException ex) {
                Logger.getLogger(AuthBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            password = "";
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Username / Password does not check out!"));
        }
    }

    public void logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(REDIRECT_KEY);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(USER_KEY);

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/");
        } catch (IOException ex) {
            Logger.getLogger(AuthBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the loggedInUser
     */
    private User getLoggedInUser() {
        User loggedInUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(USER_KEY);

        //Make sure we are always refreshed?
        /*        
        if(loggedInUser != null) {
            Database.refresh(loggedInUser);
        }
        */
        
        return loggedInUser;
    }

    public String getRedirectUrl() {
        try {
            return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(REDIRECT_KEY).toString();
        } catch(NullPointerException e) {
            return null;
        }
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the redirectRequestUrl
     */
    public String getRedirectRequestUrl() {
        return redirectRequestUrl;
    }

    /**
     * @param redirectRequestUrl the redirectRequestUrl to set
     */
    public void setRedirectRequestUrl(String redirectRequestUrl) {
        this.redirectRequestUrl = redirectRequestUrl;
    }
}
