package com.smartpantrymanager.activities.activities;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.smartpantrymanager.R;
import com.smartpantrymanager.activities.data.SettingsManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

public class SettingsActivity extends AppCompatActivity{

    private static final int[] WARNING_DAY_VALUES = {1, 3, 7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ScreenHelper.applySystemBarInsets(findViewById(R.id.main));
        SettingsManager settings = new SettingsManager(this);
        MaterialSwitch switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        TextInputLayout inputLayoutWarningDays = findViewById(R.id.inputLayoutWarningDays);
        MaterialAutoCompleteTextView autoWarningDays = findViewById(R.id.autoWarningDays);
        MaterialSwitch switchExcludeExpired = findViewById(R.id.switchExcludeExpired);

        switchExpiryAlerts.setChecked(settings.isExpiryAlertsEnabled());
        inputLayoutWarningDays.setEnabled(settings.isExpiryAlertsEnabled());
        switchExpiryAlerts.setOnCheckedChangeListener((button, isChecked) -> {
            settings.setExpiryAlertsEnabled(isChecked);
            inputLayoutWarningDays.setEnabled(isChecked);
        });

        String[] labels = getResources().getStringArray(R.array.warning_day_labels);
        autoWarningDays.setSimpleItems(labels);
        autoWarningDays.setText(labels[indexOf(settings.getWarningDays())], false);
        autoWarningDays.setOnItemClickListener((parent, view, position, id) ->
                settings.setWarningDays(position));
        switchExcludeExpired.setChecked(settings.isExcludeExpired());

        switchExcludeExpired.setOnCheckedChangeListener((button, isChecked) ->
                settings.setExcludeExpired(isChecked));
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        ScreenHelper.setUpBottomNav(this, bottomNav, R.id.nav_settings);
    }
    private static int indexOf(int days) {
        for (int i = 0; i < WARNING_DAY_VALUES.length; i++) {
            if (WARNING_DAY_VALUES[i] == days) {
                return i;
            }
        }
        return 1;
    }
}