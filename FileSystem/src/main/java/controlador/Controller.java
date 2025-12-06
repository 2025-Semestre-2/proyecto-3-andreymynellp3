
package controlador;


import java.util.Scanner;
import controlador.FileSystem;
import java.util.ArrayList;
import java.util.List;


public class Controller {
    private FileSystem fs;
    private Scanner scanner;
    
    public Controller(){
        scanner = new Scanner(System.in);
        this.fs = new FileSystem();
       
    }
    public void ejecutar() throws Exception{
        String filename="";
        System.out.println("execute terminal");
        while(true){
            String input = scanner.nextLine().trim();
            String [] partes = input.split(" ");
            if(partes.length ==2 && partes[0].equals("java") && partes[1].equals("myFileSystem")){
                filename = "miDiscoDuro.fs";
                break;
            }
            else if (partes.length ==3 && partes[0].equals("java") && partes[1].equals("myFileSystem")){
                filename=partes[2];
                break;
            }else{
                System.out.println("try with:");
                System.out.println("java myFileSystem");
                System.out.println("java myFileSystem <filename>");
                
            } 
        }
        startShell(filename);

    }

    public void startShell(String filename){
        while(true){
            System.out.print(fs.getCurrentUser()+"@myFS: ");
            String input = scanner.nextLine().trim();
            if(input.equals("")) continue;
            if(input.equals("exit")) break;
            handleCommand(input,filename);
        }
        scanner.close();
    }
    public void handleCommand(String input,String filename ){
        String [] partes = input.split(" ");
        switch(partes[0]){
            
            case "format":
                handleFormat(partes,filename);
                break;
            case "useradd":
                handleUserAdd(partes);
                break;
            case "passwd":
                handlePasswd(partes);
                break;
            case "groupadd":
                handleGroupadd(partes);
                break;
            case "usermod":
                handleUsermod(partes);
                break;
            case "su":
                handleSu(partes);
                break;
            case "whoami":
                System.out.println(fs.whoami());
                break;
            case "pwd":
                System.out.println(fs.pwd());
                break;
            case "mkdir":
                handleMkdir(partes);
                break;
            case "rm":
                handleRM(partes);
                break;
            case "mv":
                handleMV(partes);
                break;
            case "ls": 
                fs.ls(partes.length == 3);
                break;
            case "clear":
                clearScreen();
                break;
            case "cd":
                handleCD(partes);
                break;
            case "whereis":
                handleWhereIs(partes);
                break;
            case "ln":
                handleLn(partes);
                break;
            case "touch":
                handleTouch(partes);
                break;
            case "cat":
                handleCat(partes);
                break;
            case "chown":
                handleChown(partes);
                break;
            case "chgrp": 
                handleChgrp(partes);
                break;
            case "chmod":
                handleChmod(partes);
                break;
            case "openFile":
                handleOpenFile(partes);
                break;
            case "closeFile":
                handleCloseFile(partes);
                break; 
            case "viewFCB": //<
                handleViewFCB(partes);
                break;
            case "infoFS":
                fs.infoFS();
                break;
            case "note":
                handleNote(partes);
                break;

            default:
                System.out.println("Error: unrecognized command.");
        }
    }
    public void handleChmod(String[] partes){
        if(partes.length !=2){
            System.out.println("Error: unrecognized command, try: chmod <number> <filename>");
            return;
        } 
        if(partes[0].length() !=2){
            System.out.println("Error: invalid number, the length of the number must de 2");
            return;
        }
        if(partes[1].charAt(0)<0 || partes[1].charAt(0)>7 || partes[1].charAt(1)<0 || partes[1].charAt(1)>7  ){
            System.out.println("Error: the permits must be between 0 - 7");
        }
        fs.chmod(partes[1].charAt(0), partes[1].charAt(1), partes[2]);
    }
    public void handleChgrp(String[] partes){
        if(partes.length >4){
            System.out.println("Error: unrecognized command, try: chgrp groupname filename <optional:-R> <filename or directory>");
            return;
        }
        if(partes[1].toLowerCase().equals("-r")){
            fs.chgrp(partes[1],partes[2],partes[3]);
        }else{
            fs.chgrp(partes[1],"",partes[3]);
        }
        
    }
    public void handleViewFCB(String[] partes){
        if(partes.length != 2){
            System.out.println("Error: unrecognized command, try: viewFCB <filename>");
            return;
        }
        fs.viewFCB(partes[1]);
    }
    public void handleCat(String[] partes){
        try {
            if(partes.length !=2){
                System.out.println("Error: unrecognized command, try: cat <filename>");
                return;
            }
            System.out.println(fs.readFile(partes[1]));
            
        } catch (Exception ex) {
            System.getLogger(Controller.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }
    public void handleGroupadd(String[] partes){
        if(partes.length !=2){
            System.out.println("Error: unrecognized command, try: group <groupname>");
            return;
        }
        if(!fs.groupadd(partes[1])){
            System.out.println("Error: the group already exists");
            return;
        }

    }
    public void handleUsermod(String[] partes){
        if(partes.length !=3){
            System.out.println("Error: unrecognized command, try: usermod <groupname>");
            return;
        }
        if(!fs.usermod(partes[1],partes[2])){
            System.out.println("Error: the group or username does not exists");
            return;
        }

    }
    public void handleMkdir(String[] partes){
        if(partes.length <2){
            System.out.println("Error: unrecognized command, try: mkdir <name>");
            return;
        }
        
        for(int i = 1;i<partes.length;i++){
            fs.mkdir(partes[i]);
        }
    }
    public void handleTouch(String[] partes){
        if(partes.length !=2){
            System.out.println("Error: unrecognized command, try: touch <filename>");
            return;
        }
        
        fs.touch(partes[1]);
    }
    public void handleLn(String[] partes){
        if(partes.length == 3){
            System.out.println("Error: unrecognized command, try: ln <filename> <path>");
            return;
        }
        fs.ln(partes[1], partes[2]);
    }
    
    public void handleChown(String []partes){
        boolean r = "-r".equals(partes[1].toLowerCase());
        if((r && partes.length != 4)||(!r && partes.length != 3)){
            System.out.println("Error: unrecognized command, try: chown <username> <filename/directory>");
            return;
        }
        fs.chown(r? partes[2]:partes[1], r? partes[3]:partes[2], r);   
    }
    public void handleCD(String[] partes){
        if(partes.length >2){
            System.out.println("Error: unrecognized command, try: touch <filename>");
            return;
        }
        
        fs.cd(partes[1]);
    }
    public void handleRM(String [] partes){
       boolean recursive = partes[1].toLowerCase().equals("-r");
       if((recursive && partes.length != 3)|| (!recursive && partes.length >2)){
           System.out.println("Error: unrecognized command, try: rm <filename/directory> or rm -r <filename/directory>");
           return;
       }
       if (recursive){fs.rm(partes[2], true);
       }else{fs.rm(partes[1], false);}
    }
    
    public void handleMV(String [] partes){
       if(partes.length >3){
            System.out.println("Error: unrecognized command, try: mv <filename> <directory>");
            return;
        }
       fs.mv(partes[1], partes[2]);
   }
    
    public void handleWhereIs(String [] partes){
        if(partes.length >2){
            System.out.println("Error: unrecognized command, try: whereis <filename>");
            return;
        }
        System.out.println(fs.whereIs(partes[1]));
    }
    
    public void handleOpenFile(String[] partes){
        if(partes.length != 2){
            System.out.println("Error: unrecognized command, try: openFile <filename>");
            return;
        }fs.openFile(partes[1]);
    }
    public void handleCloseFile(String[] partes){
        if(partes.length != 2){
            System.out.println("Error: unrecognized command, try: closeFile <filename>");
            return;
        }fs.closeFile(partes[1]);
    }
    public void handleFormat(String[] partes,String filename ){
        try{
            if(partes.length != 2){
                System.out.println("Error: unrecognized command, try: format <number>");
                return;
            }
            Integer tamMB = validarInt(partes[1]);
            
            if(tamMB == null || tamMB <= 0) {
                System.out.println("Error: the size must be a positive number");
                return;
            }
            
            System.out.print("type the block size: ");
            Integer tamBlock = validarInt(scanner.nextLine());
            if(tamBlock == null || tamBlock <= 0) {
                System.out.println("Error: the size must be a positive number");
                return;
            }
            
            System.out.print("password: ");
            String password1 = scanner.nextLine();
            System.out.print("confirma password: ");
            String password2 = scanner.nextLine();
            
            if(!password1.equals(password2)){
                System.out.println("Error: passwords do not match.");
                return;
            }
            fs.format(tamMB,password2,tamBlock,filename);
            
            
        }catch (Exception ex){
            System.getLogger(Controller.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    
    public void clearScreen() {
        
        //System.out.print("\033[H\033[2J");
        //System.out.flush();
        for (int i = 0; i < 60; i++) {
            System.out.println();
        }
        /*try{
            new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
        }catch(Exception E){
            System.out.println(E);
        }*/
    }
    public String editorTxt() {
        List<String> lines = new ArrayList<>();
        

        while (true) {
            String line = scanner.nextLine();
            if (line.equals(":q")) {
                break;
            }
            lines.add(line);
        }

        // Construir contenido final
        StringBuilder finalTxt = new StringBuilder();
        for (String l : lines) finalTxt.append(l).append("\n");

        return finalTxt.toString();
    }

    public void handleNote(String[] partes){
        if(partes.length != 2){
            System.out.println("Error: unrecognized command, try: note <filename>");
            return;
        }
        if(!fs.fileExist(partes[1])){
            System.out.println("Erro: file not found");
            return;
        }
        System.out.println("-------------------------------------");
        System.out.println("filename:"+partes[1]+"          Exit = :q");
        System.out.println("-------------------------------------");
        String content = editorTxt();
        System.out.print("Save changes?(y/n)");
        String resp = scanner.nextLine().trim().toLowerCase();
        if(resp.equals("y")){
            try {
                fs.writeFile(partes[1],content);
            } catch (Exception ex) {
                System.getLogger(Controller.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            System.out.println("Fie saved"); 
        }else{System.out.println("Dicarded changes");}
        
    }

    public void handleUserAdd(String[] partes){
        if(partes.length != 2){
            System.out.println("Error: unrecognized command, try: useradd <username>");
            return;
        }
        if(fs.userExists(partes[1])){
            System.out.println("Error: the user already exists.");
            return;
        }
        System.out.print("name: ");
        String nombre = scanner.nextLine();
        System.out.print("last name: ");
        String apellidos = scanner.nextLine();
        System.out.print("password: ");
        String password1 = scanner.nextLine();
        System.out.print("confirma password: ");
        String password2 = scanner.nextLine();
        
        if(!password1.equals(password2)){
            System.out.println("Error: passwords do not match.");
            return;
        }
        fs.useradd(partes[1],nombre+" "+apellidos, password1);
        
        System.out.println("User created successfully.");
        
        
    }
    public void handlePasswd(String[] partes){
        if(partes.length != 2){
            System.out.println("Error: unrecognized command, try: passwd <username>");
            return;
        }
        System.out.print("password: ");
        String password1 = scanner.nextLine();
        System.out.print("confirma password: ");
        String password2 = scanner.nextLine();
        
        if(!password1.equals(password2)){
            System.out.println("Error: passwords do not match.");
            return;
        }
        fs.passwd(partes[1], password2);
        
    }
    public void handleSu(String[] partes){
        if(partes.length >2){
            System.out.println("Error: unrecognized command, try: su <username>");
            return;
        }
        if(partes.length == 1){
            fs.su("root");
        }else{
            System.out.print("password: ");
            String password1 = scanner.nextLine();
            if(!fs.changeUser(partes[1],password1)){
                System.out.println("Error: passwords do not match.");
                return;
            }
            
        }
        
    }
    /*
    =================================================
    Auxiliares
    =================================================
    
    */
    private Integer validarInt(String s){
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
