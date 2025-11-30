
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

  
    
    private void format(int block, int size, String type){
        int SBsize = 6;
        int blocksize = block;
        int numblocks = size/blocksize;
        int struArea = (int)(size*0.1);
        int userArea = (int)(size-SBsize-struArea);
        superblock = new SuperBlock(blocksize, numblocks, struArea, userArea);
    }
    //void mkdir(String path);
    //void rmdir(String path);
    //void createFile(String path);
    //void deleteFile(String path);
    //void writeFile(String path, byte[] data);
    //byte[] read(String path);
    
}
