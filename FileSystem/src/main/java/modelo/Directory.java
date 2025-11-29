/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author Andrey
 */
public class Directory {
    private int id;
    private String name;
    private int father;
    private ArrayList<Integer> childs;

    public Directory(int id, String name, int father) {
        this.id = id;
        this.name = name;
        this.father = father;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFather() {
        return father;
    }

    public void setFather(int father) {
        this.father = father;
    }
    
    public Integer getChild(Integer i){
        return childs.get(i);
    }
    
    public void setChild(Integer i){
        childs.add(i);
    }
    
    public int size(){
        return 3 + childs.size();
    }
}
