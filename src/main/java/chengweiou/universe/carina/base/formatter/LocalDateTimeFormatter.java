package chengweiou.universe.carina.base.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
    @Override
    public LocalDateTime parse(String text, Locale locale) throws ParseException {
        try {
            return LocalDateTime.parse(text);
        } catch(DateTimeParseException ex) {
            try {
                return LocalDate.parse(text).atStartOfDay();
            } catch(DateTimeParseException ex1) {
                return ZonedDateTime.parse(text).toLocalDateTime();
            }
        }
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return object.toString();
    }
}
