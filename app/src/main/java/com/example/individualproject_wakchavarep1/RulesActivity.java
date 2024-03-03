package com.example.individualproject_wakchavarep1;

import android.content.Intent;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        // Find the "Next" button by its ID
        Button nextButton = findViewById(R.id.nextButton);

        // Set click listener to the "Next" button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the QuestionsActivity
                startActivity(new Intent(RulesActivity.this, QuestionsActivity.class));
                finish(); // Optional, to close the current activity
            }
        });
    }
}

