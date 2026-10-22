package com.smartpantrymanager.data;

import android.content.Context;
import android.content.SharedPreferences;
public class SettingsManager {
    private static final String PREFS_NAME = "smart_pantry_settings";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts_enabled";
    private static final String KEY_WARNING_DAYS = "expiry_warning_days";
    private static final String KEY_EXCLUDE_EXPIRED = "exclude_expired";
    private final SharedPreferences prefs;
    public SettingsManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    public boolean isExpiryAlertsEnabled() {
        return prefs.getBoolean(KEY_EXPIRY_ALERTS, true);
    }
    public void setExpiryAlertsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, enabled).apply();
    }
    public int getWarningDays() {
        return prefs.getInt(KEY_WARNING_DAYS, 3);
    }
    public void setWarningDays(int days) {
        prefs.edit().putInt(KEY_WARNING_DAYS, days).apply();
    }
    public boolean isExcludeExpired() {
        return prefs.getBoolean(KEY_EXCLUDE_EXPIRED, true);
    }
    public void setExcludeExpired(boolean exclude) {
        prefs.edit().putBoolean(KEY_EXCLUDE_EXPIRED, exclude).apply();
    }
}