package com.example.chatbotui.HealthTips;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.chatbotui.R;

public class profileActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tips);

        imageView = findViewById(R.id.imageViewId);
        textView = findViewById(R.id.textViewId);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){

            String topicName = bundle.getString("name");
            showDetails(topicName);

        }

    }

    void showDetails(String topicName){

        if(topicName.equals("Fever")){

            imageView.setImageResource(R.drawable.fever_im);
            textView.setText(R.string.First_Aid_for_Fever);

        }
        if(topicName.equals("Burns")){

            imageView.setImageResource(R.drawable.burn_hand);
            textView.setText(R.string.Minor_Burns);

        }
        if(topicName.equals("Splinters")){

            imageView.setImageResource(R.drawable.splinters_im);
            textView.setText(R.string.Splinters);

        }
        if(topicName.equals("Sprains")){

            imageView.setImageResource(R.drawable.sprains_strains_im);
            textView.setText(R.string.Sprains_and_Strains);

        }
        if(topicName.equals("Nosebleeds")){

            imageView.setImageResource(R.drawable.bleedsnose);
            textView.setText(R.string.Nosebleeds);

        }
        if(topicName.equals("Cuts")){

            imageView.setImageResource(R.drawable.cuts_scrapes_children_first_aid);
            textView.setText(R.string.Cuts_and_Scrapes);

        }
        if(topicName.equals("Bites")){

            imageView.setImageResource(R.drawable.insect_stings);
            textView.setText(R.string.Animal_Bites_and_Insect_Stings);

        }
        if(topicName.equals("Poisoning")){

            imageView.setImageResource(R.drawable.food_poisoning);
            textView.setText(R.string.Food_Poisoning_Treatment);

        }
        if(topicName.equals("Stroke")){

            imageView.setImageResource(R.drawable.stroke_im);
            textView.setText(R.string.First_Aid_for_Stroke);

        }
        if(topicName.equals("First")){

            imageView.setImageResource(R.drawable.kits_first);
            textView.setText(R.string.What_should_be_in_my_first_aid_kit);

        }
        /*if(topicName.equals("About")){

            imageView.setImageResource(R.drawable.about__us);
            textView.setText(R.string.aboutUs);

        }*/

    }
}
