/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guillermo Veintemilla
 * Clase Servidor que crea nuestro servidor en el puerto que le indicamos
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ServerSocket servidor;
        Socket conexion;

        try {
            servidor = new ServerSocket(4332);
            while (true) {
                conexion = servidor.accept();
                conexion.setSoLinger(true, 10);
                MiHilo miHilo = new MiHilo(conexion);
                miHilo.run();
                miHilo.conexion.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
