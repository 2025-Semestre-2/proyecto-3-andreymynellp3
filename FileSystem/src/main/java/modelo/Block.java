
package modelo;

import java.io.Serializable;

public class Block implements Serializable {
    ;
    public byte[] data;
    public Block next;
    public boolean free;

    public Block(){}
    
    public Block(int blocksize) {
        data = new byte[blocksize];
        free = true;
        next = null;
    }
}
