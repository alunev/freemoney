package common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author red
 * @since 0.0.1
 */
public class DateUtils {
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static long nowTs() {
        return new Date().getTime();
    }

    public static long startOfTime() {
        return 0L;
    }

    public static ZonedDateTime startOfTimeZdt() {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.systemDefault());
    }

    public static ZonedDateTime millisToZdt(long millis) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }
}
