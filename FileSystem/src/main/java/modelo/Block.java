
package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<Byte> data;
    public Integer next;

    public Block(){}
    
    public Block(int blocksize) {
        this.data = new ArrayList<>();
        this.next = -1;
    }
}
