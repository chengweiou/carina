package chengweiou.universe.carina.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class DateUtil {
    public static LocalDate localDateNow() {
        return LocalDate.now(ZoneId.of("America/Los_Angeles"));
    }
    public static LocalDateTime localDateTimeNow() {
        return LocalDateTime.now(ZoneId.of("America/Los_Angeles"));
    }
}
