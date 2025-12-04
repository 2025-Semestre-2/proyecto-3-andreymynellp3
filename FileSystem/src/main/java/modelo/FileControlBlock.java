package modelo;

import java.io.Serializable;

public class FileControlBlock extends Node {
    private static final long serialVersionUID = 1L;
    public User owner;
    public Integer permitions;
    public Block startblock;
    int size =0;
    
    public FileControlBlock(String name, User owner, int permitions, Block startblock, Directory father) {
        super(name,father);
        this.owner = owner;
        this.permitions = permitions;
        this.startblock = startblock;
    }
    
    @Override
    public boolean isDirectory() {
        return false;
    }
}
