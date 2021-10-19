import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FileReceiver extends Thread {
    private Socket client;
    private DataInputStream in;
    private FileOutputStream  outputStream;
    private final int FILENAME_MAX_SIZE = 256;
    private final int FILESIZE_SIZE = Integer.BYTES;
    private final String PATH_FOR_UPLOADING = "./uploads";
    public FileReceiver(Socket client) {
        this.client = client;
    }
    @Override
    public void run() {
        byte[] buf = new byte[300];
        try {
            in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            int readBytes = 0;
            while (readBytes != FILENAME_MAX_SIZE) {
                readBytes += in.read(buf, readBytes, FILENAME_MAX_SIZE - readBytes);
            }
            File file = createFile(PATH_FOR_UPLOADING, new String(buf, StandardCharsets.UTF_8));

            assert file != null;///TODO: =)
            outputStream = new FileOutputStream (file);

            readBytes = 0;
            while (readBytes != FILESIZE_SIZE) {
                readBytes += in.read(buf, readBytes, FILESIZE_SIZE - readBytes);
            }
            int fileSize = ByteBuffer.wrap(buf).getInt();
            readBytes = 0;
            while((readBytes = in.read(buf, 0, 300)) > 0) {
                outputStream.write(buf, 0, readBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private File createFile(String pathname, String filename) {
        File path = new File(pathname);
        if (!path.isDirectory()) {
            if (!path.mkdir()) {
                System.out.println("Cannot create a directory");
            }
        } else {
            System.out.println("Directory already exists");
        }
        File file = new File(path, filename);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.out.println("Cannot create a file");
                } else {
                    return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File already exists");
        }
        return null;
    }
}
