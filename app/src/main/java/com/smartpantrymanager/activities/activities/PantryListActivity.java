package com.smartpantrymanager.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smartpantrymanager.R;
import com.smartpantrymanager.activities.adapters.PantryAdapter;
import com.smartpantrymanager.activities.data.PantryRepository;
import com.smartpantrymanager.activities.data.SettingsManager;
import com.smartpantrymanager.activities.model.PantryItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class PantryListActivity extends AppCompatActivity{
    private static final String TAG = "PantryListActivity";
    private ProgressBar progressPantry;
    private LinearLayout layoutPantryMessage;
    private TextView textPantryMessage;
    private MaterialButton buttonPantryRetry;
    private PantryAdapter adapter;
    private PantryRepository repository;
    private SettingsManager settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantry_list);
        ScreenHelper.applySystemBarInsets(findViewById(R.id.main));
        Log.d(TAG, "onCreate");
        repository = new PantryRepository();
        settings = new SettingsManager(this);
        RecyclerView recyclerPantry = findViewById(R.id.recyclerPantry);
        progressPantry = findViewById(R.id.progressPantry);
        layoutPantryMessage = findViewById(R.id.layoutPantryMessage);
        textPantryMessage = findViewById(R.id.textPantryMessage);
        buttonPantryRetry = findViewById(R.id.buttonPantryRetry);
        FloatingActionButton fabAddIngredient = findViewById(R.id.fabAddIngredient);
        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));
        recyclerPantry.setAdapter(adapter);
        fabAddIngredient.setOnClickListener(v -> openEditor(null));
        buttonPantryRetry.setOnClickListener(v -> loadPantry());
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        ScreenHelper.setUpBottomNav(this, bottomNav, R.id.nav_pantry);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: reloading pantry");
        adapter.setExpirySettings(settings.isExpiryAlertsEnabled(), settings.getWarningDays());
        loadPantry();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
    private void loadPantry() {
        progressPantry.setVisibility(View.VISIBLE);
        layoutPantryMessage.setVisibility(View.GONE);
        repository.getPantryItems(new PantryRepository.DataCallback<List<PantryItem>>() {
            @Override
            public void onSuccess(List<PantryItem> items) {
                if (isDestroyed()) return;
                progressPantry.setVisibility(View.GONE);
                adapter.setItems(items);
                if (items == null || items.isEmpty()) {
                    showMessage(getString(R.string.pantry_empty), false);
                }
            }
            @Override
            public void onError(String message) {
                if (isDestroyed()) return;
                progressPantry.setVisibility(View.GONE);
                showMessage(message, true);
            }
        });
    }
    private void showMessage(String message, boolean showRetry) {
        textPantryMessage.setText(message);
        buttonPantryRetry.setVisibility(showRetry ? View.VISIBLE : View.GONE);
        layoutPantryMessage.setVisibility(View.VISIBLE);
    }
    private void openEditor(Integer pantryItemId) {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        if (pantryItemId != null) {
            intent.putExtra(AddEditIngredientActivity.EXTRA_PANTRY_ITEM_ID, pantryItemId.intValue());
        }
        startActivity(intent);
    }

    private void onItemClick(PantryItem item) {
        openEditor(item.getId());
    }
}