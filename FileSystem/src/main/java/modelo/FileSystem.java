
package modelo;

import modelo.directorios.TreeStructure;
import modelo.directorios.OpenFile;

public class FileSystem {
    SuperBlock superblock;
    FileControlBlock[] inodeTabla;
    LinkedAllocation[] dataBlocks;
    FreeSpace freesm;
    OpenFile openF;
    LinkedAllocation linkedALlo;
    TreeStructure treeStr;

  
    
    
    //void mkdir(String path);
    //void rmdir(String path);
    //void createFile(String path);
    //void deleteFile(String path);
    //void writeFile(String path, byte[] data);
    //byte[] read(String path);
    
}
