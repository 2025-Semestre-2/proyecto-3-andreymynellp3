
package modelo;


public class LinkedAllocation {
    byte[] data;
    int nextBlock;   

    public LinkedAllocation(int blockSize) {
        this.data = new byte[blockSize];
        this.nextBlock = -1;
    }
    public int getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(int nextBlock) {
        this.nextBlock = nextBlock;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
