package com.example.chatbotui.waterIntake;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.example.chatbotui.R;
import com.example.chatbotui.waterIntake.HydrationTrackerActivity;

public class MainActivity extends AppCompatActivity {

    /* --------------------------------------------------- onCreate ------------------------------------------------ */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /* --------------------------------------------------- Actual functions ----------------------------------------- */


    public void openHydrationSetting(MenuItem item)
    {
        Intent intent = new Intent(this, HydrationSettingActivity.class);
        startActivity(intent);
    }




}
