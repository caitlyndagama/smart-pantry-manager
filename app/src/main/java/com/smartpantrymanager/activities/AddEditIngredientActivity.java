package com.smartpantrymanager.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.smartpantrymanager.R;
import com.smartpantrymanager.data.PantryRepository;
import com.smartpantrymanager.logic.ExpiryUtils;
import com.smartpantrymanager.logic.UnitConverter;
import com.smartpantrymanager.model.PantryItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;

public class AddEditIngredientActivity extends AppCompatActivity {
    public static final String EXTRA_PANTRY_ITEM_ID = "extra_pantry_item_id";
    private static final int MAX_NAME_LENGTH = 40;
    private static final double MAX_QUANTITY = 100000;
    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutQuantity;
    private TextInputLayout inputLayoutUnit;
    private TextInputLayout inputLayoutExpiry;
    private TextInputEditText editName;
    private TextInputEditText editQuantity;
    private TextInputEditText editExpiry;
    private MaterialAutoCompleteTextView autoUnit;
    private ProgressBar progressSave;
    private MaterialButton buttonSave;
    private MaterialButton buttonDelete;
    private View root;
    private final PantryRepository repository = new PantryRepository();
    private Integer pantryItemId;
    private String selectedExpiryDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_ingredient);
        root = findViewById(R.id.main);
        ScreenHelper.applySystemBarInsets(root);

        MaterialToolbar toolbar = findViewById(R.id.toolbarAddEdit);
        inputLayoutName = findViewById(R.id.inputLayoutName);
        inputLayoutQuantity = findViewById(R.id.inputLayoutQuantity);
        inputLayoutUnit = findViewById(R.id.inputLayoutUnit);
        inputLayoutExpiry = findViewById(R.id.inputLayoutExpiry);
        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiry = findViewById(R.id.editExpiry);
        autoUnit = findViewById(R.id.autoUnit);
        progressSave = findViewById(R.id.progressSave);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        toolbar.setNavigationOnClickListener(v -> finish());

        autoUnit.setSimpleItems(UnitConverter.USER_UNITS);
        autoUnit.setText("pcs", false);

        editExpiry.setOnClickListener(v -> showDatePicker());
        inputLayoutExpiry.setEndIconOnClickListener(v -> {
            selectedExpiryDate = null;
            editExpiry.setText("");
        });

        clearErrorWhileTyping(editName, inputLayoutName);
        clearErrorWhileTyping(editQuantity, inputLayoutQuantity);
        autoUnit.setOnItemClickListener((parent, view, position, id) -> inputLayoutUnit.setError(null));
        buttonSave.setOnClickListener(v -> saveIngredient());
        buttonDelete.setOnClickListener(v -> confirmDelete());

        if (getIntent().hasExtra(EXTRA_PANTRY_ITEM_ID)) {
            pantryItemId = getIntent().getIntExtra(EXTRA_PANTRY_ITEM_ID, -1);
            toolbar.setTitle(R.string.title_edit_ingredient);
            buttonDelete.setVisibility(View.VISIBLE);
            loadItem();
        } else {
            toolbar.setTitle(R.string.title_add_ingredient);
        }
    }
    private void loadItem() {
        setBusy(true);
        repository.getPantryItem(pantryItemId, new PantryRepository.DataCallback<PantryItem>() {
            @Override
            public void onSuccess(PantryItem item) {
                if (isDestroyed()) return;
                setBusy(false);
                editName.setText(item.getDisplayName());
                editQuantity.setText(UnitConverter.formatNumber(item.getQuantity()));
                autoUnit.setText(item.getBaseUnit(), false);
                selectedExpiryDate = item.getExpiryDate();
                editExpiry.setText(ExpiryUtils.toDisplayFormat(selectedExpiryDate));
            }
            @Override
            public void onError(String message) {
                if (isDestroyed()) return;
                setBusy(false);
                Toast.makeText(AddEditIngredientActivity.this, message, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
    private void saveIngredient() {
        if (!validateInputs()) {
            return;
        }
        String name = textOf(editName);
        double quantity = Double.parseDouble(textOf(editQuantity).replace(',', '.'));
        String unit = autoUnit.getText().toString();
        setBusy(true);
        repository.savePantryItem(pantryItemId, name, quantity, unit, selectedExpiryDate,
                new PantryRepository.DataCallback<PantryItem>() {
                    @Override
                    public void onSuccess(PantryItem saved) {
                        if (isDestroyed()) return;
                        Toast.makeText(AddEditIngredientActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(String message) {
                        if (isDestroyed()) return;
                        setBusy(false);
                        Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
                    }
                });
    }
    private boolean validateInputs() {
        boolean valid = true;

        String name = textOf(editName);
        if (name.isEmpty()) {
            inputLayoutName.setError(getString(R.string.error_name_required));
            valid = false;
        } else if (!name.matches("[\\p{L} '\\-]+")) {
            inputLayoutName.setError(getString(R.string.error_name_letters));
            valid = false;
        } else if (name.length() > MAX_NAME_LENGTH) {
            inputLayoutName.setError(getString(R.string.error_name_long));
            valid = false;
        }

        String quantityText = textOf(editQuantity).replace(',', '.');
        if (quantityText.isEmpty()) {
            inputLayoutQuantity.setError(getString(R.string.error_quantity_required));
            valid = false;
        } else {
            try {
                double quantity = Double.parseDouble(quantityText);
                if (quantity <= 0) {
                    inputLayoutQuantity.setError(getString(R.string.error_quantity_positive));
                    valid = false;
                } else if (quantity > MAX_QUANTITY) {
                    inputLayoutQuantity.setError(getString(R.string.error_quantity_large));
                    valid = false;
                }
            } catch (NumberFormatException e) {
                inputLayoutQuantity.setError(getString(R.string.error_quantity_number));
                valid = false;
            }
        }

        if (!UnitConverter.isSupported(autoUnit.getText().toString())) {
            inputLayoutUnit.setError(getString(R.string.error_unit));
            valid = false;
        }
        return valid;
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, textOf(editName)))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteItem())
                .show();
    }
    private void deleteItem() {
        setBusy(true);
        repository.deletePantryItem(pantryItemId, new PantryRepository.DataCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                if (isDestroyed()) return;
                Toast.makeText(AddEditIngredientActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String message) {
                if (isDestroyed()) return;
                setBusy(false);
                Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
    private void showDatePicker() {
        Calendar today = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    selectedExpiryDate = ExpiryUtils.toDbFormat(year, month, day);
                    editExpiry.setText(ExpiryUtils.toDisplayFormat(selectedExpiryDate));
                },
                today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(today.getTimeInMillis() - 1000);
        dialog.show();
    }
    private void setBusy(boolean busy) {
        progressSave.setVisibility(busy ? View.VISIBLE : View.GONE);
        buttonSave.setEnabled(!busy);
        buttonDelete.setEnabled(!busy);
    }
    private static String textOf(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }
    private static void clearErrorWhileTyping(TextInputEditText field, TextInputLayout layout) {
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
}