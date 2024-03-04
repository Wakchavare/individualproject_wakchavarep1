package com.example.individualproject_wakchavarep1;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.Toast;
import android.content.Intent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuestionsActivity extends AppCompatActivity {

    private List<QuizQuestion> quizQuestions;
    private SharedPreferences sharedPreferences;
    private LinearLayout questionLayout; // Reference to the question layout
    private Button submitButton;
    private int userScore = 0;
    private int currentQuestionIndex = 0;
    private RadioGroup currentRadioGroup;
    private LinearLayout currentCheckBoxLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        questionLayout = findViewById(R.id.question_layout); // Assigning question layout
        submitButton = findViewById(R.id.submit_button);
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_score", MODE_PRIVATE);

        initializeQuestions();
        displayQuestion(currentQuestionIndex);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerAndDisplayNext();
            }
        });
    }

    private void initializeQuestions() {
        quizQuestions = Arrays.asList(
                new QuizQuestion("1. In the game \"Super Mario Bros.\", what item allows Mario to grow larger?", Arrays.asList("Mushroom", "Star", "Fire Flower", "Super Leaf"), Collections.singletonList("Mushroom"), QuestionType.SINGLE),
                new QuizQuestion("2. Which of these is not a character in the game \"Overwatch\"?", Arrays.asList("Tracer", "Reinhardt", "Sora", "Mercy"), Collections.singletonList("Sora"), QuestionType.SINGLE),
                new QuizQuestion("3. Which of the following are playable characters in \"Super Smash Bros. Ultimate\"?", Arrays.asList("Mario", "Pikachu", "Donkey Kong", "Kirby"), Arrays.asList("Mario", "Pikachu"), QuestionType.MULTIPLE),
                new QuizQuestion("4. Which of the following are weapons in the game \"Counter-Strike: Global Offensive\"?", Arrays.asList("AK-47", "AWP", "M4A1", "Desert Eagle"), Arrays.asList("AK-47", "AWP", "M4A1"), QuestionType.MULTIPLE),
                new QuizQuestion("5. In \"Minecraft\", what is the primary material used to craft tools?", Arrays.asList("Wood", "Stone", "Iron", "Diamond"), Collections.singletonList("Wood"), QuestionType.SINGLE)
        );
    }

    private void displayQuestion(int index) {
        QuizQuestion question = quizQuestions.get(index);
        questionLayout.removeAllViews(); // Clear previous question and options

        TextView questionText = new TextView(this);
        questionText.setText(question.getQuestion());
        questionLayout.addView(questionText);

        if (question.getType() == QuestionType.SINGLE) {
            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            for (String option : question.getOptions()) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                radioGroup.addView(radioButton);
            }
            questionLayout.addView(radioGroup);
            currentRadioGroup = radioGroup;
            currentCheckBoxLayout = null; // Reset the checkbox layout
        } else {
            LinearLayout checkBoxLayout = new LinearLayout(this);
            checkBoxLayout.setOrientation(LinearLayout.VERTICAL);
            for (String option : question.getOptions()) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(option);
                checkBoxLayout.addView(checkBox);
            }
            questionLayout.addView(checkBoxLayout);
            currentCheckBoxLayout = checkBoxLayout;
            currentRadioGroup = null; // Reset the radio group
        }
    }

    private void checkAnswerAndDisplayNext() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you confirming your selection?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (checkSelectedOption()) {
                    userScore++;
                }
                saveUserScore(userScore);
                currentQuestionIndex++;
                if (currentQuestionIndex < quizQuestions.size()) {
                    displayQuestion(currentQuestionIndex);
                } else {
                    Toast.makeText(QuestionsActivity.this, "End of questions", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(QuestionsActivity.this, DisplayScoreActivity.class);
                    startActivity(intent);
                    finish();
                    saveUserHistory();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean checkSelectedOption() {
        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
        boolean correct = false;

        if (currentRadioGroup != null) {
            int radioButtonID = currentRadioGroup.getCheckedRadioButtonId();
            View radioButton = currentRadioGroup.findViewById(radioButtonID);
            if (radioButton != null) {
                int idx = currentRadioGroup.indexOfChild(radioButton);
                RadioButton r = (RadioButton) currentRadioGroup.getChildAt(idx);
                String selectedText = r.getText().toString();
                if (currentQuestion.getAnswer().contains(selectedText)) {
                    correct = true;
                }
            }
        } else if (currentCheckBoxLayout != null) {
            int correctChoices = 0;
            for (int i = 0; i < currentCheckBoxLayout.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) currentCheckBoxLayout.getChildAt(i);
                if (checkBox.isChecked() && currentQuestion.getAnswer().contains(checkBox.getText().toString())) {
                    correctChoices++;
                }
            }
            if (correctChoices == currentQuestion.getAnswer().size()) {
                correct = true;
            }
        }

        return correct;
    }

    private void saveUserScore(int score) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_score", score);
        editor.apply();
    }

    private void saveUserHistory() {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        saveUserHistory(currentTime, userScore);
    }

    private void saveUserHistory(String time, int score) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_history", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(time, score);
        editor.apply();
    }

    class QuizQuestion {
        private String question;
        private List<String> options;
        private List<String> answer;
        private QuestionType type;

        public QuizQuestion(String question, List<String> options, List<String> answer, QuestionType type) {
            this.question = question;
            this.options = options;
            this.answer = answer;
            this.type = type;
        }

        public String getQuestion() {
            return question;
        }

        public List<String> getOptions() {
            return options;
        }

        public List<String> getAnswer() {
            return answer;
        }

        public QuestionType getType() {
            return type;
        }
    }

    enum QuestionType {
        SINGLE, MULTIPLE
    }
}
