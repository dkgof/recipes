/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.fambagge.recipes.domain;

import dk.fambagge.recipes.db.Database;
import dk.fambagge.recipes.db.DomainObject;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 *
 * @author rolf
 */
@Entity
@Table(name = "users")
public class User implements Serializable, DomainObject {

    private static final String permissionSplitter = ":";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userName;

    private String password;
    
    private String permissions;

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
     * @return the permissions
     */
    public String getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public static Set<User> getAll() {
        return Database.getAll(User.class);
    }

    public static User getFromId(long id) {
        return Database.get("id = " + id, User.class);
    }

    public static User getFromUsername(String username) {
        return Database.get("userName = '" + username + "'", User.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return ((User) obj).id == this.id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    public void save() {
        Database.saveOrUpdate(this);
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean hasPermission(String testedPermission) {
        String[] permissionArray = permissions.split(permissionSplitter);
        for (String permission : permissionArray) {
            if (permission.equals(testedPermission)) {
                return true;
            }
        }

        return false;
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
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        this.password = passwordEncryptor.encryptPassword(password);
    }
}
