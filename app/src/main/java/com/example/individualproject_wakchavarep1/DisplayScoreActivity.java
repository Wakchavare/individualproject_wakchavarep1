package com.example.individualproject_wakchavarep1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayScoreActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_score);

        scoreTextView = findViewById(R.id.score_text_view);
        sharedPreferences = getSharedPreferences("user_score", MODE_PRIVATE);

        // Retrieve the user's score from SharedPreferences
        int userScore = sharedPreferences.getInt("user_score", 0);

        // Display the user's score
        scoreTextView.setText("Your Score: " + userScore);
    }
}
