
package modelo;

import java.io.Serializable;


public abstract class Node implements Serializable {
    public String nombre;
    public Directory padre;
    public User owner;
    public Integer permitions;
    public Integer size = 0;

    public Node(String nombre, Directory padre, User owner, Integer perm) {
        this.nombre = nombre;
        this.padre = padre;
        this.owner = owner;
        this.permitions = perm;
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
