
package modelo;

import java.util.ArrayList;

public class Block {
    private int id;
    private ArrayList<Byte> data;
    private int next;

    public Block(int id, ArrayList<Byte> data, int next) {
        this.id = id;
        this.data = data;
        this.next = next;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
