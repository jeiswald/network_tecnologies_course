import java.util.List;
import java.util.TimerTask;

public class SpeedTesterTimer extends TimerTask {
    private long lastBytesNumber = 0;
    volatile private long currentBytesNumber = 0;
    private final int timeout;
    volatile private int executionCount = 0;
    volatile private Long expectedCountOfBytes = null;

    public SpeedTesterTimer(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void run() {
        long currentBytesNumberForProcessing = currentBytesNumber;
        Long expectedCountOfBytesForProcessing = expectedCountOfBytes;
        float speed = (float) (currentBytesNumberForProcessing - lastBytesNumber) / timeout;
        lastBytesNumber = currentBytesNumberForProcessing;
        executionCount++;
        System.out.println("Instant speed: " + speed);
        if (expectedCountOfBytesForProcessing != null) {
            System.out.print("|");
            double percent = (double) currentBytesNumberForProcessing / expectedCountOfBytesForProcessing;
            int divisions = 30;
            for (int i = 0; i < Math.floor(percent * divisions); i++) {
                System.out.print("#");
            }
            for (int i = (int) Math.ceil(percent * divisions); i < divisions; i++) {
                System.out.print("-");
            }
            System.out.println("|");
        }
    }

    public void addToCurrentBytesNumber(long toAdd) {
        currentBytesNumber = currentBytesNumber + toAdd;
    }

    public float getAverageSpeed(long period) {
        return (float) currentBytesNumber / period;
    }

    public int executionCount() {
        return executionCount;
    }

    public void setExpectedCountOfBytes(long expectedCountOfBytes) {
        this.expectedCountOfBytes = expectedCountOfBytes;
    }
}
