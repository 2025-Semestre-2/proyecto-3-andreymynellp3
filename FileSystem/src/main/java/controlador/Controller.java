
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
        OUTER:
        while (true) {
            String input = scanner.nextLine().trim();
            String [] partes = input.split(" ");
            switch (partes.length) {
                case 2 -> {
                    filename = "miDiscoDuro.fs";
                    break OUTER;
                }
                case 3 -> {
                    filename=partes[2];
                    break OUTER;
                }
                default -> {
                    System.out.println("try with:");
                    System.out.println("java myFileSystem");
                    System.out.println("java myFileSystem <filename>");
                }
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
            case "note":
                handleNote(partes);
                break;
            case "format":
                handleFormat(partes,filename);
                break;
            case "useradd":
                handleUserAdd(partes);
                break;
            case "groupadd"://< Andrey
                break;
            case "passwd":
                handlePasswd(partes);
                break;
            case "su":
                handleSu(partes);
                break;
            case "whoami":
                System.out.println(fs.whoami());
                break;
            case "pwd":
                System.out.println(fs.pwd());
                fs.pwd();
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
            case "ls": //<
                fs.ls(partes.length == 3);
                break;
            case "clear":
                clearScreen();
                break;
            case "cd":
                handleCD(partes);
                break;
            case "whereis": //<
                handleWhereIs(partes);
                break;
            case "ln": //<
                break;
            case "touch":
                handleTouch(partes);
                break;
            case "cat": //<
                break;
            case "chown": //<
                handleChown(partes);
                break;
            case "chgrp": //< Andrey
                break;
            case "chmod": //<
                break;
            case "openFile": //<
                break;
            case "closeFile": //<
                break; 
            case "viewFCB": //<
                break;
            case "infoFS": //<
                break;
            default:
                System.out.println("Error: unrecognized command.");
        }
    }
    public void handleMkdir(String [] partes){
        if(partes.length <2){
            System.out.println("Error: unrecognized command, try: mkdir <name>");
            return;
        }
        
        for(int i = 1;i<partes.length;i++){
            fs.mkdir(partes[i]);
        }
    }
    public void handleTouch(String [] partes){
        if(partes.length >2){
            System.out.println("Error: unrecognized command, try: touch <filename>");
            return;
        }
        
        fs.touch(partes[1]);
    }
    
    public void handleChown(String []partes){
        boolean r = "-r".equals(partes[1].toLowerCase());
        if((r && partes.length != 4)||(!r && partes.length != 3)){
            System.out.println("Error: unrecognized command, try: chown <username> <filename/directory>");
            return;
        }
        fs.chown(r? partes[2]:partes[1], r? partes[3]:partes[2], r);   
    }
    
    public void handleCD(String [] partes){
        if(partes.length >2){
            System.out.println("Error: unrecognized command, try: cd <directory>");
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
    
    public void handleFormat(String [] partes,String filename ){
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

    public void handleNote(String [] partes){
        /*if(partes.length != 2){
            System.out.println("Error: unrecognized command, try: note <filename>");
            return;
        }*/
        //validar si el archivo existe
        System.out.println("-------------------------------------");
        System.out.println("filename:"+partes[1]+"          Exit = :q");
        System.out.println("-------------------------------------");
        String content = editorTxt();
        System.out.print("Save changes?(y/n)");
        String resp = scanner.nextLine().trim().toLowerCase();
        if(resp.equals("y")){
            fs.writeFile(partes[1],content);
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
        fs.useradd(nombre+" "+apellidos, partes[1], password1);
        System.out.println("User created successfully.");
        
        
    }
    public void handlePasswd(String [] partes){
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
    public void handleSu(String [] partes){
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
    public String readPasswordMasked(String prompt) {
        System.out.print(prompt);

        StringBuilder password = new StringBuilder();

        try {
            while (true) {
                int c = System.in.read();

                // ENTER → finalizar
                if (c == '\n' || c == '\r') {
                    System.out.println();
                    break;
                }

                // BACKSPACE → borrar
                if (c == 8 || c == 127) {
                    if (password.length() > 0) {
                        password.deleteCharAt(password.length() - 1);
                        System.out.print("\b \b"); // borra el *
                    }
                    continue;
                }

                // caracter normal
                password.append((char) c);
                System.out.print("*");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return password.toString();
    }



}
