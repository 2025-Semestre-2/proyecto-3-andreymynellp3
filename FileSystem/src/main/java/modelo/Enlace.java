/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Enlace extends Node {
    public FileControlBlock objetivo;

    public Enlace(String name, Directory father, FileControlBlock objetivo) {
        super(name, father, objetivo.owner, objetivo.group, objetivo.permissions);
        this.objetivo = objetivo;
    }

    @Override
    public boolean isDirectory() { return false; }
    @Override
    public boolean isLink(){return true;}
}

