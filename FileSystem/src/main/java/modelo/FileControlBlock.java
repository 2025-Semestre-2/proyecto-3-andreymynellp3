package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FileControlBlock extends Node {
    public Block startblock;
    public LocalDateTime createdAt;
    public boolean open = false;
    
    public FileControlBlock(String name, User owner, Group group, int permitions, Block startblock, Directory father) {
        super(name,father,owner,group,permitions);
        this.startblock = startblock;
        this.createdAt = LocalDateTime.now();
    }
    
    @Override
    public boolean isDirectory() {
        return false;
    }
}
