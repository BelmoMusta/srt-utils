package model;

import java.time.format.DateTimeFormatter;

public class StaticHolder {
    private static class LazyLoader {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
    }
    public static DateTimeFormatter getFormatter(){
        return LazyLoader.FORMATTER;
    }
}
