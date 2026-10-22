package com.smartpantrymanager.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import java.util.Calendar;

public class ExpiryUtilsTest {
    private static Calendar day(int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, dayOfMonth, 15, 30);
        return c;
    }

    @Test
    public void countsWholeDays() {
        Calendar today = day(2026, 10, 20);
        assertEquals(0, ExpiryUtils.daysUntil("2026-10-20", today));
        assertEquals(3, ExpiryUtils.daysUntil("2026-10-23", today));
        assertEquals(-1, ExpiryUtils.daysUntil("2026-10-19", today));
        assertEquals(12, ExpiryUtils.daysUntil("2026-11-01", today));
    }

    @Test
    public void missingDateMeansNeverExpires() {
        assertEquals(Integer.MAX_VALUE, ExpiryUtils.daysUntil(null, day(2026, 10, 20)));
        assertEquals(Integer.MAX_VALUE, ExpiryUtils.daysUntil("not a date", day(2026, 10, 20)));
    }

    @Test
    public void formatsPickedDatesForTheDatabase() {
        assertEquals("2026-01-05", ExpiryUtils.toDbFormat(2026, 0, 5));
        assertEquals("25 Oct 2026", ExpiryUtils.toDisplayFormat("2026-10-25"));
        assertEquals("", ExpiryUtils.toDisplayFormat(null));
    }
}