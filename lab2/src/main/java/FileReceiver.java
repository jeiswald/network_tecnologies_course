import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileReceiver extends Thread {
    private final Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private FileOutputStream fileOutputStream;
    private final int FILESIZE_SIZE = Long.BYTES;
    private final int BUF_SIZE = 500;
    private final String PATH_FOR_UPLOADING = "./uploads";

    private final List<Long> readingInfo;

    public FileReceiver(Socket client) {
        this.client = client;
        readingInfo = new ArrayList<>(2);
        readingInfo.add(0, 0L);
        readingInfo.add(1, 0L);
    }

    @Override
    public void run() {
        byte[] buf = new byte[BUF_SIZE];
        Timer timer = new Timer();
        //SpeedTesterTimer task = new SpeedTesterTimer(readingInfo, 3);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                float speed;
                synchronized (readingInfo) {
                    speed = (float)readingInfo.get(1) / 3;
                    readingInfo.set(0, readingInfo.get(0) + readingInfo.get(1));
//            System.out.println("0 " + readingInfo.get(0));
                    readingInfo.set(1, 0L);
//            System.out.println("1 " + readingInfo.get(1));
                }
                System.out.println("Instant speed: " + speed);
            }
        }, 0, 3000);
        Date date = new Date();
        int filenameSize = 0;
        long fileSize = 0;
        try {
            in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

            int readBytes = 0;
            filenameSize = in.readInt();
            synchronized (readingInfo) {
                //readingInfo[1] += Integer.BYTES;
                readingInfo.set(1, readingInfo.get(1) + Integer.BYTES);
            }
            int tmp;
            while (readBytes != filenameSize) {
                if (filenameSize - readBytes > BUF_SIZE) {
                    tmp = in.read(buf, readBytes, BUF_SIZE);
                } else {
                    tmp = in.read(buf, readBytes, filenameSize - readBytes);
                }
                if (tmp > 0) {
                    readBytes += tmp;
                    synchronized (readingInfo) {
                        readingInfo.set(1, readingInfo.get(1) + tmp);
                    }
                }
            }
            File file = createFile(PATH_FOR_UPLOADING, (new String(buf, StandardCharsets.UTF_8)).trim());
            //System.out.println(new String(buf, StandardCharsets.UTF_8));
            fileOutputStream = new FileOutputStream(file);
            fileSize = in.readLong();
            synchronized (readingInfo) {
                readingInfo.set(1, readingInfo.get(1) + Long.BYTES);
            }
            System.out.println(fileSize);
            System.out.println(readingInfo.get(0));
            while ((readBytes = in.read(buf, 0, BUF_SIZE)) > 0 &&
                    (readingInfo.get(0) - (Integer.BYTES + Long.BYTES + filenameSize)) < fileSize) {
                fileOutputStream.write(buf, 0, readBytes);
                synchronized (readingInfo) {
                    System.out.println(readingInfo.get(0));
                    readingInfo.set(1, readingInfo.get(1) + readBytes);
                }
                fileOutputStream.flush();
            }
            System.out.println(readingInfo.get(0));
            out.writeBoolean(readingInfo.get(0) == fileSize + Integer.BYTES + Long.BYTES + filenameSize);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        timer.cancel();
        System.out.println("Instant speed: " + readingInfo.get(1) / 3);
        System.out.println("Average speed: " + (float)readingInfo.get(0) / ((new Date()).getTime() - date.getTime()));
    }

    private File createFile(String pathname, String filename) throws IOException {
        File path = new File(pathname);
        if (!path.isDirectory()) {
            if (!path.mkdir()) {
                throw new IOException("Cannot create a directory");
            }
        } else {
            System.out.println("Directory already exists");
        }
        File file = new File(path, filename);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Cannot create a file");
            } else {
                return file;
            }
        } else {
            throw new IOException("File already exist");
        }
    }
}
