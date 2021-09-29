import java.io.IOException;
import java.net.*;

public class SecondMain {
    public static void main(String[] args) {
        Runtime current = Runtime.getRuntime();
        current.addShutdownHook(new Thread(() -> {
            System.out.println("In clean up code");
            System.out.println("In shutdown hook");
        }));
        while(true){}
    }
}
