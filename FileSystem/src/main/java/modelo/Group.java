package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private User owner;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Node> nodes = new ArrayList<>();
    
    public Group(String name, User user) { this.name = name; this.owner = user;}
    
    public void addUser(User u){
        users.add(u);
    }
    
    public void addDirFile(Node d){
        nodes.add(d);
    }  
}
