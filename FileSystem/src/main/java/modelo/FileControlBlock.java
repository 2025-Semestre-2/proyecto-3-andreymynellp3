package modelo;

import modelo.acceso.Protection;

public class FileControlBlock {
    private int index;
    private String name;
    private int owner;
    private int permitions;
    private int startblock;

    public FileControlBlock(int index, String name, int owner, int permitions, int startblock) {
        this.index = index;
        this.name = name;
        this.owner = owner;
        this.permitions = permitions;
        this.startblock = startblock;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getPermitions() {
        return permitions;
    }

    public void setPermitions(int permitions) {
        this.permitions = permitions;
    }

    public int getStartblock() {
        return startblock;
    }

    public void setStartblock(int startblock) {
        this.startblock = startblock;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
