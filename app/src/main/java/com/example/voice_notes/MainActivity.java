package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    ImageButton btnLogin;
    EditText etLogEmail;
    EditText etLogPassword;
    SignInButton btnSignIn;
    Button regHere;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN=123;


    @SuppressLint("WrongViewCast")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLogEmail = findViewById(R.id.editTextTextEmailAddress);
        etLogPassword = findViewById(R.id.editTextTextPassword);
        btnLogin = findViewById(R.id.imageButton);
        btnSignIn =findViewById(R.id.sign_in_button);
        regHere = findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();
        DbQuery.gFireStore = FirebaseFirestore.getInstance();

        requestGoogleSignIn();
        btnSignIn.setOnClickListener(view -> {
            signIn();
        });

        btnLogin.setOnClickListener(view -> {
            LoginUser();
        });

        regHere.setOnClickListener(view ->{
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
//            startActivity(new Intent(this, MainActivity.class));
//        }
//        else {
            startActivity(new Intent(MainActivity.this, activity_recording.class));
        }
    }


    private void LoginUser() {
        String email = etLogEmail.getText().toString();
        String password = etLogPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etLogEmail.setError("Email cannot be empty");
            etLogEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etLogPassword.setError("Enter password");
            etLogPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "user Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, activity_recording.class));
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

//    public void linkToRegister(View v) {
//        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, RegistrationActivity.class);
//        startActivity(intent);
//    }
//    public void linkToRecorder(View v) {
//        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, activity_recording.class);
//        startActivity(intent);
//    }

//         google authentication


    private void requestGoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                SharedPreferences.Editor editor=getApplicationContext()
                        .getSharedPreferences("MyPrefs",MODE_PRIVATE)
                        .edit();
                editor.putString("username",account.getDisplayName());
                editor.putString("useremail", account.getEmail());
                editor.putString("Userphoto",account.getPhotoUrl().toString());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "user Login successful", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();

                            if (task.getResult().getAdditionalUserInfo().isNewUser())
                            {
                                DbQuery.createUserdata(user.getDisplayName(),user.getEmail(), new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        startActivity(new Intent(MainActivity.this, activity_recording.class));
                                    }

                                    @Override
                                    public void onFailure() {

                                        Toast.makeText(MainActivity.this, "Something went wrong please try again after some time", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}