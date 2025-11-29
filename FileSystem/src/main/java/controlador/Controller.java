
package controlador;

import static controlador.Command.*;
import java.util.Scanner;
import controlador.FileSystem;


public class Controller {
    private FileSystem fs;
    private Command cm;
    
    public Controller(){
        this.fs = new FileSystem(4050,"Link");
        this.cm = new Command();
    }
    
    public void startShell(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print(cm.getUserManager().getCurrentUser()+"@myFS: ");
            String input = scanner.nextLine().trim();
            if(input.equals("")) continue;
            if(input.equals("exit")) break;
            cm.handleCommand(input);
        }
        scanner.close();
    }


}
