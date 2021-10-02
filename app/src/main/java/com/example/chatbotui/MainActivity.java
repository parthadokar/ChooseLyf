package com.example.chatbotui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.SavedStateHandle;

import com.example.chatbotui.bmi.BmiCalMainActivity;
import com.example.chatbotui.chatbot.ChatActivity;
import com.example.chatbotui.chatbot.RequestUtil;
import com.example.chatbotui.chatbot.helpers.GlobalVariables;
import com.example.chatbotui.chatbot.helpers.SharedPreferencesHelper;
import com.example.chatbotui.databinding.ActivityMainBinding;
import com.example.chatbotui.login.LoginActivity;
import com.example.chatbotui.reminder.ReminderMain;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements AddProfileFragment.AddProfileFragmentListener {

    ActivityMainBinding binding;
    private long pressedTime;
    boolean isNewUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setPicture();

        binding.content.chatbot.setOnClickListener(v -> {
            GlobalVariables.getInstance().setCurrentChat(null);
            RequestUtil.getInstance().resetEvidenceArray();
            RequestUtil.getInstance().resetConditionsArray();
            goToChatActivity();
        });

        binding.content.reminder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReminderMain.class);
            startActivity(intent);
        });

        binding.content.bmiCal.setOnClickListener(v -> startActivity(new Intent(getApplication(), BmiCalMainActivity.class)));

        //binding.content.barcode.setOnClickListener(v -> startActivity(new Intent(getApplication(), BarcodeActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.logout) {
            logout();
            return true;
        }
        if (item.getItemId() == R.id.profileImage) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("key", isNewUser);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnSuccessListener(unused -> startActivity(new Intent(getApplicationContext(), LoginActivity.class))).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "SignOut Failed", Toast.LENGTH_SHORT).show());
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    /*private void runAddProfileFragment(boolean isNewUser) {
        Fragment fragment = new AddProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(getString(R.string.is_new_user), isNewUser);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.layoutToBeReplacedWithFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/

    public void goToChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void setPicture() {
        if (!GlobalVariables.getInstance().getCurrentUser().isPresent()) {
            SharedPreferencesHelper.loadUser(this);
        }
    }

    @Override
    public void callback(String result) {
        goToMainActivity();
    }
}