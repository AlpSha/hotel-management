package alancerpro.model;

import java.time.format.DateTimeFormatter;

public class TimeFormat {
    public static final String format  = "dd-MM-yyyy";

    public static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(format);
    }
}
