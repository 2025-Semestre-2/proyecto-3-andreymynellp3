package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FileControlBlock extends Node {
    private static final long serialVersionUID = 1L;
    public Block startblock;
    public LocalDateTime createdAt;
    public boolean open = false;
    public int size =0;
    
    public FileControlBlock(String name, User owner, int permitions, Block startblock, Directory father) {
        super(name,father,owner, permitions);
        this.startblock = startblock;
        this.createdAt = LocalDateTime.now();
    }
    
    @Override
    public boolean isDirectory() {
        return false;
    }
}
