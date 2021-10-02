package com.example.chatbotui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProfileActivity extends AppCompatActivity implements AddProfileFragment.AddProfileFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        boolean isNewUser = intent.getBooleanExtra("key", false);

        loadFragment(isNewUser);

    }

    @Override
    public void onPause() {
        super.onPause();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void loadFragment(boolean isNewUser) {
        Fragment fragment = new AddProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(getString(R.string.is_new_user), isNewUser);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.FragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void callback(String result) {
        goToMainActivity();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
}