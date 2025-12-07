/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;


public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    private ArrayList<User> users = new ArrayList<>();
  
    
    public Group(String name) { 
        this.name = name; 
    }
    public void addUser(User u) {
        if (!users.contains(u)) {
            users.add(u);
        }
    }

    public boolean hasUser(User u) {
        return users.contains(u);
    }
}
