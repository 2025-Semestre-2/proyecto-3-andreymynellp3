
package controlador;

import java.util.Scanner;


public class Command {
    private Scanner scanner;
    
    public Command(){
        scanner= new Scanner(System.in);
    }/*
    
    public void handleCommand(String input){
        String [] partes = input.split(" ");
        switch(partes[0]){
            case "useradd":
                handleUserAdd(input);
                break;
            case "passwd":
                handlePasswd(input);
                break;
            case "whoami":
                System.out.println(um.whoami());
                break;
            case "su":
                handleSu(input);
                break;
                
                
            default:
                System.out.println("Error: unrecognized command.");
        }
   
    }
    public void handleUserAdd(String input){
        String [] partes = input.split(" ");
        if(partes.length != 2){
            System.out.println("Error: unrecognized command, try: useradd <username>");
            return;
        }
        if(um.userExists(partes[1])){
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
        um.useradd(nombre+" "+apellidos, partes[1], password1);
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
        um.passwd(partes[1], password2);
        
    }
    public void handleSu(String input){
        String [] partes = input.split(" ");
        if(partes.length >2){
            System.out.println("Error: unrecognized command, try: su <username>");
            return;
        }
        if(partes.length == 1){
            um.su("root");
        }else{
            System.out.print("password: ");
            String password1 = scanner.nextLine();
            if(!um.changeUser(partes[1],password1)){
                System.out.println("Error: passwords do not match.");
                return;
            }
            
        }
        
    }

    public UserManager getUserManager() {return um;}*/
    
}
