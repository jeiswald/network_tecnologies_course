import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

public class FileReceiver extends Thread {
    private final Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private FileOutputStream fileOutputStream;
    private final Timer timer;
    private final int BUF_SIZE = 500;
    private final String PATH_FOR_UPLOADING = "./uploads";

    private long bytesHaveBeenRead = 0;

    public FileReceiver(Socket client, Timer timer) {
        this.client = client;
        this.timer = timer;
    }

    @Override
    public void run() {
        SpeedTesterTimer task = new SpeedTesterTimer(3);
        timer.schedule(task, 3000, 3000);
        Date date = new Date();
        int filenameSize;
        long fileSize;
        try {
            in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

            filenameSize = in.readInt();
            bytesHaveBeenRead += Integer.BYTES;
            task.addToCurrentBytesNumber(Integer.BYTES);

            String filename = readFilename(in, filenameSize);
            System.out.println("file name: " + filename);
            bytesHaveBeenRead += filenameSize;
            task.addToCurrentBytesNumber(filenameSize);

            File file = createFile(PATH_FOR_UPLOADING, filename);
            System.out.println("file created");
            fileOutputStream = new FileOutputStream(file);
            fileSize = in.readLong();
            System.out.println("file size:" + fileSize);
            task.setExpectedCountOfBytes(fileSize + filenameSize + Long.BYTES + Integer.BYTES);
            bytesHaveBeenRead += Long.BYTES;
            task.addToCurrentBytesNumber(Long.BYTES);

            readFileData(in, fileOutputStream, fileSize, task);
            fileOutputStream.close();

            out.writeBoolean(true);
            out.flush();

            task.cancel();
            timer.purge();
            System.out.println("Average speed: " + task.getAverageSpeed((new Date().getTime() - date.getTime()) / 1000L));
            if (task.executionCount() == 0) {
                System.out.println("Instant speed: " + task.getAverageSpeed((new Date().getTime() - date.getTime()) / 1000L));
            }
        } catch (Exception e) {
            try {
                out.writeBoolean(false);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readFileData(DataInputStream inputStream, FileOutputStream outputStream, long fileSize, SpeedTesterTimer task) throws IOException {
        int readBytes;
        long readTotalBytes = 0;
        byte[] buf = new byte[BUF_SIZE];
        while (readTotalBytes < fileSize && (readBytes = inputStream.read(buf, 0, BUF_SIZE)) != -1) {
            outputStream.write(buf, 0, readBytes);
            readTotalBytes += readBytes;
            task.addToCurrentBytesNumber(readBytes);
        }
        outputStream.flush();
        if (readTotalBytes != fileSize) {
            throw new IOException("not enough bytes read");
        }
    }

    private String readFilename(DataInputStream inputStream, int filenameSize) throws IOException {
        byte[] buf = new byte[filenameSize];
        int readBytes = 0;
        while (readBytes < filenameSize) {
            readBytes += inputStream.read(buf, readBytes, filenameSize - readBytes);
        }
        return new String(buf, StandardCharsets.UTF_8);
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
        File fileForCreation = new File(filename);
        Path pathForValidation = fileForCreation.toPath().normalize();
        if (pathForValidation.startsWith("..")) {
            throw new IOException("filename invalid");
        }
        if (pathForValidation.isAbsolute()) {
            file = new File(path, pathForValidation.getFileName().toString());
        }
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Cannot create a file");
            } else {
                return file;
            }
        } else {
            file.delete();
            file.createNewFile();
            return file;
            //FIXME: //throw new IOException("File already exist");
        }
    }
}
