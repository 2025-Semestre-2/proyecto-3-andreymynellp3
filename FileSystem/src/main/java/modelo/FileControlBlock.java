package modelo;

import java.io.Serializable;

public class FileControlBlock implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public Integer owner;
    public Integer permitions;
    public Integer startblock;

    public FileControlBlock(){}
    
    public FileControlBlock(String name, int owner, int permitions, int startblock) {
        this.name = name;
        this.owner = owner;
        this.permitions = permitions;
        this.startblock = startblock;
    }
}
