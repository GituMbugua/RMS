/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

/**
 *
 * @author Gitu
 */
public class User extends Person{
    private String username, password, role;
    private boolean isAdmin;
    
    User() {
        
    }
    User(String firstName, String middleName, String surname, String username, String password, String dateOfBirth, 
            String phoneNumber, String address, String email, String role, boolean isAdmin) {
        super(firstName, middleName, surname, dateOfBirth, phoneNumber, address, email);
        this.username = username;
        this.password = password;
        this.role = role;
        this.isAdmin = isAdmin;
        
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setAdminStatus(boolean isAdmin) {
        this.isAdmin = isAdmin;
    } 
    
    public String getUsername() {
        return username;
    }
    public String getRole() {
        return role;
    }
    public boolean getAdminStatus() {
        return isAdmin;
    }
}
