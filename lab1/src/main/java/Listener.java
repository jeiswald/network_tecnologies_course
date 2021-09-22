import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;

public class Listener extends Thread {
    private InetAddress address;
    private DatagramSocket socket;
    private int port;
    private List<InetAddress> copiesOnline;

    public Listener(InetAddress ip, MulticastSocket socket, int port) {
        address = ip;
        this.socket = socket;
        this.port = port;
//        copiesOnline = new
    }

    @Override
    public void run() {
        byte[] buf = new byte[256];
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                ////
                copiesOnline.add(packet.getAddress());
                ////
            } catch (IOException e) {
                break;
            }
        }
    }
}
