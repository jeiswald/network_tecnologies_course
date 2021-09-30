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
        MulticastSocket socket;
        Map<InetAddress, Date> copiesOnline = new HashMap<>();

        try {
            address = InetAddress.getByName(args[0]);
            port = parseInt(args[1]);
            socket = new MulticastSocket(port);
            socket.joinGroup(new InetSocketAddress(address, port), NetworkInterface.getByInetAddress(address));
            socket.isClosed();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Runtime current = Runtime.getRuntime();
        current.addShutdownHook(new ShutDownHook(socket, address, port));

        Listener listener = new Listener(socket, copiesOnline);
        Sender sender = new Sender(address, socket, port, copiesOnline);
        listener.start();
        sender.start();
    }
}
