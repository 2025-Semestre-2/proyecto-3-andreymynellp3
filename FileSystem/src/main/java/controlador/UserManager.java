
package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.User;

public class UserManager {

    private Map<String, User> users;
    private String currentUser;
    private int nextId = 1000;
    
    public UserManager(){
    
        users = new HashMap<>();
        currentUser = "guest";
        
    }
    public void useradd (String name,String username, String password){
        
        User u = new User(username,name,password,nextId,nextId);
        nextId++;
        users.put(username, u);
        currentUser = username;

    }
    public void passwd (String username,String password){      
        users.get(username).setPassword(password);
   
    }
    public boolean userExists (String username){
        return (users.containsKey(username));
    }
    public void logout(){
        currentUser = "guest";
    }
    public String getCurrentUser(){
        return currentUser;
    }
    
    public void su(String currentUser) {
        this.currentUser = currentUser;
    }
    
    public boolean isLogget(){
        return currentUser.equals("guest");
    }
    
}

