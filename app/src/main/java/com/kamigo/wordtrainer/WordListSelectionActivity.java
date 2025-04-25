package com.kamigo.wordtrainer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity; // ← вот этого не хватает!

public class WordListSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list_selection);

        Button animalsBtn = findViewById(R.id.button_animals);
        Button verbsBtn = findViewById(R.id.button_verbs);
        Button foodBtn = findViewById(R.id.button_food);
        Button uploadBtn = findViewById(R.id.button_upload_own);

        animalsBtn.setOnClickListener(v -> startTrainingWithList("animals"));
        verbsBtn.setOnClickListener(v -> startTrainingWithList("verbs"));
        foodBtn.setOnClickListener(v -> startTrainingWithList("food"));

        uploadBtn.setOnClickListener(v ->
                Toast.makeText(this, "Функция загрузки будет добавлена позже", Toast.LENGTH_SHORT).show()
        );
    }

    private void startTrainingWithList(String listName) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("word_list", listName);
        startActivity(intent);
    }
}
