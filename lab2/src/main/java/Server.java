import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

public class Server extends Thread {
    ServerSocket server;

    public Server(int port) throws IOException {
        server = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            Timer timer = new Timer();
            while (true) {
                Socket client = server.accept();
                System.out.println("Accepted new client");
                FileReceiver receiver = new FileReceiver(client, timer);
                receiver.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
