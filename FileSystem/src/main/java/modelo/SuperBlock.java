
package modelo;

public class SuperBlock {
    private int blocksize;
    private int numblocks;
    
    private int struArea;
    private int userArea;
    
    private int freeblocks;
    private int root;

    public SuperBlock(int blocksize, int numblocks, int struArea, int userArea) {
        this.blocksize = blocksize;
        this.numblocks = numblocks;
        this.struArea = struArea;
        this.userArea = userArea;
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

    public int getStruArea() {
        return struArea;
    }

    public void setStruArea(int struArea) {
        this.struArea = struArea;
    }

    public int getUserArea() {
        return userArea;
    }

    public void setUserArea(int userArea) {
        this.userArea = userArea;
    }

    public int getFreeblocks() {
        return freeblocks;
    }

    public void setFreeblocks(int freeblocks) {
        this.freeblocks = freeblocks;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }
}