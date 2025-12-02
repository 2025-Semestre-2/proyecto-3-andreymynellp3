/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Andrey
 */
public class Directory implements Serializable{
    public static final long serialVersionUID = 1L;
    public String name;
    public Integer father;
    public ArrayList<Integer> childs;

    public Directory(){}
    
    public Directory(String name, int father) {
        this.name = name;
        this.father = father;
    }
}
