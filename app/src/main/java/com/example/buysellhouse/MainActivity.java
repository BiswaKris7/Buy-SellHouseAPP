package com.example.buysellhouse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView3;
    private String fullText = "Your Dream Home\nis just a\nHunt away!";
    private int index = 0;
    private long delay = 100; // Delay for typing effect in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView3 = findViewById(R.id.textView3);
        textView3.setText("");

        // Start typing animation
        startTypingAnimation();

        // Set up a 7-second delay before moving to LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Transition to LoginActivity after 5 seconds
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close MainActivity so the user can't return to it
            }
        }, 7000); // 7000 milliseconds = 7 seconds
    }

    private void startTypingAnimation() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (index < fullText.length()) {
                    textView3.setText(textView3.getText().toString() + fullText.charAt(index));
                    index++;
                    handler.postDelayed(this, delay);
                }
            }
        });
    }
}
