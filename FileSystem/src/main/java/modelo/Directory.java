/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

public class Directory extends Node{
    public ArrayList<Node> childs = new ArrayList<>();

    public Directory(String name, Directory padre, User owner, Integer perm){
        super(name,padre, owner, perm);
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
    public void addChild(Node node){
        childs.add(node);
        addSize();
    }
    public void removeChild(Node node){
        childs.remove(node);
        subSize();
    }
    public Node findChild(String nombre){
        for(Node n : childs) {
            if (n.nombre != null && n.nombre.equals(nombre)) return n;
        }
        return null;
    }
    public void addSize(){
        if (super.padre != null){
            super.padre.addSize();
        }
        super.size++;
    }
    public void subSize(){
        if (super.padre != null){
            super.padre.addSize();
        }
        super.size++;
    }
}
