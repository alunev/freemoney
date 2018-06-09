package common;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
}
