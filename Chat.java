import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class Chat {
    static int puerto = 50000;
    static String ip = "230.0.0.0";
    static class Worker extends Thread{
        
        public void run(){
            try {
                int  longitud_mensaje = 1024;
                MulticastSocket socket = new MulticastSocket(puerto);
                socket.joinGroup(InetAddress.getByName(ip));
                while(true){
                    byte[] mensajeRecibido =  recibe_mensaje_multicast(socket, longitud_mensaje);
                    System.out.println("NIGGA");
                    if (mensajeRecibido != null) {
                        System.out.println(new String(mensajeRecibido));        
                    }            
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
    public static void main(String[] args) throws IOException {
        Worker w = new Worker();
        w.start();
       
        String nombre = args[0];

        Scanner scann = new Scanner(System.in);

        while(true){
            System.out.println("Ingrese el mensaje a enviar:");
            String message = nombre + ":" + scann.nextLine();
            envia_mensaje_multicast(message.getBytes(), ip, puerto);
        }

    }
    static void envia_mensaje_multicast(byte[] buffer, String ip, int puerto) {
        try {
            DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket( buffer, buffer.length, InetAddress.getByName(ip), puerto));
        Thread.sleep(100);
        socket.close();    
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        
    }
    static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException{
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }
}
