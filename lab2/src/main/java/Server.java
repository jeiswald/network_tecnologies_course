import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server extends Thread {
    ServerSocket server;
    List<FileReceiver> clientList;

    public Server(int port) throws IOException {
        server = new ServerSocket(port);
        clientList = new LinkedList<>();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket client = server.accept();
                System.out.println("Accepted new client");
                FileReceiver receiver = new FileReceiver(client);
                clientList.add(receiver);
                receiver.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
