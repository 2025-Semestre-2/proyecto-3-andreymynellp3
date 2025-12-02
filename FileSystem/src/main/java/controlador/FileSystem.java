
package controlador;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modelo.Block;
import modelo.Directory;
import modelo.FileControlBlock;
import modelo.Group;
import modelo.SuperBlock;
import modelo.User;

public class FileSystem implements Serializable {
    private static final long serialVersionUID = 1L;
    static final int T_DIR = 1, T_FCB = 2, T_GROUP = 4, PREFIX_BASE = 1_000_000;
    Map<Integer, Integer> counters = new HashMap<>();
    
    private SuperBlock superblock;
    private Map<Integer,Directory> directories;
    private Map<Integer,FileControlBlock> fbs;
    private Map<Integer,Block> blocks;
    private Map<String,User> users;
    private Map<Integer,Group> groups;
    
    private Integer nowDirectory;
    private String nowUser;
    
    public FileSystem() {
        counters.put(T_DIR, 1);
        counters.put(T_FCB, 1);
        counters.put(T_GROUP, 1);
    }
    
    public void format(int diskSize, String rootPassword, String strategy, int blockSize, String filename) throws Exception {
        int numBlocks = diskSize / blockSize;
        if (numBlocks <= 0) {
            throw new Exception("El tamaño del disco es demasiado pequeño para los bloques definidos.");
        }
        superblock = new SuperBlock();
        superblock.blocksize = blockSize;
        superblock.numblocks = numBlocks;
        superblock.type = strategy;
        superblock.struArea = 0;
        superblock.userArea = diskSize;
        superblock.freeblocks = 5;
        superblock.root = 1;

        directories.clear();
        fbs.clear();
        blocks.clear();
        users.clear();
        groups.clear();

        for (int i = 0; i < numBlocks; i++) {
            Block b = new Block();
            b.data = new ArrayList<>();
            b.next = -1;
            blocks.put(i, b);
        }

        User root = new User();
        root.username = "root";
        root.fullname = "Super Usuario";
        root.password = rootPassword;
        users.put(root.username, root);

        Directory rootDir = new Directory();
        rootDir.name = "/";
        rootDir.father = -1;
        rootDir.childs = new ArrayList<>();
        directories.put(1,rootDir);
        
        Directory home = new Directory();
        home.name = "root";
        home.father = 1;
        home.childs = new ArrayList<>();
        int homeid = nextId(1);
        directories.put(homeid,home);
        root.root = homeid;

        rootDir.childs.add(homeid);
        nowDirectory = 1;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }
    
    public void format(int newDiskSize, int newBlockSize, String rootPassword, String strategy, String filename) throws Exception {
        boolean isNew = (superblock == null);
        if (isNew || (fbs.isEmpty() && directories.isEmpty())) {
            format(newDiskSize, rootPassword, strategy, newBlockSize, filename);
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
        superblock.type = strategy;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }
    
    public void useradd(String username, String fullname, String pass1) {
        User u = new User();
        u.username = username;
        u.fullname = fullname;
        u.password = pass1;
        users.put(u.username, u);

        Directory home = new Directory();
        home.name = username;
        home.father = superblock.root;
        home.childs = new ArrayList<>();
        int homeid = nextId(1);
        directories.put(homeid,home);
        u.root = homeid;

        Directory rootDir = directories.get(superblock.root);
        rootDir.childs.add(homeid);
        System.out.println("Usuario creado: " + username);
    }
    
    public void changeUser (String username, String password){ 
        User user = users.get(username);
        nowUser = user.username;
        nowDirectory = user.root;
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
    
    public void mkdir(String... names) {
        Directory current = directories.get(nowDirectory);
        if (current == null) {
            System.out.println("Error: directorio actual inválido.");
            return;
        }
        for (String name : names) {
            Directory d = new Directory();
            d.name = name;
            d.father = nowDirectory;
            d.childs = new ArrayList<>();
            int id = nextId(1);

            directories.put(id,d);
            current.childs.add(id);
            System.out.println("Directorio creado: " + name);
        }
    }
    
    public void touch(String filename, int ownerId) {
        Directory current = directories.get(nowDirectory);
        if (current == null) {
            System.out.println("Error: directorio actual inválido.");
            return;
        }

        FileControlBlock fcb = new FileControlBlock();
        int id = nextId(2);
        fcb.name = filename;
        fcb.owner = ownerId;
        fcb.permitions = 77;
        fcb.startblock = -1;

        fbs.put(id,fcb);
        current.childs.add(id);

        System.out.println("Archivo creado: " + filename);
    }

    public String getCurrentUser(){
        return this.nowUser;
    }

    public int nextId(int type) {
        int c = counters.get(type);
        counters.put(type, c + 1);
        return type * PREFIX_BASE + c;
    }
}
