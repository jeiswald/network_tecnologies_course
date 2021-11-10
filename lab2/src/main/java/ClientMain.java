import java.io.IOException;
import java.net.InetAddress;

public class ClientMain {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Not enough args");
            return;
        }
        try {
            String path = args[0];
            InetAddress address = InetAddress.getByName(args[1]);
            int port = Integer.parseInt(args[2]);
            Client client = new Client(address, port);
            client.run(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
