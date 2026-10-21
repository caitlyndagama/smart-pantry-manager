package com.smartpantrymanager.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class ExpiryUtils {

    public static final String DB_FORMAT = "yyyy-MM-dd";

    private ExpiryUtils() {
        // Utility class: no instances
    }

    public static int daysUntil(String expiryDate, Calendar today) {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(DB_FORMAT, Locale.ROOT);
            format.setLenient(false);
            Date expiry = format.parse(expiryDate);
            Calendar expiryDay = Calendar.getInstance();
            expiryDay.setTime(expiry);
            long diffMillis = startOfDay(expiryDay).getTimeInMillis()
                    - startOfDay(today).getTimeInMillis();
            return (int) Math.round(diffMillis / (double) TimeUnit.DAYS.toMillis(1));
        } catch (ParseException e) {
            return Integer.MAX_VALUE;
        }
    }

    public static int daysUntil(String expiryDate) {
        return daysUntil(expiryDate, Calendar.getInstance());
    }

    public static boolean isExpired(String expiryDate) {
        return daysUntil(expiryDate) < 0;
    }

    public static String toDbFormat(int year, int monthZeroBased, int day) {
        return String.format(Locale.ROOT, "%04d-%02d-%02d", year, monthZeroBased + 1, day);
    }

    public static String toDisplayFormat(String dbDate) {
        if (dbDate == null || dbDate.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat in = new SimpleDateFormat(DB_FORMAT, Locale.ROOT);
            SimpleDateFormat out = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
            return out.format(in.parse(dbDate));
        } catch (ParseException e) {
            return dbDate;
        }
    }

    private static Calendar startOfDay(Calendar source) {
        Calendar c = (Calendar) source.clone();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }
}