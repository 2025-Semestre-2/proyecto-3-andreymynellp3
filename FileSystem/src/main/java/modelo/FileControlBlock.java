package modelo;

import modelo.acceso.Protection;

public class FileControlBlock {
    String nombre;
    int id;
    int tam;
    int duenno;
    Protection permisos;
    boolean isDirectory;
    int[] punterosBlock;
    long creacionTime;
    long modificacionTime;
    long accesoTime;
    int firstBlock;
}
