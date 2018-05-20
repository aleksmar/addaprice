package com.example.addaprice;

import java.time.*;
import java.util.Date;

public class Util {
    public static Date startDay(int year, Month january, int day) {
        return toDate(atStartOfDay(year, january, day));
    }

    public static Date endDay(int year, Month february, int day) {
        return toDate(atEndOfDay(year, february, day));
    }

    public static Date minusMillis(Date date, int millis) {
        return toDate(toLocalDateTime(date).minus(Duration.ofMillis(millis)));
    }

    public static Date plusMillis(Date date, int millis) {
        return toDate(toLocalDateTime(date).plus(Duration.ofMillis(millis)));
    }

    private static LocalDateTime atStartOfDay(int year, Month month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    private static LocalDateTime atEndOfDay(int year, Month month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
