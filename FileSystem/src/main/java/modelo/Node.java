
package modelo;

import java.io.Serializable;


public abstract class Node implements Serializable {
    public String nombre;
    public Directory padre;

    public Node(String nombre, Directory padre) {
        this.nombre = nombre;
        this.padre = padre;
    }
    public abstract boolean isDirectory();
    public String path(){
        if (padre == null) {
            return (nombre == null || nombre.isEmpty()) ? "/" : nombre;
        }
        if (padre.padre == null) {
            return "/" + nombre;
        }
        return padre.path() + "/" + nombre;
    }
    
}
