import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class Sender extends Thread {
    private final InetAddress address;
    private final MulticastSocket socket;
    private final int port;
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
        while (true) {
            try {
                Date date = new Date();
                boolean changed = false;
                synchronized (copiesOnline) {
                    Iterator<Map.Entry<InetAddress, Date>> iterator = copiesOnline.entrySet().iterator();
                    while(iterator.hasNext()) {
                        Map.Entry<InetAddress, Date> pair = iterator.next();
                        if (date.getTime() - pair.getValue().getTime() > 10000) {
                            copiesOnline.remove(pair.getKey());
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
