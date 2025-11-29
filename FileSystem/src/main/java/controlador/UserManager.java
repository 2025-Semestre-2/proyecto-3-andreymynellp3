
package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.acceso.User;

public class UserManager {

    private Map<String, User> users;
    private String currentUser;
    private int nextId = 1000;
    
    public UserManager(){
    
        users = new HashMap<>();
        
        users.put("root", new User("root","Super root","rootpass",0,0));
        
        currentUser = "root";
        
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
    public boolean changeUser (String username,String password){ 
        if(!userExists(username)){
            return false;
        }
        String storepass = users.get(username).getPassword();
        if(storepass.equals(password)){
            su(username);
            return true;
        }
        return false;
    }
    public String whoami(){
        String msj = "username: "+getCurrentUser()+"\n";
        msj += "Full name: "+ users.get(getCurrentUser()).getFullname();
        return msj;
    }
    public boolean userExists (String username){
        return (users.containsKey(username));
    }
    public void logout(){
        currentUser = "root";
    }
    public String getCurrentUser(){
        return currentUser;
    }
    
    public void su(String currentUser) {
        this.currentUser = currentUser;
    }
    
    public boolean isLogget(){
        return currentUser.equals("root");
    }
    
}

