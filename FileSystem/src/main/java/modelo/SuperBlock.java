
package modelo;

import java.io.Serializable;

public class SuperBlock implements Serializable{
    private static final long serialVersionUID = 1L;
    public Integer blocksize;
    public Integer numblocks;
    public Integer remainingblocks;
    public Integer maxStructures;
    public Integer numStructures = 5;
    
    public Block freeblocks;
    public Directory rootDirNode;
    
    public SuperBlock() {}

    public SuperBlock(int blocksize, int numblocks) {
        this.blocksize = blocksize;
        this.numblocks = numblocks;
    }

    public int getBlocksize() {
        return blocksize;
    }

    public void setBlocksize(int blocksize) {
        this.blocksize = blocksize;
    }

    public int getNumblocks() {
        return numblocks;
    }

    public void setNumblocks(int numblocks) {
        this.numblocks = numblocks;
    }

    public Block getFreeblocks() {
        return freeblocks;
    }

    public void setFreeblocks(Block freeblocks) {
        this.freeblocks = freeblocks;
    }

}