/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<Integer> users = new ArrayList<>();
    private ArrayList<Integer> files = new ArrayList<>();
    
    public Group(String name) { this.name = name; }
}
