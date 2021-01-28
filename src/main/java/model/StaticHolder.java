package model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StaticHolder {
    private static class LazyLoader {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
    }
    public static DateTimeFormatter getFormatter(){
        return LazyLoader.FORMATTER;
    }
    
    public static void main(String[] args) {
        LocalTime startsAt = LocalTime.parse("01:15:56,001", StaticHolder.getFormatter());
        Duration duration  = Duration.ofHours(0).plusMinutes(-1).plusSeconds(0);
        startsAt = startsAt.plus(duration);
        System.out.println(startsAt);
    }
}
