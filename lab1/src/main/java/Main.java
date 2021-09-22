import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) {
        InetAddress address;
        int port;
        MulticastSocket socket;
        try {
            address = InetAddress.getByName(args[0]);
            port = parseInt(args[1]);
            socket = new MulticastSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (socket) {
            socket.joinGroup(address);
            Listener listener = new Listener(address, socket, port);
            listener.start();
            Sender sender = new Sender(address, socket, port);
            sender.start();
            if (!listener.isAlive() && !sender.isAlive()) {
                socket.leaveGroup(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            socket.close();
        }
    }
}
