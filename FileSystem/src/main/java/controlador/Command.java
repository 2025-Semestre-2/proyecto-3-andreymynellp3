
package controlador;

import java.util.Scanner;


public class Command {
    private Scanner scanner;
    private UserManager um;
    
    public Command(){
        scanner= new Scanner(System.in);
        this.um = new UserManager();
    }
    
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
                handleWhoami(input);
                break;
                
                
            default:
                System.out.println("Comando no reconocido.");
        }
   
    }
    public void handleUserAdd(String input){
        String [] partes = input.split(" ");
        if(partes.length != 2){
            System.out.println("Comando no reconido, prueba con: useradd <username>");
            return;
        }
        if(um.userExists(partes[1])){
            System.out.println("Error: el usuario ya exite");
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
            System.out.println("Error: Las contraseñas no coinciden.");
            return;
        }
        um.useradd(nombre+" "+apellidos, partes[1], password1);
        System.out.println("Usuario creado con exito.");
        
        
    }
    public void handlePasswd(String input){
        String [] partes = input.split(" ");
        if(partes.length != 2){
            System.out.println("Comando no reconido, prueba con: passwd <username>");
            return;
        }
        System.out.print("password: ");
        String password1 = scanner.nextLine();
        System.out.print("confirma password: ");
        String password2 = scanner.nextLine();
        
        if(!password1.equals(password2)){
            System.out.println("Error: Las contraseñas no coinciden.");
            return;
        }
        um.passwd(partes[1], password2);
        
    }
    public void handleWhoami(String input){
        
        
    }
    public UserManager getUserManager() {return um;}
    
}
