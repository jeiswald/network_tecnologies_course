import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.*;

public class Sender extends Thread {
    private InetAddress address;
    private DatagramSocket socket;
    private int port;
    public Sender(InetAddress ip, MulticastSocket socket, int port) {
        address = ip;
        this.socket = socket;
        this.port = port;
    }

    @Override
    public void run() {
        byte[] message = "I'm here".getBytes();
        while(true) {
            try {
                DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
                socket.send(packet);
                System.out.println("sent");
            } catch (IOException e) {
                break;
            }
        }
    }
}
