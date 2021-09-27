package com.example.chatbotui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ContentMainActivity extends AppCompatActivity {
    ImageView chatbot;
    ImageView bmiCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);

        chatbot = findViewById(R.id.chatbot);
        bmiCal = findViewById(R.id.bmiCal);

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),DiseasePrediction.class));
            }
        });

        bmiCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),BmiCalMainActivity.class));
            }
        });
    }
}