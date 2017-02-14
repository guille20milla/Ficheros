/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Cliente.Cliente;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Guillermo Veintemilla
 * Clase MiHilo que se ejecuta por cada cliente que se conecta
 */
public class MiHilo extends Thread {

    private int opcion;
    ObjectOutputStream output;
    ObjectInputStream input;
    Socket conexion;
    private static final String repositorioServidor = "Repositorio\\Servidor\\";

    /**
     * Constructor del hilo
     * @param c 
     */
    public MiHilo(Socket c) {
        this.conexion = c;
    }

    /**
     * Metodo run del hilo
     */
    @Override
    public void run() {
        try {
            opcion = 0;
            output = new ObjectOutputStream(conexion.getOutputStream());
            input = new ObjectInputStream(conexion.getInputStream());
            opcion = (int) input.readObject();
            switch (opcion) {
                case 1:
                    File repositorio = new File(this.repositorioServidor);
                    File[] archivos;
                    archivos = repositorio.listFiles();
                    output.writeObject(archivos);
                    break;
                case 2:
                    String archivo = (String) input.readObject();
                    File download = new File(archivo);
                    output.writeObject(download);
                    break;
                case 3:
                    String rutaDestino = (String) input.readObject();
                    try {
                        File archivoOrigen = (File) input.readObject();
                        File archivoDestino = new File(rutaDestino);
                        subirArchivo(archivoOrigen, archivoDestino);
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, "Selecciona que quieres descargar", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Selecciona que quieres descargar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para subir archivos al servidor.
     *
     * @param archivoOrigen archivo que se recibe del cliente.
     * @param archivoDestino archivo donde va a ir dicho archivo.
     */
    public void subirArchivo(File archivoOrigen, File archivoDestino) {
        InputStream inOrigen = null;
        OutputStream out = null;
        File[] archivos;
        File archivo;
        try {
            if (archivoOrigen.isDirectory()) {
                archivo = new File(archivoDestino.getAbsolutePath() + "\\" + archivoOrigen.getName());
                if (!archivo.exists()) {
                    archivo.mkdir();
                }
                archivos = archivoOrigen.listFiles();
                String ruta = archivo.getAbsolutePath();
                for (int i = 0; i < archivos.length; i++) {
                    subirArchivo(archivos[i], archivo);
                }
            } else {
                archivo = new File(archivoDestino.getAbsolutePath() + "\\" + archivoOrigen.getName());
                inOrigen = new FileInputStream(archivoOrigen);
                out = new FileOutputStream(archivo);
                byte[] buffer = new byte[1024];
                int longitud;
                while ((longitud = inOrigen.read(buffer)) > 0) {
                    out.write(buffer, 0, longitud);
                }

            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Selecciona que quieres descargar", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
