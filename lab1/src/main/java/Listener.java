import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.*;

public class Listener extends Thread {
    //    private InetAddress address;
    private MulticastSocket socket;
    //    private int port;
    private final Map<InetAddress, Date> copiesOnline;

    public Listener(MulticastSocket socket, Map<InetAddress, Date> copiesOnline) {
//        address = ip;
        this.socket = socket;
//        this.port = port;
        this.copiesOnline = copiesOnline;
    }

    @Override
    public void run() {
        byte[] buf = new byte[256];
        while (!isInterrupted()) {
            try {
//                System.out.println("before receive");
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
//                System.out.println(received);
//                System.out.println("after receive");

                synchronized (copiesOnline) {
                    if (!copiesOnline.containsKey(packet.getAddress())) {
                        copiesOnline.put(packet.getAddress(), new Date());
                        System.out.println("Online copies:");
                        for (var entry : copiesOnline.entrySet()) {
                            System.out.println(entry.getKey());
                        }
                    } else {
                        copiesOnline.put(packet.getAddress(), new Date());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
