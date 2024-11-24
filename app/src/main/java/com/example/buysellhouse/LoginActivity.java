package com.example.buysellhouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginButton;
    TextView signupText, forgotPasswordText;
    DatabaseHelper databaseHelper;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_REMEMBER_ME = "remember_me";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private Switch rememberMeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupText = findViewById(R.id.signup_text);
        forgotPasswordText = findViewById(R.id.forgot_password);
        rememberMeSwitch = findViewById(R.id.remember_me);
        databaseHelper = new DatabaseHelper(this);

        // Ensure database is opened
        databaseHelper.getReadableDatabase();

        // Load saved credentials if remember me is checked
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
        if (rememberMe) {
            emailInput.setText(sharedPreferences.getString(KEY_EMAIL, ""));
            passwordInput.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
            rememberMeSwitch.setChecked(true);
        }

        // Login button click event
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (databaseHelper.checkUser(email, password)) {
                    if (rememberMeSwitch.isChecked()) {
                        // Save credentials
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_REMEMBER_ME, true);
                        editor.putString(KEY_EMAIL, email);
                        editor.putString(KEY_PASSWORD, password);
                        editor.apply();
                    } else {
                        // Clear saved credentials
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_REMEMBER_ME, false);
                        editor.remove(KEY_EMAIL);
                        editor.remove(KEY_PASSWORD);
                        editor.apply();
                    }
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,Dashboard.class);

                    startActivity(intent);
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Signup link click event
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Forgot password link click event
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
