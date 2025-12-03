package modelo;

/**
 *
 * @author Andrey
 */
public class File extends Node {
    public FileControlBlock fcb;
    public int fcbID;
    
    public File(String nombre, Directory padre, int fcbID){
        super(nombre,padre);
        this.fcbID = fcbID;
    }
    @Override
    public boolean isDirectory() {
        return false;
    }
    
    
}
