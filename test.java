import java.util.Scanner;

public class test {
    static class Worker extends Thread {
        public void run(){
            while (true) {
                System.out.println("Chigan tu pito");    
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();
        Scanner scann = new Scanner(System.in);
        while(true){
            System.out.println("Ingrese a tu hermana porfis");
            String newString = scann.nextLine();
            System.out.println(newString + "Tu JEFAAAAAAAAAAAAAAAAAA");
        }
    }
}
