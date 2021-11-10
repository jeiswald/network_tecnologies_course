import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    InetAddress address;
    int port;
    private final int FILENAME_MAX_SIZE = 4096;
    private final long FILE_MAX_SIZE = 1048576000;
    private final int BUF_SIZE = 500;

    public Client(InetAddress address, int port) throws IOException {
        this.address = address;
        this.port = port;
    }

    public void run(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        try (Socket socket = new Socket(address, port);
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
             DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
             FileInputStream fileInputStream = new FileInputStream(file);
        ) {
            byte[] buf = new byte[BUF_SIZE];
            int readBytes = 0;

            byte[] name = file.getName().getBytes();
            if (name.length > FILENAME_MAX_SIZE) {
                throw new IOException("File name is too long");
            }
            long size = file.length();
            System.out.println("file size: " + size);
            if (size / 1024 > FILE_MAX_SIZE) {
                throw new IOException("File size is too big");
            }
            out.writeInt(name.length);
            out.write(name, 0, name.length);
            out.writeLong(size);
            while ((readBytes = fileInputStream.read(buf)) > 0) {
                out.write(buf, 0, readBytes);
            }
            out.flush();

            boolean isTransmissionSuccessful = in.readBoolean();
            if (isTransmissionSuccessful) {
                System.out.println("Transmission successful");
            } else {
                System.out.println("Transmission has failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
