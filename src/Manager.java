import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

public class Manager {
    public static void main(String[] args) {
        LocalTime startTime = LocalTime.of(10, 59, 0); // 10:30 AM
        LocalTime endTime = LocalTime.of(14, 55, 30); // 2:45:30 PM

        // Calculate the time gap
        Duration duration = Duration.between(startTime, endTime);

        // Get the difference in hours, minutes, and seconds
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        // Output the result
        System.out.println("Time gap: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds.");

    }
}
