package controlador;

import vista.VistaPrincipal;

public class Main {

    public static void main(String[] args) {
        
        VistaPrincipal vista = new VistaPrincipal();
        ControlVistaPrincipal control = new ControlVistaPrincipal(vista);
        
        control.functionality();
        
    }

}
