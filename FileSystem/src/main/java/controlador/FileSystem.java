
package controlador;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Directory nowDirectory ;
    private String nowUser = "root";
    private Map<String, User> users = new HashMap<>();
    private Map<String, Group> groups = new HashMap<>();
    
    public FileSystem() {
    }
    
    public void format(int disk, String rootPassword, int blockSize, String filename) throws Exception {
        int diskSize = (int) (disk*0.95);
        int numBlocks = diskSize / blockSize;
        if (numBlocks <= 0) throw new Exception("El tamaño del disco es demasiado pequeño para los bloques definidos.");

        superblock = new SuperBlock();
        superblock.blocksize = blockSize;
        superblock.numblocks = numBlocks;
        superblock.remainingblocks = numBlocks;
        superblock.maxStructures = disk-diskSize +(diskSize - (numBlocks*blockSize));

        Block first = null;
        Block prev = null;
        for (int i = 0; i < numBlocks; i++) {
            Block b = new Block(blockSize);  
            if (first == null) first = b;
            if (prev != null) {
                prev.next = b;   
            }
            prev = b;
        }
        superblock.freeblocks = first; 
        
        Group rootGroup = new Group("root");
        groups.put("root", rootGroup); 
        // usuario root
        User rootUser = new User();
        rootUser.username = "root";
        rootUser.fullname = "Super Usuario";
        rootUser.password = rootPassword;
        
        rootGroup.addUser(rootUser);
        // nodo raiz
        Directory root = new Directory("/", null,rootUser,rootGroup,77);
        nowDirectory = root;
        superblock.rootDirNode = root;

        Directory rootHome = new Directory("root", root,rootUser,rootGroup,77);
        root.addChild(rootHome);
        rootUser.home = rootHome;
        users.put(rootUser.username, rootUser);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }


    /*
    =====================================================
    ALLOCATION
    =====================================================
    */
    public Block asigBloque(){
        Block free = superblock.freeblocks;
        if(free == null) return null;
        superblock.freeblocks = free.next;
        free.free = false;
        free.next = null;
        superblock.remainingblocks--;
        return free;
    }
    public void liberarBlock(Block b){
        b.free = true;
        b.next = superblock.freeblocks;
        superblock.freeblocks = b;
        superblock.remainingblocks++;
    }
    public boolean fileExist(String filename){
        Node n = nowDirectory.findChild(filename);
        return !(n ==null || n.isDirectory());
        
    }
    public void writeFile(String filename, String content) throws Exception {
        Node n = nowDirectory.findChild(filename);
        if(n ==null || n.isDirectory()){
            throw new Exception("Error: File not found");
        }
        FileControlBlock fcb = (FileControlBlock) n;
        //liberar
        Block actual = fcb.startblock;
        while (actual != null) {
            Block next = actual.next;
            liberarBlock(actual);
            actual = next;
        }
        byte[] bytes = content.getBytes();
        int blockSize = superblock.blocksize;

        Block prev = null;
        int offset = 0;
        
        while(offset < bytes.length){
            Block nuevo = asigBloque();
            if (nuevo == null)
                throw new Exception("Error: there is no space in the disc");

            int length = Math.min(blockSize, bytes.length - offset);
            System.arraycopy(bytes, offset, nuevo.data, 0, length);
            offset += length;

            if (prev == null)
                fcb.startblock = nuevo;
            else
                prev.next = nuevo;

            prev = nuevo;
        }
        fcb.size = bytes.length;
        
    }
    public String readFile(String filename) throws Exception {
        Node n = nowDirectory.findChild(filename);
        if(n ==null || n.isDirectory()){
            throw new Exception("Erro: File not found");
        }
        FileControlBlock fcb = (FileControlBlock) n;
        
        Block actual = fcb.startblock;
        StringBuilder sb = new StringBuilder();
        while(actual != null){
            sb.append(new String(actual.data));
            actual = actual.next;
        }
        return sb.toString().trim();
    }
    /*
    =====================================================
    COMANDOS
    =====================================================
    */
    public void mkdir(String name) {
        if (superblock.numStructures>= superblock.maxStructures){
            System.out.println("No hay espacio para nuevas estructuras.");
            return;
        }
        if (nowDirectory == null) {
            System.out.println("Directorio actual inválido.");
            return;
            }
        if (nowDirectory.findChild(name) != null) {
            System.out.println("Ya existe: " + name);
            return;
        }
        User u = users.get(nowUser);
        Group g = groups.get(u.username);
        Directory d = new Directory(name, nowDirectory,u,g,77);
        nowDirectory.addChild(d);
        superblock.numStructures++;
        System.out.println("Directorio creado: " + name);
        
    }
    public void rm(String filename, boolean r){
        Node n = nowDirectory.findChild(filename);
        if(n != null){
            if(r){
                nowDirectory.removeChild(n);
            }
            else{
                if (n.isDirectory()){
                    nowDirectory.childs.addAll(((Directory)n).childs);
                }
                nowDirectory.removeChild(n);
            }
            superblock.numStructures--;
        }
    }
    public void mv(String filename, String directory){
        Node file = nowDirectory.findChild(filename);
        Node dir = nowDirectory.findChild(directory);
        if (file != null && dir != null){
            if (dir.isDirectory()){
                ((Directory)dir).addChild(file);
                nowDirectory.removeChild(file);
            }
        }
    }
    
    public void touch(String filename) {
        if (superblock.numStructures>= superblock.maxStructures){
            System.out.println("Error: No hay espacio suficiente para una nueva estructura.");
            return;
        }
        if (nowDirectory == null) {
            System.out.println("Error: directorio actual inválido.");
            return;
        }
        if(nowDirectory.findChild(filename)!=null){
            System.out.println("Error: Ya existe un nodo con ese nombre: " + filename);
            return;
        }
        User u = users.get(nowUser);
        Group g = groups.get(u.username);
        
        FileControlBlock fcb = new FileControlBlock(filename,u,g,77,null, nowDirectory); //Implementar poner nodo libre
        nowDirectory.addChild(fcb);
        superblock.numStructures++;

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
    public Node nodeOnPath(Node n, String[] path){
        if (path[0]== null || n == null){
            return null;
        }
        if(path[0].equals(n.nombre)){
            return n;
        }
        if (n.isDirectory()){
            return nodeOnPath(((Directory)n).findChild(path[0]),Arrays.copyOfRange(path, 1, path.length));
        }
        return null;
    }
    
    public void infoFS(){
        System.out.println("Nombre del FileSystem: myFs");
        System.out.println("Tamaño: "+ superblock.blocksize*superblock.numblocks+" MB");
        System.out.println("Utilizado: "+ (superblock.blocksize*superblock.numblocks - superblock.blocksize*superblock.remainingblocks)+" MB");
        System.out.println("Disponible: "+ superblock.blocksize*superblock.remainingblocks+" MB");
    }
    
    public void viewFCB(String filename){
        Node n = nowDirectory.findChild(filename);
        if (!n.isDirectory()){
            FileControlBlock temp = (FileControlBlock)n;
            System.out.println("Nombre: "+temp.nombre);
            System.out.println("Dueño: "+temp.owner.username);
            System.out.println("Fecha de creación: "+temp.createdAt);
            System.out.println((temp.open? "Abierto":"Cerrado"));
            System.out.println("Tamaño: "+temp.size);
            System.out.println("Ubicación: "+temp.path());
        }
    }
    
    public void ls(boolean r){
        System.out.println(nowDirectory.nombre);
        for(Node n : nowDirectory.childs){
            System.out.println("> "+(n.isDirectory() ? "d " : "f ") + n.nombre);
            if (r && n.isDirectory()){recursiveLs((Directory)n,"  ");}
        }
    }
    public void recursiveLs(Directory n, String level){
        for(Node node : n.childs){
            System.out.println(level+"> "+(node.isDirectory() ? "d " : "f ") + node.nombre);
            if (n.isDirectory()) {recursiveLs((Directory)node,level+"  ");}
        }
    }
    public String pwd() { return nowDirectory.path(); }
    public String whereIs(String target){
        Node search = searchDFS(nowDirectory, target);
        return search != null ? search.path() : null;
    }
    
    public Node searchDFS(Directory n, String target){
        for(Node c: n.childs){
            if(!c.isDirectory()){
                if(c.nombre.equals(target)){
                    return c;
                }
            }else{
                Node temp = searchDFS((Directory)c,target);
                if(temp!=null){
                    return temp;
                }
            }
        }return null;
    }
    
    public void openFile(String n){
        Node c = nowDirectory.findChild(n);
        if(c != null && !c.isDirectory()){
            ((FileControlBlock)c).open = true;
        }
    }
    
    public void closeFile(String n){
        Node c = nowDirectory.findChild(n);
        if(c != null && !c.isDirectory()){
            ((FileControlBlock)c).open = false;
        }
    }
    
    public void ln(String name, String path){
        String[] partes = path.split("/");
        Node n = nodeOnPath(superblock.rootDirNode, Arrays.copyOfRange(partes, 1, partes.length));
        if(n!= null && !n.isDirectory()){
            FileControlBlock temp = (FileControlBlock)n;
            FileControlBlock node = new FileControlBlock(name, temp.owner,temp.group, temp.permissions, temp.startblock, nowDirectory);
            nowDirectory.addChild(node);
        }
    }
    public void chown(String username, String filename, boolean r){
        User u = users.get(username);
        if(u != null){
            Node n = nowDirectory.findChild(filename);
            if(n != null){
                n.owner = u;
                if (r && n.isDirectory()){recursiveChown((Directory)n,u);}
            }
        }
    }
    
    public void recursiveChown(Directory n, User u){
        for(Node node : n.childs){
            node.owner = u;
            if (n.isDirectory()) {recursiveChown((Directory)node,u);}
        }
    }
    public void chmod(int permUser, int permGr, String filename){
        User current = users.get(nowUser);
        Node n = nowDirectory.findChild(filename);
        if(n==null){
            System.out.println("Error: the"+filename+" doesnt exist");
        }
        if(!current.username.equals("root") && n.owner!= current){
            System.out.println("Error: only the owner or root are alloed to make changes");
        }
        n.permissions = permUser *10+permGr;
        System.out.println("Updated permissions: "+filename+" -> "+n.permissions);
    }
    
    /*
    =====================================================
    GRUPOS
    =====================================================
    */
    public boolean groupadd(String groupName) {
        if (superblock.numStructures>= superblock.maxStructures){
            return false;
        }
        if (groups.containsKey(groupName)) {
            return false;
        }
        groups.put(groupName, new Group(groupName));
        superblock.numStructures++;
        return true;
    }
    public boolean usermod(String username, String groupname){
        User u = users.get(username);
        Group g = groups.get(groupname);

        if (u == null || g == null ) {
            return false;
        }
        g.addUser(u);
        return true;
        
    }
    public void chgrp(String groupname,String recursive,String target){
        Group g = groups.get(groupname);
        if(g==null){
            System.out.println("Error: the group doesnt exist");
        }
        Node t = nowDirectory.findChild(target);
        if(t==null){
            System.out.println("Error: the"+target+" doesnt exist");
        }
        t.group=g;
        if(recursive.contains("-R")&& t.isDirectory()){
            chgrpRecurvise((Directory) t,g);
        }
        System.out.println("Group changed: "+target+"->"+groupname);
        
    }
    public void chgrpRecurvise(Directory d, Group g){
        for(Node n: d.childs){
            n.group = g;
            if(n.isDirectory()){
                chgrpRecurvise((Directory) n,g);
            }
        }
    }
    
    /*
    =====================================================
    USUARIOS
    =====================================================
    */ 
    
    public void useradd(String username, String fullname, String pass1) {
        if (superblock.numStructures>= superblock.maxStructures){
            System.out.println("Error: No hay espacio para nuevas estructuras.");
            return;
        }
        User u = new User();
        u.username = username;
        u.fullname = fullname;
        u.password = pass1;
        users.put(u.username, u);
        
        Group g = new Group(username);
        g.addUser(u);
        groups.put(username, g);

        Directory home = new Directory(username,superblock.rootDirNode,u,g,70);
        superblock.rootDirNode.addChild(home);
        u.home = home;
        superblock.numStructures++;
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
