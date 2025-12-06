
package modelo;

import java.util.ArrayList;

public class Directory extends Node{
    public ArrayList<Node> childs = new ArrayList<>();

    public Directory(String name, Directory padre,User owner,Group group,int permissions){
        super(name,padre,owner, group,permissions);
    }

    @Override
    public boolean isDirectory() { 
        return true;
    }
    public void addChild(Node node){
        childs.add(node);
    }
    public void removeChild(Node node){
        childs.remove(node);
    }
    public Node findChild(String nombre){
        for(Node n : childs) {
            if (n.nombre != null && n.nombre.equals(nombre)) return n;
        }
        return null;
    }
}
