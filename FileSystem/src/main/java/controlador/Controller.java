
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
        while(true){
            
            String input = scanner.nextLine().trim();
            String [] partes = input.split(" ");
            if(partes.length ==1){
                System.out.print("type the block size: ");
                int tamBlock = Integer.parseInt(scanner.nextLine());
                
                //fs.format2(100,tamBlock,"rootpass","miDiscoDuro.fs");
                break;
            }
            else if (partes.length ==2){
                System.out.print("type the block size: ");
                int tamBlock = Integer.parseInt(scanner.nextLine());
                
                //fs.format2(100,tamBlock,"rootpass",partes[2]);
                break;
            }else{
                System.out.println("try with:");
                System.out.println("java myFileSystem");
                System.out.println("java myFileSystem <filename>");
                
            } 
        }
        startShell();

    }

    public void startShell(){
        while(true){
            System.out.print(fs.getCurrentUser()+"@myFS: ");
            String input = scanner.nextLine().trim();
            if(input.equals("")) continue;
            if(input.equals("exit")) break;
            handleCommand(input);
        }
        scanner.close();
    }
    public void handleCommand(String input){
        String [] partes = input.split(" ");
        switch(partes[0]){
            case "note":
                handleNote(input);
                break;
            case "format":
                handleFormat(input);//incompleto
                break;
            case "useradd":
                handleUserAdd(input);
                break;
            case "passwd":
                handlePasswd(input);
                break;
            case "whoami":
                System.out.println(fs.whoami());
                break;
            case "su":
                handleSu(input);
                break;
            case "clear":
                clearScreen();
                break;
                
                
            default:
                System.out.println("Error: unrecognized command.");
        }
   
    }
    public void handleFormat(String input){
        String [] partes = input.split(" ");
        if(partes.length != 2){
            System.out.println("Error: unrecognized command, try: format <number>");
            return;
        }
        int tamMB;
        try{
            tamMB = Integer.parseInt(partes[1]);
        }catch (NumberFormatException e){
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
        fs.passwd("root",password1);//quitar y llamar al format 
        
    }
    //no me gusta pero agregar for de ""
    public void clearScreen() {
        
        //System.out.print("\033[H\033[2J");
        //System.out.flush();
        try{
            new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
        }catch(Exception E){
            System.out.println(E);
        }
    }
    public String editorTxt() {
        List<String> lines = new ArrayList<>();

        System.out.println("Exit = :q");

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

    public void handleNote(String input){
        String [] partes = input.split(" ");
        /*if(partes.length != 2){
            System.out.println("Error: unrecognized command, try: note <filename>");
            return;
        }*/
         String content = editorTxt();
         System.out.print("Save changes?(y/n)");
         String resp = scanner.nextLine().trim().toLowerCase();
         if(resp.equals("y")){
             fs.writeFile(partes[1],content);
             System.out.println("Fie saved"); 
         }else{System.out.println("Dicarded changes");}
        
    }

    public void handleUserAdd(String input){
        String [] partes = input.split(" ");
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
    public void handlePasswd(String input){
        String [] partes = input.split(" ");
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
    public void handleSu(String input){
        String [] partes = input.split(" ");
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


}
