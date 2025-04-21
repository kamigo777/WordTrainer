package com.kamigo.wordtrainer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.*;

public class GameActivity extends AppCompatActivity {

    private TextView wordText;
    private CardView wordCard;
    private GridLayout answerContainer;
    private WaveView waveView;
    private BubbleView bubbleView;
    private List<Button> optionButtons = new ArrayList<>();
    private String correctAnswer;
    private int buttonCount;

    private int lives = 3;
    private ImageView heart1, heart2, heart3;

    private boolean answered = false;

    private final Map<String, String> wordMap = new HashMap<String, String>() {{
        put("apple", "яблоко");
        put("house", "дом");
        put("cat", "кот");
        put("water", "вода");
        put("sun", "солнце");
        put("book", "книга");
        put("tree", "дерево");
        put("milk", "молоко");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        wordText = findViewById(R.id.wordText);
        wordCard = findViewById(R.id.wordCard);
        answerContainer = findViewById(R.id.answerContainer);
        waveView = findViewById(R.id.waveView);
        bubbleView = findViewById(R.id.bubbleView);

        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        buttonCount = prefs.getInt("answer_count", 4);
        answerContainer.setColumnCount(2);

        loadNewWord();
    }

    private void loadNewWord() {
        answerContainer.removeAllViews();
        optionButtons.clear();
        answered = false;

        wordCard.setCardBackgroundColor(getColor(android.R.color.white));
        bubbleView.resetBubbles();

        List<String> englishWords = new ArrayList<>(wordMap.keySet());
        String randomWord = englishWords.get(new Random().nextInt(englishWords.size()));
        correctAnswer = wordMap.get(randomWord);
        wordText.setText(randomWord);

        List<String> allTranslations = new ArrayList<>(wordMap.values());
        allTranslations.remove(correctAnswer);
        Collections.shuffle(allTranslations);

        List<String> options = new ArrayList<>();
        options.add(correctAnswer);
        for (int i = 0; i < buttonCount - 1; i++) {
            options.add(allTranslations.get(i));
        }
        Collections.shuffle(options);

        for (int i = 0; i < buttonCount; i++) {
            String answer = options.get(i);

            Button btn = new Button(this);
            btn.setText(answer);
            btn.setTextColor(getColor(R.color.text_normal));
            btn.setTextSize(18);
            btn.setPadding(16, 16, 16, 16);
            btn.setBackgroundResource(R.drawable.button);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);
            btn.setLayoutParams(params);

            btn.setOnClickListener(v -> checkAnswer(btn, answer));

            optionButtons.add(btn);
            answerContainer.addView(btn);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int answerSpeed = prefs.getInt("answer_speed", 6);
        long duration = answerSpeed * 1000L;

        waveView.startRising(duration, () -> {
            if (!answered) {
                runOnUiThread(() -> {
                    loseLife();
                    disableButtons();

                    wordCard.setCardBackgroundColor(getColor(R.color.button_wrong));

                    for (Button btn : optionButtons) {
                        if (btn.getText().equals(correctAnswer)) {
                            btn.setBackgroundResource(R.drawable.button_correct);
                            break;
                        }
                    }

                    wordCard.postDelayed(() -> {
                        if (lives > 0) {
                            wordCard.setCardBackgroundColor(getColor(android.R.color.white));
                            loadNewWord();
                        }
                    }, 1000);
                });
            }
        });

        // Пузыри стартуют одновременно с волной
        bubbleView.startBubbles(duration);
    }

    private void checkAnswer(Button clickedButton, String selectedAnswer) {
        if (answered) return;

        answered = true;
        waveView.stop();
        bubbleView.stopBubbles();

        if (selectedAnswer.equals(correctAnswer)) {
            clickedButton.setBackgroundResource(R.drawable.button_correct);
            wordCard.setCardBackgroundColor(getColor(R.color.button_correct));
        } else {
            clickedButton.setBackgroundResource(R.drawable.button_wrong);
            wordCard.setCardBackgroundColor(getColor(R.color.button_wrong));

            for (Button btn : optionButtons) {
                if (btn.getText().equals(correctAnswer)) {
                    btn.setBackgroundResource(R.drawable.button_correct);
                    break;
                }
            }

            loseLife();
        }

        disableButtons();

        clickedButton.postDelayed(() -> {
            if (lives > 0) {
                wordCard.setCardBackgroundColor(getColor(android.R.color.white));
                loadNewWord();
            }
        }, 1500);
    }

    private void disableButtons() {
        for (Button btn : optionButtons) {
            btn.setEnabled(false);
        }
    }

    private void loseLife() {
        lives--;
        if (lives == 2) {
            heart3.setImageResource(R.drawable.heart_empty);
        } else if (lives == 1) {
            heart2.setImageResource(R.drawable.heart_empty);
        } else if (lives == 0) {
            heart1.setImageResource(R.drawable.heart_empty);
            gameOver();
        }
    }

    private void gameOver() {
        Toast.makeText(this, "Игра окончена!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
