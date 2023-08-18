package io.nermdev.kafka.leaderboardstreams.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;


public final class DateFormatter {
    static final String FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static String formatDateToString(Long timestamp,
                                            String timeZone) {
        final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        final Date date = Optional.of(new Date(timestamp)).orElseGet(() -> new Date(System.currentTimeMillis()));
        return Optional.of(date)
                .map(d -> {
                    sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
                    return sdf.format(d);
                }).orElseGet(() -> {
                    sdf.setTimeZone(TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().getID()));
                    return sdf.format(date);
                });
    }

    private DateFormatter(){}
}
