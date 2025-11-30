
package controlador;

import java.util.ArrayList;
import modelo.Block;
import modelo.Directory;
import modelo.FileControlBlock;
import modelo.SuperBlock;

public class FileSystem {
    private String file;
    private SuperBlock superblock;
    private String type;
    private Integer root;
    private ArrayList<Directory> directories;
    private ArrayList<FileControlBlock> fbs;
    private ArrayList<Block> blocks;
    private UserManager umanager;
    
    private Integer nowDirectory;
    private Integer idCount;

    public FileSystem(int size, String type) {
        this.type = type;
        int SBsize = 6;
        int blocksize = 2;
        int numblocks = size/blocksize;
        int struArea = (int)(size*0.1);
        int userArea = (int)(size-SBsize-struArea);
        superblock = new SuperBlock(blocksize, numblocks, struArea, userArea);
    }
    
    public FileSystem(int block, int size, String type) {
        this.type = type;
        int SBsize = 6;
        int blocksize = block;
        int numblocks = size/blocksize;
        int struArea = (int)(size*0.1);
        int userArea = (int)(size-SBsize-struArea);
        superblock = new SuperBlock(blocksize, numblocks, struArea, userArea);
    }
    
    public int createDirectory(String name){
        idCount++;
        int temp = Integer.parseInt("1"+idCount.toString());
        if(root == null){
            idCount = 0;
            directories.add(new Directory(temp, name, temp));
            root = idCount;
            return temp;
        }
        directories.get(nowDirectory).setChild(temp);
        directories.add(new Directory(temp, name, nowDirectory));
        return temp;
    }
    
    //PENDIENTE
    public int createFile(String name, ArrayList<Byte> data){
        idCount++;
        int temp = Integer.parseInt("2"+idCount.toString());
        directories.get(nowDirectory).setChild(temp);
        Integer index = superblock.getNumblocks()-superblock.getFreeblocks();
        FileControlBlock n = new FileControlBlock(temp,name,0,0,index);
        for(int i = 0; i<data.size(); i+=superblock.getBlocksize()){
            blocks.add(new Block(index, (ArrayList<Byte>) data.subList(i,i+superblock.getBlocksize()), ++index));
        }
        return 0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public Directory getRoot(){
        return directories.get(root);
    }
}
