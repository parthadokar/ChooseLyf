package com.example.chatbotui.HealthTips;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.chatbotui.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;

    private Button fever,burns,splinters,sprains,nosebleeds,cuts,bites,poison,stroke,kit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tips);

        fever = findViewById(R.id.feverId);
        burns = findViewById(R.id.burnsId);
        splinters = findViewById(R.id.splintersId);
        sprains = findViewById(R.id.sprainsId);
        nosebleeds = findViewById(R.id.nosebleedsId);
        cuts = findViewById(R.id.cutsId);
        bites = findViewById(R.id.bitesId);
        poison = findViewById(R.id.foodPoisonId);
        stroke = findViewById(R.id.strokeId);
        kit = findViewById(R.id.firstAidKitId);
        //about = findViewById(R.id.aboutId);

        fever.setOnClickListener(this);
        burns.setOnClickListener(this);
        splinters.setOnClickListener(this);
        sprains.setOnClickListener(this);
        nosebleeds.setOnClickListener(this);
        cuts.setOnClickListener(this);
        bites.setOnClickListener(this);
        poison.setOnClickListener(this);
        stroke.setOnClickListener(this);
        kit.setOnClickListener(this);
        //about.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.feverId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","Fever");
            startActivity(intent);

        }
        if(v.getId()== R.id.burnsId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","Burns");
            startActivity(intent);

        }
        if(v.getId()== R.id.splintersId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","Splinters");
            startActivity(intent);

        }
        if(v.getId()== R.id.sprainsId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","Sprains");
            startActivity(intent);

        }
        if(v.getId()== R.id.nosebleedsId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","Nosebleeds");
            startActivity(intent);

        }
        if(v.getId()== R.id.cutsId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","Cuts");
            startActivity(intent);

        }
        if(v.getId()== R.id.bitesId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","Bites");
            startActivity(intent);

        }
        if(v.getId()== R.id.foodPoisonId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","Poisoning");
            startActivity(intent);

        }
        if(v.getId()== R.id.strokeId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","Stroke");
            startActivity(intent);

        }
        if(v.getId()== R.id.firstAidKitId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","First");
            startActivity(intent);

        }
        /*if(v.getId()== R.id.aboutId){

            intent = new Intent(MainActivity.this,profileActivity.class);
            intent.putExtra("name","About");
            startActivity(intent);

        }*/


    }

}
