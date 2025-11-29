
package modelo;

import java.util.ArrayList;

public class Block {
    private ArrayList<Byte> data;
    private int next;

    public ArrayList<Byte> getData() {
        return data;
    }

    public void setData(ArrayList<Byte> data) {
        this.data = data;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }
}
