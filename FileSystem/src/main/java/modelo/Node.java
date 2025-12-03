
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
        if(padre == null) return nombre;
        String p = padre.path();
        if(p.endsWith("/")) return p+nombre;
        return p + "/"+nombre;
    }
    
}
