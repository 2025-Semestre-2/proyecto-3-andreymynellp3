
package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;


public abstract class Node implements Serializable {
    public String nombre;
    public Directory padre;
    public User owner;
    public Group group;
    public Integer permissions;
    public LocalDateTime createdAt;

    public Node(String nombre, Directory padre,User owner,Group group,int permissions) {
        this.nombre = nombre;
        this.padre = padre;
        this.owner = owner;
        this.group = group;
        this.permissions = permissions;
        this.createdAt = LocalDateTime.now();
    }
    public abstract boolean isDirectory();
    public abstract boolean isLink();
    public String path(){
        if (padre == null) {
            return (nombre == null || nombre.isEmpty()) ? "/" : nombre;
        }
        if (padre.padre == null) {
            return "/" + nombre;
        }
        return padre.path() + "/" + nombre;
    }
    public String getNombre(){
        return nombre;
    }
    
}
