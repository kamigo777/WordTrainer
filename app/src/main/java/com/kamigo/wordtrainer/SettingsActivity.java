package com.kamigo.wordtrainer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup answerCountGroup, directionGroup;
    private SeekBar speedSeekBar;
    private TextView speedValueText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        answerCountGroup = findViewById(R.id.answerCountGroup);
        directionGroup = findViewById(R.id.directionGroup);
        speedSeekBar = findViewById(R.id.speedSeekBar);
        speedValueText = findViewById(R.id.speedValueText);
        Button saveBtn = findViewById(R.id.saveSettingsButton);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int count = prefs.getInt("answer_count", 4);
        String direction = prefs.getString("translation_direction", "en_to_ru");
        int speed = prefs.getInt("answer_speed", 6);

        // Кол-во кнопок
        if (count == 4) answerCountGroup.check(R.id.count4);
        else if (count == 6) answerCountGroup.check(R.id.count6);
        else if (count == 8) answerCountGroup.check(R.id.count8);

        // Направление
        switch (direction) {
            case "en_to_ru": directionGroup.check(R.id.dir_en_to_ru); break;
            case "ru_to_en": directionGroup.check(R.id.dir_ru_to_en); break;
            case "random":  directionGroup.check(R.id.dir_random); break;
        }

        // Диапазон скорости: от 6 до 18 с шагом 2 → 0..6
        int progress = (speed - 6) / 2;
        speedSeekBar.setMax(6);
        speedSeekBar.setProgress(progress);
        speedValueText.setText(speed + " сек");

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = 6 + progress * 2;
                speedValueText.setText(value + " сек");
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Сохранение
        saveBtn.setOnClickListener(v -> {
            int selectedCount = 4;
            if (answerCountGroup.getCheckedRadioButtonId() == R.id.count6) selectedCount = 6;
            else if (answerCountGroup.getCheckedRadioButtonId() == R.id.count8) selectedCount = 8;

            String selectedDirection = "en_to_ru";
            if (directionGroup.getCheckedRadioButtonId() == R.id.dir_ru_to_en) selectedDirection = "ru_to_en";
            else if (directionGroup.getCheckedRadioButtonId() == R.id.dir_random) selectedDirection = "random";

            int selectedSpeed = 6 + speedSeekBar.getProgress() * 2;

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("answer_count", selectedCount);
            editor.putString("translation_direction", selectedDirection);
            editor.putInt("answer_speed", selectedSpeed);
            editor.apply();

            finish();
        });
    }
}
