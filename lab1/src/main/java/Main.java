import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Main {

    public static void main(String[] args) {
        InetAddress address;
        int port;
        MulticastSocket receiveSocket;
        //DatagramSocket sendSocket;

        Map<InetAddress, Date> copiesOnline = new HashMap<>();

        try {
            address = InetAddress.getByName(args[0]);
            port = parseInt(args[1]);
            receiveSocket = new MulticastSocket(port);
            //sendSocket = new DatagramSocket();
            receiveSocket.joinGroup(new InetSocketAddress(address, port), NetworkInterface.getByInetAddress(address));
            receiveSocket.isClosed();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            Runtime current = Runtime.getRuntime();
            current.addShutdownHook(new Thread(() -> {
                System.out.println("In clean up code");
                System.out.println("In shutdown hook");
            }));

            Listener listener = new Listener(receiveSocket, copiesOnline);
            Sender sender = new Sender(address, receiveSocket, port, copiesOnline);
            listener.start();
            sender.run();

            if (!listener.isAlive() && !sender.isAlive()) {
                receiveSocket.leaveGroup(new InetSocketAddress(address, port), NetworkInterface.getByInetAddress(address));
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
