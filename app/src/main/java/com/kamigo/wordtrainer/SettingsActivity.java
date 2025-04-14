package com.kamigo.wordtrainer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radioGroup = findViewById(R.id.radioGroup);
        saveButton = findViewById(R.id.saveButton);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int savedCount = prefs.getInt("answer_count", 4);

        // Устанавливаем сохранённый выбор
        switch (savedCount) {
            case 4:
                ((RadioButton) findViewById(R.id.radio4)).setChecked(true);
                break;
            case 6:
                ((RadioButton) findViewById(R.id.radio6)).setChecked(true);
                break;
            case 8:
                ((RadioButton) findViewById(R.id.radio8)).setChecked(true);
                break;
        }

        saveButton.setOnClickListener(v -> {
            int selectedCount = 4;
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId == R.id.radio6) {
                selectedCount = 6;
            } else if (checkedId == R.id.radio8) {
                selectedCount = 8;
            }

            prefs.edit().putInt("answer_count", selectedCount).apply();
            Toast.makeText(this, "Сохранено: " + selectedCount + " вариантов", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
