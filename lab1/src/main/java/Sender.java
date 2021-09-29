import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.Map;

public class Sender extends Thread {
    private InetAddress address;
    private MulticastSocket socket;
    private int port;
    private final Map<InetAddress, Date> copiesOnline;

    public Sender(InetAddress ip, MulticastSocket socket, int port, Map<InetAddress, Date> copiesOnline) {
        address = ip;
        this.socket = socket;
        this.port = port;
        this.copiesOnline = copiesOnline;
    }

    @Override
    public void run() {
        byte[] message = "I'm here".getBytes();
        DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
        while (!isInterrupted()) {
            try {
                Date date = new Date();
                boolean changed = false;
                synchronized (copiesOnline) {
                    for (var entry : copiesOnline.entrySet()) {
                        if (date.getTime() - entry.getValue().getTime() > 10000) {
                            copiesOnline.remove(entry.getKey());
                            changed = true;
                        }
                    }
                    if (changed) {
                        System.out.println("Online copies:");
                        for (var entry : copiesOnline.entrySet()) {
                            System.out.println(entry.getKey());
                        }
                    }
                }

                socket.send(packet);
                sleep(2000);
                //System.out.println("sent");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
