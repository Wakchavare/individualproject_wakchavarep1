//package com.example.individualproject_wakchavarep1;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class LoginOrRegisterActivity extends AppCompatActivity {
//
//    private EditText editTextEmail, editTextPassword;
//    private Button btnLogin, btnRegister;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_or_register);
//
//        editTextEmail = findViewById(R.id.editTextEmail);
//        editTextPassword = findViewById(R.id.editTextPassword);
//        btnLogin = findViewById(R.id.btn_login);
//        btnRegister = findViewById(R.id.btn_register);
//
//        // Set click listener for login button
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = editTextEmail.getText().toString().trim();
//                String password = editTextPassword.getText().toString().trim();
//
//                // Check if email and password are empty
//                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//                    Toast.makeText(LoginOrRegisterActivity.this, "Please provide your credentials", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Check for a dummy login, assuming email and password are correct
//                    if (email.equals("user@example.com") && password.equals("password")) {
//                        // Show login successful message
//                        Toast.makeText(LoginOrRegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Show login failed message
//                        Toast.makeText(LoginOrRegisterActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//
//        // Set click listener for register button
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to the register page
//                startActivity(new Intent(LoginOrRegisterActivity.this, RegisterActivity.class));
//            }
//        });
//    }
//}

package com.example.individualproject_wakchavarep1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginOrRegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin, btnRegister;

    // SharedPreferences for storing user data
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        // Set click listener for login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if email and password are empty
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginOrRegisterActivity.this, "Please provide your credentials", Toast.LENGTH_SHORT).show();
                } else {
                    // Retrieve stored credentials
                    String storedEmail = sharedPreferences.getString(KEY_EMAIL, "");
                    String storedPassword = sharedPreferences.getString(KEY_PASSWORD, "");

                    // Check if input credentials match stored credentials
                    if (email.equals(storedEmail) && password.equals(storedPassword)) {
                        // Login successful
                        Toast.makeText(LoginOrRegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        // Start the ActivityRulesActivity
                        startActivity(new Intent(LoginOrRegisterActivity.this, HistoryActivity.class));

                        // Finish the current activity (optional)
                        finish();
                    } else {
                        // Login failed
                        Toast.makeText(LoginOrRegisterActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Set click listener for register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the register page
                startActivity(new Intent(LoginOrRegisterActivity.this, RegisterActivity.class));
            }
        });
    }
}
