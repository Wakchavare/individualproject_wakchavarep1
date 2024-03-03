package com.example.individualproject_wakchavarep1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class HistoryActivity extends AppCompatActivity {

    private Button takeQuizButton;
    private TableLayout historyTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        takeQuizButton = findViewById(R.id.takeQuizButton);
        historyTable = findViewById(R.id.historyTable);

        takeQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, RulesActivity.class));
                finish(); // Optional, depending on your navigation flow
            }
        });

        // Display the user's history
        displayHistory();
    }

    private void displayHistory() {
        // Retrieve user's history from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_history", MODE_PRIVATE);
        Map<String, Integer> historyMap = new TreeMap<>(Collections.reverseOrder());
        historyMap.putAll((Map<String, Integer>) sharedPreferences.getAll());

        for (Map.Entry<String, Integer> entry : historyMap.entrySet()) {
            String time = entry.getKey();
            int score = (int) entry.getValue();
            System.out.println("######"+ score+"     "+time);

            // Create a new row in the table
            TableRow row = new TableRow(this);

            // Add time TextView
            TextView timeTextView = new TextView(this);
            timeTextView.setText(time);
            timeTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            timeTextView.setBackgroundResource(R.drawable.cell_border);
            row.addView(timeTextView);

            // Add score TextView
            TextView scoreTextView = new TextView(this);
            scoreTextView.setText(String.valueOf(score));
            scoreTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            scoreTextView.setBackgroundResource(R.drawable.cell_border);
            row.addView(scoreTextView);


            // Add the row to the table
            historyTable.addView(row);
        }
    }


}

