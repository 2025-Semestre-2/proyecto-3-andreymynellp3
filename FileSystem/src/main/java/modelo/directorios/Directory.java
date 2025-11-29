
package modelo.directorios;


public class Directory {
    String name;
    String inodeID;
    boolean isDirectory;

    public Directory(String name, String inodeID, boolean isDirectory) {
        this.name = name;
        this.inodeID = inodeID;
        this.isDirectory = isDirectory;
    }
    
    
}
