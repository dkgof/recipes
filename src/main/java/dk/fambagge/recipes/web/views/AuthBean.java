package dk.fambagge.recipes.web.views;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.domain.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.SessionFactory;

/**
 * Allows the user to navigate between instances
 *
 * @author jbk
 */
@ManagedBean
@SessionScoped
public class AuthBean implements Serializable {
    public static final String REDIRECT_KEY = "RECIPES_REDIRECT";
    public static final String USER_KEY = "RECIPES_USER";

    private User loggedInUser = null;
    
    private String username;
    private String password;
    
    public boolean hasPermission(String permission) {
        if(loggedInUser == null) {
            return false;
        }
        
        return loggedInUser.hasPermission(permission);
    }
    
    public void login() {
        //Make sure database is created
        SessionFactory sessionFactory = Database.getSessionFactory();
        
        //Check login
        System.out.println("Doing login: "+username+" // "+password);
        
        if(username.equals("test") && password.equals("tester")) {
            loggedInUser = new User();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(USER_KEY, loggedInUser);
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(REDIRECT_KEY));
            } catch (IOException ex) {
                Logger.getLogger(AuthBean.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    public User getLoggedInUser() {
        //Make sure we are always refreshed?
        
        User loggedInUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(USER_KEY);
        
        Database.refresh(loggedInUser);
        
        return loggedInUser;
    }

    public String getRedirectUrl() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(REDIRECT_KEY).toString();
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
}
