package com.example.chatbotui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.chatbotui.bmi.BmiCalMainActivity;
import com.example.chatbotui.chatbot.ChatActivity;
import com.example.chatbotui.chatbot.RequestUtil;
import com.example.chatbotui.chatbot.helpers.GlobalVariables;
import com.example.chatbotui.chatbot.helpers.SharedPreferencesHelper;
import com.example.chatbotui.databinding.ActivityMainBinding;
import com.example.chatbotui.hospital.GMap.ListHealthCenters;
import com.example.chatbotui.login.LoginActivity;
import com.example.chatbotui.Reminder_.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements AddProfileFragment.AddProfileFragmentListener {

    ActivityMainBinding binding;
    private long pressedTime;
    boolean isNewUser = false;
    public static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        requestLocationPermission();

        setPicture();

        binding.content.chatbot.setOnClickListener(v -> {
            GlobalVariables.getInstance().setCurrentChat(null);
            RequestUtil.getInstance().resetEvidenceArray();
            RequestUtil.getInstance().resetConditionsArray();
            goToChatActivity();
        });

        binding.content.reminder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        binding.content.bmiCal.setOnClickListener(v -> startActivity(new Intent(getApplication(), BmiCalMainActivity.class)));

        binding.content.barcode.setOnClickListener(v -> startActivity(new Intent(getApplication(), com.example.chatbotui.HealthTips.MainActivity.class)));

        binding.content.hospital.setOnClickListener(v -> {
            hospitalLocations();
        });
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
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    public void goToChatActivity() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
        } else
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
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

    private void hospitalLocations() {
        if (isNetworkAvailable()) {
            loading("Scanning Location...");
            Intent intent = new Intent(MainActivity.this, ListHealthCenters.class);
            startActivity(intent);
        } else
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void loading(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this,
                        "Without the location permission we will be unable to show the hospital list",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void callback(String result) {
        goToMainActivity();
    }
}