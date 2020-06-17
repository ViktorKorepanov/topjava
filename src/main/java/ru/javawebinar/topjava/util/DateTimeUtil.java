package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // фильтрация по времени
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    // фильтрация по дате
    public static boolean isBetweenHalfOpen(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        return ld.compareTo(startDate) >= 0 && ld.compareTo(endDate) <= 0;
    }

    // фильтрация по дате и времени
    public static boolean isBetweenHalfOpen(LocalDate ld, LocalDate startDate, LocalDate endDate, LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return (ld.compareTo(startDate) >= 0 && ld.compareTo(endDate) <= 0) && (lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}
