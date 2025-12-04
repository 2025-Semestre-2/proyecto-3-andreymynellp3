
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
import modelo.FileControlBlock;
import modelo.Group;
import modelo.Node;
import modelo.SuperBlock;
import modelo.User;

public class FileSystem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private SuperBlock superblock;
    private Map<Integer, Block> blocks = new HashMap<>();
    private Directory root; // "/"
    private Directory nowDirectory ;
    private String nowUser = "root";
    private Map<String, User> users = new HashMap<>();
    
    public FileSystem() {
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
        
        blocks.clear();
        Block prev = new Block();
        
        superblock.freeblocks = prev;
        for (int i = 1; i < numBlocks; i++) {
            Block b = new Block();
            b.data = new ArrayList<>();//??
            b.next = prev;
            prev = b;
            blocks.put(i,b);
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

    //pendiente de implementar de manera correcta (linked)
    public void writeFile(String fsFile, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("miDiscoDuro.fs", false))) {
            writer.write(content);  
        } catch (IOException e) {
            System.out.println("Error guardando en el FS: " + e.getMessage());
        }
    }
    // COMANDOS
    public void mkdir(String name) {
        if (nowDirectory == null) {
            System.out.println("Directorio actual inválido.");
            return;
            }
        if (nowDirectory.findChild(name) != null) {
            System.out.println("Ya existe: " + name);
        }
        Directory d = new Directory(name, nowDirectory);
        nowDirectory.addChild(d);
        System.out.println("Directorio creado: " + name);
        
    }
    
    public void touch(String filename) {
        if (nowDirectory == null) {
            System.out.println("Error: directorio actual inválido.");
            return;
        }
        if(nowDirectory.findChild(filename)!=null){
            System.out.println("Error: Ya existe un nodo con ese nombre: " + filename);
            return;
        }
        FileControlBlock fcb = new FileControlBlock(filename,users.get(nowUser),77,null, nowDirectory); //Implementar poner nodo libre
        nowDirectory.addChild(fcb);

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
        Node n = nowDirectory.findChild(directorio); //Pendiente aplicar recursivo
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
}
