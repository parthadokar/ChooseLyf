package com.example.chatbotui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import com.example.chatbotui.chatbot.helpers.GlobalVariables;
import com.example.chatbotui.chatbot.helpers.SharedPreferencesHelper;
import com.example.chatbotui.databinding.ActivitySplashBinding;
import com.example.chatbotui.login.LoginActivity;

public class splash extends AppCompatActivity implements AddProfileFragment.AddProfileFragmentListener {

    private final Runnable loadRunnable = () -> {
        SharedPreferencesHelper.loadUser(splash.this);
    };

    ActivitySplashBinding binding;

    private Thread loadThread;

    private final Runnable waitForLoadRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                loadThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                runOnUiThread(() -> new Handler().postDelayed(() -> {
                    if (GlobalVariables.getInstance().getCurrentUser().isPresent()) {
                        startMainActivity();
                    } else {
                        runLoginActivity();
                    }
                }, 4000));
            }
        }
    };

    private void startMainActivity() {
        Intent intent = new Intent(splash.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void runLoginActivity() {
        Intent intent = new Intent(splash.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(splash.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);*/

        this.loadThread = new Thread(this.loadRunnable);
        this.loadThread.start();
        new Thread(this.waitForLoadRunnable).start();
    }

    @Override
    public void callback(String result) {
    }
}


