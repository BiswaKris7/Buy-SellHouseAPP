package com.example.buysellhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailInput, newPasswordInput, confirmNewPasswordInput;
    Button resetPasswordButton;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI elements
        emailInput = findViewById(R.id.email_input);
        newPasswordInput = findViewById(R.id.new_password_input);
        confirmNewPasswordInput = findViewById(R.id.confirm_new_password_input);
        resetPasswordButton = findViewById(R.id.reset_password_button);
        databaseHelper = new DatabaseHelper(this);

        // Handle reset password button click
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String newPassword = newPasswordInput.getText().toString();
                String confirmNewPassword = confirmNewPasswordInput.getText().toString();

                // Check if passwords match
                if (newPassword.equals(confirmNewPassword)) {
                    // Update password in the database
                    if (databaseHelper.updatePassword(email, newPassword)) {
                        Toast.makeText(ForgotPasswordActivity.this, "Password reset successful!", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Failed to reset password. Check your email.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle back icon click
        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to LoginActivity
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close the activity
            }
        });
    }
}
