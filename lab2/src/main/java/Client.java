import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private FileInputStream fileInputStream;
    private final int FILENAME_MAX_SIZE = 4096;
    private final long FILE_MAX_SIZE = 1048576000;
    private final int BUF_SIZE = 500;

    public Client(InetAddress address, int port) throws IOException {
        socket = new Socket(address, port);
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public void run(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        fileInputStream = new FileInputStream(file);
        byte[] buf = new byte[BUF_SIZE];
        int readBytes = 0;
        try {
            byte[] name = file.getName().getBytes();
            if (name.length > FILENAME_MAX_SIZE) {
                throw new IOException("File name is too long");
            }
            long size = file.length();
            if (size / 1024 > FILE_MAX_SIZE) {
                throw new IOException("File size is too big");
            }
            System.arraycopy(name, 0, buf, 0, name.length);
            out.writeInt(name.length);
            out.write(buf, 0, name.length);
            out.writeLong(size);
            out.flush();
            while ((readBytes = fileInputStream.read(buf, 0, 300)) > 0) {
                out.write(buf, 0, readBytes);
                out.flush();
            }
        } catch (IOException e) {
            out.close();
            in.close();
            fileInputStream.close();
            e.printStackTrace();
        }

        boolean isTransmissionSuccessful = in.readBoolean();
        if (isTransmissionSuccessful) {
            System.out.println("Transmission successful");
        } else {
            System.out.println("Transmission has failed");
        }

        in.close();
        fileInputStream.close();
    }
}
