/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.directorios;

import java.util.ArrayList;
import java.util.List;
import modelo.directorios.Directory;


public class TreeStructure {
    int inodeID;
    TreeStructure padre;
    List<Directory> entradas;

    public TreeStructure(int inodeID, TreeStructure padre) {
        this.inodeID = inodeID;
        this.padre = padre;
        this.entradas = new ArrayList<>();
    }
    
    public void addEntrada(Directory entrada){
        entradas.add(entrada);
        
    }
    public void removeEntrada(String name) {
        entradas.removeIf(e -> e.name.equals(name));
    }
    public Directory findEntry(String name) {
        return entradas.stream().filter(e -> e.name.equals(name))
                      .findFirst()
                      .orElse(null);
    }
    
}
