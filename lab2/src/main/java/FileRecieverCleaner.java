//import java.io.IOException;
//import java.net.*;
//
//public class FileReciverCleaner extends Thread {
//    private final Socket socket;
//
//    public FileReciverCleaner(Socket socket) {
//        this.socket = socket;
//    }
//    @Override
//    public void run() {
//        try {
//            socket.close();
////            System.out.println("Socket closed");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
