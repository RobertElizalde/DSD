import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Token {
    static DataInputStream entrada;
    static DataOutputStream salida;
    static boolean primera_vez = true;
    static String ip;
    static int nodo;
    static int token;
    static int contador = 0;

    static class Worker extends Thread{
        public void run(){
            //Algoritmo 1
            try {
                ServerSocket servidor;
                servidor = new ServerSocket(50000);
                Socket conexion;
                conexion = servidor.accept();    
                entrada = new DataInputStream(conexion.getInputStream());
            } catch (Exception e) {
                //TODO: handle exception
                System.err.println(e);
            }
            
        }
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        if(args.length != 2){
            System.err.println("Se debe pasar parametros el numero del nodo y la IP del siguiente nodo ");
            System.exit(1);
        }
        nodo = Integer.valueOf(args[0]);
        ip = args[1];
        //Algoritmo 2
        Worker w;
        w = new Worker();
        w.start();
        Socket conexion;
        conexion = null;
        while(true){
            try {
                conexion = new Socket(ip, 50000);
                break;
            } catch (Exception e) {
                //TODO: handle exception
                Thread.sleep(500);
            }
        }
        salida = new DataOutputStream(conexion.getOutputStream());
        w.join();
        while(true){
            if (nodo == 0) {
                if(primera_vez){
                    primera_vez = false;
                    token = 1;
                }else{
                    token = entrada.readInt();
                    contador++;
                    System.out.println("Nodo:" + nodo + ", Contador:" + contador + ", Token:" + token);
                }
            }else{
                token = entrada.readInt();
                contador++;
                System.out.println("Nodo:" + nodo + ", Contador:" + contador + ", Token:" + token);
            }
            if(nodo == 0 && contador == 1000)break;

            salida.writeInt(token);
        }
    }
}
