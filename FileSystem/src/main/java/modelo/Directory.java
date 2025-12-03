/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

public class Directory extends Node{
    public ArrayList<Node> childs = new ArrayList<>();

    public Directory(String name, Directory padre){
        super(name,padre);
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
        for(Node n : childs) if(n.equals(nombre)) return n;
        return null;
    }
}
