import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class MultiplicacionMatrizDistribuida {

    static int N = 1000;
    static int [][] A = new int[N][N];
    static int [][] B = new int[N][N];
    static int [][] C = new int[N][N];
    static int [][] C1,C2,C3,C4 = new int[N/2][N/2];
    static long checksum = 0;
    static Object lock = new Object();

    static class Worker extends Thread{
        Socket conexion;
        Worker(Socket conexion){
            this.conexion = conexion;
        }

        public void run(){
            
            try {
                System.out.println("Iniciando conexion..");

                ObjectInputStream objetoEntrada = new ObjectInputStream(conexion.getInputStream());     
                int nodo;
                nodo = (int)objetoEntrada.readObject();
                
                
                ObjectOutputStream objetoSalida = new ObjectOutputStream(conexion.getOutputStream());
                objetoSalida.writeObject("Conectado con servidor....");

                switch (nodo) {
                    case 1:
                        
                        //Enviar matriz A1 al nodo 1
                        objetoSalida.writeObject((int[][])Arrays.copyOfRange(A, 0, N/2));//A1
                        objetoSalida.writeObject((int[][])Arrays.copyOfRange(B, 0, N/2));//B1
                        //objetoSalida.close();
                        C1 = (int[][])objetoEntrada.readObject();
                        break;
                    case 2:
                        
                        objetoSalida.writeObject((int[][])Arrays.copyOfRange(A, 0, N/2));//A1
                        objetoSalida.writeObject((int[][])Arrays.copyOfRange(B, N/2, N));//B2
                        //objetoSalida.close();
                        C2 = (int[][])objetoEntrada.readObject();
                        break;
                    case 3:
                        
                        objetoSalida.writeObject((int[][])Arrays.copyOfRange(A, N/2, N));//A2
                        objetoSalida.writeObject((int[][])Arrays.copyOfRange(B, 0, N/2));//B1
                        //objetoSalida.close();
                        C3 = (int[][])objetoEntrada.readObject();
                        break;
                    case 4:
                        
                        objetoSalida.writeObject((int[][])Arrays.copyOfRange(A, N/2, N));//A2
                        objetoSalida.writeObject((int[][])Arrays.copyOfRange(B, N/2, N));//B2
                        //objetoSalida.close();
                        C4 = (int[][])objetoEntrada.readObject();
                        break;
                
                }

                objetoEntrada.close();
                objetoSalida.close();
                conexion.close();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            
        }
    }

    public static void main(String[] args) throws InterruptedException {
        
        if(args.length != 1){

            System.err.println("Uso");
            System.err.println("java MultiplicaciónMatrizDistribuida <nodo>");

        }
        else{

            int nodo = Integer.valueOf(args[0]);
            
            if (nodo==0) {
                //1. Inicializar matrices  A y B
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        A[i][j] = 2*i+3*j;
                        B[i][j] = 2*i-3*j;
                        //C[i][j] = 0;
                    }
                }
                //2. Transponer la matrices B
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < i; j++) {
                        int x = B[i][j];
                        B[i][j] = B[j][i];
                        B[j][i] = x;
                    }
                }

                
                
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
                    Thread.sleep(2000);

                    for (i =0; i < N/2;i++) {
                        for (int j = 0; j < N/2; j++) {
                            C[i][j] = C1[i][j];
                            C[i][j+N/2] = C2[i][j];
                            C[i+N/2][j] = C3[i][j];
                            C[i+N/2][j+N/2] = C4[i][j];
                        }
                    }

                    for ( i = 0; i < N; i++){
                        for (int j = 0; j < N; j++) {
                            checksum+=C[i][j];
                        }
                    }

                    System.out.println("Checksum: "+checksum);

                    if(N==4){
                        System.out.println("Matriz A");
                        for (int j = 0; j < N; j++) {
                            for (int k = 0; k < N; k++) {
                                System.out.print(A[j][k]+" ");
                            }
                            System.out.println();
                        }
                        System.out.println("Matriz B transpuesta");
                        for (int j = 0; j < N; j++) {
                            for (int k = 0; k < N; k++) {
                                System.out.print(B[j][k]+" ");
                            }
                            System.out.println();
                        }
                        System.out.println("Matriz C");
                        for (int j = 0; j < N; j++) {
                            for (int k = 0; k < N; k++) {
                                System.out.print(C[j][k]+" ");
                            }
                            System.out.println();
                        }
                    }

                }catch (Exception e) {
                    System.err.println(e.getMessage());
                }   
            }else{
                System.out.println("Nodo "+nodo + " iniciando conexion...");
                Socket conexion = null;
            
                while(true){
                    try {
                        
                        conexion = new Socket("localhost", 5000);
                        System.out.println("Conexionexitosa...");
                        ObjectOutputStream objetoSalida = new ObjectOutputStream(conexion.getOutputStream());
                        objetoSalida.writeObject(nodo);
                        int[][] ATemp, BTemp, CTemp;

                        ObjectInputStream objetoEntrada = new ObjectInputStream(conexion.getInputStream());
                        System.out.println((String)objetoEntrada.readObject());
                        ATemp = (int[][])objetoEntrada.readObject();
                        BTemp = (int[][])objetoEntrada.readObject();
                        
                        CTemp = new int[N/2][N/2];

                        for (int i = 0; i < N/2; i++) {
                            for (int j = 0; j < N/2; j++) {
                                for (int k = 0; k < N; k++) {
                                    CTemp[i][j] += ATemp[i][k]*BTemp[j][k];    
                                }
                            }
                        }
                        
                        objetoSalida.writeObject(CTemp);
                        System.out.println("Calculo completado");
                        objetoSalida.close();
                        objetoEntrada.close();
                        conexion.close();
                        break;
                    } catch (Exception e) {
                        Thread.sleep(100);

                    }
                }
    
            }
        }    
    }
    
}
