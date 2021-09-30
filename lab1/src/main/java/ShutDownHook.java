import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class ShutDownHook extends Thread {
    private final MulticastSocket socket;
    private final InetAddress address;
    private final int port;
    public ShutDownHook(MulticastSocket socket, InetAddress address, int port) {
        this.socket = socket;
        this.address = address;
        this.port = port;
    }
    @Override
    public void run() {
        try {
            socket.leaveGroup(new InetSocketAddress(address, port), NetworkInterface.getByInetAddress(address));
//            System.out.println("Socket left the group");
            socket.close();
//            System.out.println("Socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
