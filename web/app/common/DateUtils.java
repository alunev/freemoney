package common;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author red
 * @since 0.0.1
 */
public class DateUtils {
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }
}
