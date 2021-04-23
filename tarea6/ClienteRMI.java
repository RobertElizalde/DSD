import java.rmi.Naming;

public class ClienteRMI {

    static int N = 8;
    public static void main(String args[]) throws Exception {
        // en este caso el objeto remoto se llama "prueba", notar que se utiliza el
        // puerto default 1099
        String url = "rmi://localhost/multimatriz";
        String url2 = "rmi://localhost/multimatriz";

        // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
        InterfaceRMI r = (InterfaceRMI) Naming.lookup(url);
        InterfaceRMI r2 = (InterfaceRMI) Naming.lookup(url2);

        //Inicializar A y B
        int [][] A = new int[N][N];
        int [][] B = new int[N][N];
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = i+3*j;
                B[i][j] = i-3*j;
            }
        }
        
        //Transpuesta de B
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                int x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }

        int[][] A1 = separa_matriz(A,0);
        int[][] A2 = separa_matriz(A,N/2);
        int[][] B1 = separa_matriz(B,0);        
        int[][] B2 = separa_matriz(B,N/2);

        int[][] C1 = r.multiplica_matrices(A1, B1);
        int[][] C2 = r.multiplica_matrices(A1, B2);
        int[][] C3 = r2.multiplica_matrices(A2, B1);
        int[][] C4 = r2.multiplica_matrices(A2, B2);

        int[][] C = new int[N][N];
        acomoda_matriz(C, C1, 0, 0);
        acomoda_matriz(C, C2, 0, N/2);
        acomoda_matriz(C, C3, N/2, 0);
        acomoda_matriz(C, C4, N/2, N/2);

        if(N==8){
            System.out.println("Matriz A");
            imprimir_matriz(A);
            System.out.println("Matriz B");
            imprimir_matriz(B);
            System.out.println("Matriz C");
            imprimir_matriz(C);
            System.out.println();
        }

        System.out.println("checksum=" + checksum(C));
    }

    static int[][] separa_matriz(int[][] A, int inicio){
        int[][] M = new int[N/2][N];
        for (int i = 0; i < N/2; i++) {
            for (int j = 0; j < N; j++) {
                M[i][j] = A[i + inicio][j];
            }
        }
        return M;
    }

    static void acomoda_matriz(int[][] C, int[][] A, int renglon, int columna){
        for (int i = 0; i < N/2; i++) {
            for (int j = 0; j < N/2; j++) {
                C[i + renglon][j + columna] = A [i][j];
            }
        }
    }
    static long checksum(int[][] X){
        long checksumT = 0;

        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++) {
                checksumT+=X[i][j];
            }
        }
        return checksumT;
    }
    static void imprimir_matriz(int[][] X){
        for (int j = 0; j < N; j++) {
            for (int k = 0; k < N; k++) {
                System.out.print(X[j][k]+" ");
            }
            System.out.println();
        }
    }
}