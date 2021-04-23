import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class SocketTest {
    public static void main(String[] args) throws InterruptedException {
        int nodo = Integer.parseInt(args[0]);

        switch (nodo) {
            case 0:
            ServerSocket server = null;           
                   try{
                        server = new ServerSocket(5000);
                        Socket socket1 = server.accept();
                        //

                        ObjectInputStream ois = new ObjectInputStream(socket1.getInputStream());
                        System.out.println((String)ois.readObject());
                        System.out.println((String)ois.readObject());
                        System.out.println((String)ois.readObject());
                        System.out.println((String)ois.readObject());
                        ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());

                        oos.writeObject("Hola desde el servidor....");
                        
                        System.out.println((String)ois.readObject());
                        oos.writeObject("ADIOS");
                        ois.close();
                        oos.close();
                        socket1.close();
                   }catch(Exception e){
                        System.err.println(e);
                   }             
                break;
        
            case 1:
                   while (true) {
                       try {
                            Socket socket = new Socket("localhost",5000);
                            
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            String saludo;

                            oos.writeObject("Saludos");
                            oos.writeObject("Saludos");
                            oos.writeObject("Saludos");
                            oos.writeObject("Saludos");
                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                            saludo = (String)ois.readObject();
                                
                            
                            System.out.println(saludo);
                            oos.writeObject(saludo+"tambien");
                            saludo = (String)ois.readObject();
                                
                            
                            System.out.println(saludo);
                            oos.close();
                            ois.close();
                            socket.close();
                            break;

                       } catch (Exception e) {
                           Thread.sleep(1000);
                       }
                   }
                break;
        }
    }
}
