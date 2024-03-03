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
    private LinearLayout mainLayout;
    private Button submitButton;
    private int userScore = 0;
    private int currentQuestionIndex = 0;
    private RadioGroup currentRadioGroup;
    private LinearLayout currentCheckBoxLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        mainLayout = findViewById(R.id.main_layout);
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

        TextView questionText = new TextView(this);
        questionText.setText(question.getQuestion());
        mainLayout.addView(questionText);

        if (question.getType() == QuestionType.SINGLE) {
            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            for (String option : question.getOptions()) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                radioGroup.addView(radioButton);
            }
            mainLayout.addView(radioGroup);

            // Set the currentRadioGroup to the newly created radio group
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
            mainLayout.addView(checkBoxLayout);

            // Set the currentCheckBoxLayout to the newly created checkbox layout
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
                // Continue with answer checking
                checkSelectedOption();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // User cancels, do nothing or provide feedback
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void checkSelectedOption() {
        System.out.println("############ Function Called: " + this.userScore);
        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);

        if (currentRadioGroup != null) {
            int radioButtonID = currentRadioGroup.getCheckedRadioButtonId();
            View radioButton = currentRadioGroup.findViewById(radioButtonID);
            int idx = currentRadioGroup.indexOfChild(radioButton);
            RadioButton r = (RadioButton) currentRadioGroup.getChildAt(idx);
            String selectedText = r.getText().toString();
            System.out.println("############ " + currentQuestionIndex + " : " + selectedText);
            if (currentQuestion.getAnswer().contains(selectedText)) {
                // Correct Answer!
                this.userScore++;
                System.out.println("############ Correct Answer Radio Button: " + selectedText);
            } else {
                // Incorrect Answer!
            }
        } else if (currentCheckBoxLayout != null) {
            LinearLayout checkBoxLayout = currentCheckBoxLayout;
            int correctChoices = 0;
            for (int i = 0; i < checkBoxLayout.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) checkBoxLayout.getChildAt(i);
                if (checkBox.isChecked() && currentQuestion.getAnswer().contains(checkBox.getText().toString())) {
                    correctChoices++;

                } else if (checkBox.isChecked() && !currentQuestion.getAnswer().contains(checkBox.getText().toString())) {
                    // If a wrong choice is selected
                    // Incorrect Answer!
                }
            }
            if (correctChoices == currentQuestion.getAnswer().size()) {
                // Correct Answer!
                this.userScore++; // Increment the score for correct answers
                System.out.println("############ Correct Answer CheckBox: ");
            } else {
                // Incorrect Answer!
            }
        }

        // Save the user's score
        saveUserScore(this.userScore);

        // Remove the views related to the current question
        mainLayout.removeViewAt(mainLayout.getChildCount() - 1); // Remove submit button
        mainLayout.removeViewAt(mainLayout.getChildCount() - 1); // Remove question view

        // Move to the next question
        currentQuestionIndex++;

        // Display next question if available
        if (currentQuestionIndex < quizQuestions.size()) {
            displayQuestion(currentQuestionIndex);
        } else {
            // End of questions
            Toast.makeText(this, "End of questions", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QuestionsActivity.this, DisplayScoreActivity.class);
            startActivity(intent);
            finish(); // Finish the current activity
            // Save user history
            saveUserHistory();
        }
    }

    private void saveUserScore(int score) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_score", score);
        editor.apply();
    }
    private void saveUserHistory() {
        // Get current date and time
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Save user history
        saveUserHistory(currentTime, this.userScore);
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
