import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        byte[] buf = {'a', 'b', 'c', 'd', 'e'};
        System.out.println(new String(buf, StandardCharsets.UTF_8));
        buf[0] = 0;
        buf[1] = 0;
        buf[2] = 0;
        buf[3] = 1;
        int fileSize = ByteBuffer.wrap(buf).getInt();
        System.out.println(fileSize);
        System.out.println(new String(buf, StandardCharsets.UTF_8));
    }
}
