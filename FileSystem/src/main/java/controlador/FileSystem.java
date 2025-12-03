
package controlador;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modelo.Block;
import modelo.Directory;
import modelo.File;
import modelo.FileControlBlock;
import modelo.Group;
import modelo.Node;
import modelo.SuperBlock;
import modelo.User;

public class FileSystem implements Serializable {
    private static final long serialVersionUID = 1L;
    static final int T_FCB = 2, T_GROUP = 4, PREFIX_BASE = 1_000_000;
    Map<Integer, Integer> counters = new HashMap<>();
    
    private SuperBlock superblock;
    private Map<Integer,FileControlBlock> fbs = new HashMap<>();;
    private Map<Integer,Block> blocks = new HashMap<>();;
    private Map<String,User> users = new HashMap<>();;
    private Map<Integer,Group> groups = new HashMap<>();;
    
    private Directory root; // "/"
    private Directory nowDirectory ;
    private String nowUser = "root";
    
    public FileSystem() {
        counters.put(T_FCB, 1);
        counters.put(T_GROUP, 1);
    }
    
    public void format(int diskSize, String rootPassword, int blockSize, String filename) throws Exception {
        int numBlocks = diskSize / blockSize;
        if (numBlocks <= 0) {
            throw new Exception("El tamaño del disco es demasiado pequeño para los bloques definidos.");
        }
        superblock = new SuperBlock();
        superblock.blocksize = blockSize;
        superblock.numblocks = numBlocks;
        superblock.struArea = 0;
        superblock.userArea = diskSize;
        superblock.freeblocks = numBlocks;
        
        //inicializar bloques
        fbs.clear();
        blocks.clear();
        users.clear();
        groups.clear();

        for (int i = 0; i < numBlocks; i++) {
            Block b = new Block();
            b.data = new ArrayList<>();//??
            b.next = -1;
            blocks.put(i, b);
        }
        //nodo raiz
        root = new Directory("/",null);
        nowDirectory = root;
        superblock.rootDirNode = root;
        
        //usuario root
        User rootUser = new User();
        rootUser.username = "root";
        rootUser.fullname = "Super Usuario";
        rootUser.password = rootPassword;
  

        Directory rootHome = new Directory("root",root);
        root.addChild(rootHome);
        rootUser.home = rootHome;
        users.put(rootUser.username, rootUser);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    public void format2(int newDiskSize, int newBlockSize, String rootPassword, String filename) throws Exception {
        boolean isNew = (superblock == null);
        if (isNew || fbs.isEmpty()) {
            format(newDiskSize, rootPassword, newBlockSize, filename);
            return;
        }
        class InMemoryFile {
            FileControlBlock fcb;
            ArrayList<Byte> data;
        }
        ArrayList<InMemoryFile> tempFiles = new ArrayList<>();
        for (Map.Entry<Integer, FileControlBlock> entry : fbs.entrySet()) {
            FileControlBlock fcb = entry.getValue();
            InMemoryFile m = new InMemoryFile();
            m.fcb = fcb;
            m.data = new ArrayList<>();
            int blockId = fcb.startblock;
            while (blockId != -1) {
                Block b = blocks.get(blockId);
                if (b != null) {
                    m.data.addAll(b.data);
                    blockId = b.next;
                } else break;
            }
            tempFiles.add(m);
        }
        int totalBytes = 0;
        for (InMemoryFile f : tempFiles) {
            totalBytes += f.data.size();
        }
        int newNumBlocks = newDiskSize / newBlockSize;
        int newTotalCapacity = newNumBlocks * newBlockSize;
        if (totalBytes > newTotalCapacity) {
            throw new Exception("ERROR: No se puede reducir el disco. "
                    + "Los datos existentes no caben en el nuevo tamaño.");
        }
        blocks.clear();
        int idCounter = 5;
        for (InMemoryFile f : tempFiles) {
            ArrayList<Byte> rem = f.data;
            int pos = 0;
            Integer firstBlock = -1;
            Integer prevBlock = -1;
            while (pos < rem.size()) {
                Block b = new Block();
                b.data = new ArrayList<>();
                int amount = Math.min(newBlockSize, rem.size() - pos);
                for (int i = 0; i < amount; i++) {
                    b.data.add(rem.get(pos + i));
                }
                pos += amount;
                b.next = -1;
                blocks.put(idCounter,b);
                if (firstBlock == -1)
                    firstBlock = idCounter;
                if (prevBlock != -1)
                    blocks.get(prevBlock).next = idCounter;

                prevBlock = idCounter++;
            }

            f.fcb.startblock = (firstBlock == -1 ? -1 : firstBlock);
        }
        superblock.blocksize = newBlockSize;
        superblock.numblocks = newNumBlocks;
        superblock.userArea = newDiskSize;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }
    //pendiente de implementar de manera correcta (linked)
    public void writeFile(String fsFile, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("miDiscoDuro.fs", false))) {
            writer.write(content);  
        } catch (IOException e) {
            System.out.println("Error guardando en el FS: " + e.getMessage());
        }
    }
    // COMANDOS
    public void mkdir(String... names) {
        if (nowDirectory == null) {
            System.out.println("Directorio actual inválido.");
            return;
            }
        for (String name : names) {
            if (nowDirectory.findChild(name) != null) {
                System.out.println("Ya existe: " + name);
                continue;
            }
            Directory d = new Directory(name, nowDirectory);
            nowDirectory.addChild(d);
            System.out.println("Directorio creado: " + name);
        }
}
    
    public void touch(String filename, int ownerId) {
        if (nowDirectory == null) {
            System.out.println("Error: directorio actual inválido.");
            return;
        }
        if(nowDirectory.findChild(filename)!=null){
            System.out.println("Error: Ya existe un nodo con ese nombre: " + filename);
            return;
        }
        
        int id = nextId(T_FCB);
        FileControlBlock fcb = new FileControlBlock(filename,ownerId,77,-1);
        fbs.put(id,fcb);
        
        File f = new File(filename,nowDirectory,id);
        nowDirectory.addChild(f);

        System.out.println("Archivo creado: " + filename);
    }
    public boolean cd(String directorio){
        if(directorio.equals("..")){
            if(nowDirectory.padre != null){
                nowDirectory = nowDirectory.padre;
                return true;
            }
            return false;
        }
        Node n = nowDirectory.findChild(directorio);
        if(n!=null && n.isDirectory()){
            nowDirectory = (Directory)n;
            return true;
        }
        return false;
    }
    public void ls(){
        for(Node n : nowDirectory.childs){
            System.out.println((n.isDirectory() ? "d " : "f ") + n.nombre);
        }
    }
    public String pwd() { return nowDirectory.path(); }
    //User Manager 
    public void useradd(String username, String fullname, String pass1) {
        User u = new User();
        u.username = username;
        u.fullname = fullname;
        u.password = pass1;
        users.put(u.username, u);

        Directory home = new Directory(username,root);
        root.addChild(home);
        u.home = home;
        users.put(username, u);
    }
    
    public boolean changeUser (String username, String password){ 
        if(!userExists(username)){
            return false;
        }
        User user = users.get(username);
        String storepass = user.password;
        if(storepass.equals(password)){
            su(username);
            nowDirectory = user.home;
            return true;
            
        }
        return false;   
    }
    
    public String whoami(){
        User user = users.get(nowUser);
        String msj = "username: "+user.username+"\n";
        msj += "Full name: "+ user.fullname;
        return msj;
    }
    
    public void passwd (String username, String password){      
        User user = users.get(username);
        user.password = password;
        users.replace(username, user);
    }
    public void su(String currentUser) {
        this.nowUser = currentUser;
    }
    public boolean userExists (String username){
        return (users.containsKey(username));
    }
    public String getCurrentUser(){
        return this.nowUser;
    }
    public void logout(){
        nowUser = "root";
    }
    public boolean isLogget(){
        return nowUser.equals("root");
    }
    public int nextId(int type) {
        int c = counters.get(type);
        counters.put(type, c + 1);
        return type * PREFIX_BASE + c;
    }
}
