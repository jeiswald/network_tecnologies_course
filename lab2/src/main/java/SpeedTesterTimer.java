import java.util.List;
import java.util.TimerTask;

public class SpeedTesterTimer extends TimerTask {
    private final List<Long> readingInfo;
    int timeout;
    public SpeedTesterTimer(List<Long> readingInfo, int timeout) {
        this.readingInfo = readingInfo;
        this.timeout = timeout;
    }
    @Override
    public void run() {
        float speed;
        synchronized (readingInfo) {
            speed = (float)readingInfo.get(1) / timeout;
            readingInfo.set(0, readingInfo.get(0) + readingInfo.get(1));
//            System.out.println("0 " + readingInfo.get(0));
            readingInfo.set(1, 0L);
//            System.out.println("1 " + readingInfo.get(1));
        }
        System.out.println("Instant speed: " + speed);
    }
}
