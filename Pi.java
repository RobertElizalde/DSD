import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;




public class Pi{

    static Object lock = new Object();
    static double pi = 0;

    static class Worker extends Thread{
        Socket conexion;
        Worker(Socket conexion){
            this.conexion = conexion;
        }

        public void run(){
            //Algoritmo 1
            try {
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());    

                double x;

                x = entrada.readDouble();

                synchronized(lock){
                    pi = x + pi;
                }

                salida.close();
                entrada.close();

                conexion.close();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            
        }
    }


    public static void main(String[] args) throws InterruptedException {
        
        if(args.length != 1){
            System.err.println("Uso");
            System.err.println("java Pi <nodo>");
        }

        int nodo = Integer.valueOf(args[0]);
        if(nodo == 0){
            //Algoritmo 2
            ServerSocket servidor;
            try {
                servidor = new ServerSocket(5000);    

                Worker w[] = new Worker[4];

                int i = 0;

                for(i = 0; i < 4 ; i++){
                    Socket conexion;
                    conexion = servidor.accept();
                    w[i] = new Worker(conexion);
                    w[i].start();
                }
                
                for (i = 0; i < 4; i++) {
                    w[i].join();
                }

                System.out.println(pi);

            }catch (Exception e) {
                System.err.println(e.getMessage());
            }
            
        }else{
            //Algortimo 3
            Socket conexion = null;
            
            while(true){
                try {
                    conexion = new Socket("localhost", 5000);
                    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                    DataInputStream entrada = new DataInputStream(conexion.getInputStream());    
                    double suma = 0;
                    for (int i = 0; i < 10000000; i++) {
                        suma = 4.0/(8*i+2*(nodo-2)+3)+suma;
                    }
                    suma = nodo%2==0?-suma:suma;

                    salida.writeDouble(suma);

                    entrada.close();
                    salida.close();

                    conexion.close();
                    break;
                } catch (Exception e) {
                    Thread.sleep(100);
                }
            }

        }
    }


}