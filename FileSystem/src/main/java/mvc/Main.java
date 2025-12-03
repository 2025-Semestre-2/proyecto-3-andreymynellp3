
package mvc;

import controlador.Controller;

public class Main {
    public static void main(String [] args){
        try {
            Controller controller = new Controller();
            controller.ejecutar();
        } catch (Exception ex) {
            System.getLogger(Main.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}
