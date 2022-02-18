package com.example.chatbotui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.chatbotui.AddProfileFragment;
import com.example.chatbotui.MainActivity;
import com.example.chatbotui.ProfileActivity;
import com.example.chatbotui.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 10005;
    ImageView signIn;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    public static final String TAG = "TAG";
    EditText name, email, mobile, password;
    Button register;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    TextView login;
    boolean isNewUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        mobile = findViewById(R.id.editTextMobile);
        password = findViewById(R.id.editTextPassword);
        register = findViewById(R.id.cirRegisterButton);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        signIn = findViewById(R.id.signIn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("660583077227-goi1j8cti482ld6f31rt80agb5ho2ik7.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null || fAuth.getCurrentUser() != null) {
            Toast.makeText(this, "User is Logged in Already", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }
        signIn.setOnClickListener(v -> {
            Intent sign = signInClient.getSignInIntent();
            signInForResult.launch(sign);
            //startActivityForResult(sign, REQUEST_CODE);

        });
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        register.setOnClickListener(v -> {
            String fName = name.getText().toString().trim();
            String fEmail = email.getText().toString().trim();
            String fMobile = mobile.getText().toString().trim();
            String fPassword = password.getText().toString().trim();


            if (TextUtils.isEmpty(fName)) {
                name.setError("Name is Required");
                return;
            }
            if (TextUtils.isEmpty(fEmail)) {
                email.setError("Email is Required");
                return;
            }
            if (TextUtils.isEmpty(fMobile)) {
                mobile.setError("Mobile is Required");
                return;
            }
            if (TextUtils.isEmpty(fPassword)) {
                password.setError("Password is Required");
                return;
            }
            if (password.length() < 6) {
                password.setError("Password must be >= 6 character");
                return;
            }

            fAuth.createUserWithEmailAndPassword(fEmail, fPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Sent verification link

                    Toast.makeText(RegisterActivity.this, "User Created..", Toast.LENGTH_SHORT).show();
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", fName);
                    user.put("email", fEmail);
                    user.put("mobile", fMobile);
                    documentReference.set(user).addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user profile is created for" + userID));
                    Intent i = new Intent(RegisterActivity.this, ProfileActivity.class);
                    i.putExtra("key", isNewUser);
                    startActivity(i);
                } else {
                    Toast.makeText(RegisterActivity.this, "ERROR !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
        window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);

                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAcc.getIdToken(), null);

                fAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
                    Toast.makeText(getApplicationContext(), "Your Google Account is connected", Toast.LENGTH_SHORT).show();
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", fAuth.getCurrentUser().getDisplayName());
                    user.put("email", fAuth.getCurrentUser().getEmail());
                    user.put("mobile", fAuth.getCurrentUser().getPhoneNumber());
                    documentReference.set(user).addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user profile is created for" + userID));
                    Toast.makeText(this, "Your Google Account is connected", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this, ProfileActivity.class);
                    i.putExtra("key", isNewUser);
                    startActivity(i);
                }).addOnFailureListener( e -> { });


            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }*/

    ActivityResultLauncher<Intent> signInForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK){
                Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                try {
                    GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);

                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAcc.getIdToken(), null);

                    fAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
                        Toast.makeText(getApplicationContext(), "Your Google Account is connected", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", fAuth.getCurrentUser().getDisplayName());
                        user.put("email", fAuth.getCurrentUser().getEmail());
                        user.put("mobile", fAuth.getCurrentUser().getPhoneNumber());
                        documentReference.set(user).addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user profile is created for" + userID));
                        Toast.makeText(getApplicationContext(), "Your Google Account is connected", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RegisterActivity.this, ProfileActivity.class);
                        i.putExtra("key", isNewUser);
                        startActivity(i);
                    }).addOnFailureListener( e -> { });


                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

}