package com.example.chatbotui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatbotui.MainActivity;
import com.example.chatbotui.ProfileActivity;
import com.example.chatbotui.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 10005;
    public static final String TAG = "TAG";
    ImageView signIn;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    EditText email, password;
    Button login;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    TextView forgotpassword;
    boolean isNewUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.cirLoginButton);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        signIn = findViewById(R.id.googleSignIn);
        forgotpassword = findViewById(R.id.forgotPassword);

        //register.findViewById(R.id.change_to_register);
        //register.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View v) {
        //startActivity(new Intent(getApplication(),RegisterActivity.class));
        //}
        //});

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("660583077227-goi1j8cti482ld6f31rt80agb5ho2ik7.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null || fAuth.getCurrentUser() != null) {
            //Toast.makeText(this, "User is Logged in Already", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }
        signIn.setOnClickListener(v -> {
            Intent sign = signInClient.getSignInIntent();
            startActivityForResult(sign, REQUEST_CODE);
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText((v.getContext()));
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link. ");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.this,"Reset Link Sent To Your Email",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,"ERROR! Reset Link is not Sent" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        login.setOnClickListener(v -> {
            String fEmail = email.getText().toString().trim();
            String fPassword = password.getText().toString().trim();

            if (TextUtils.isEmpty(fEmail)) {
                email.setError("Email is Required");
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

            fAuth.signInWithEmailAndPassword(fEmail, fPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Logged in Successfully..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "ERROR !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    @Override
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
                    startActivity(new Intent(this, MainActivity.class));
                    Toast.makeText(this, "Your Google Account is connected", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                    i.putExtra("key", isNewUser);
                    startActivity(i);
                }).addOnFailureListener(e -> { });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void onRegisterClick(View View) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}