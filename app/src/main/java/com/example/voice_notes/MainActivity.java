package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText etLogEmail;
    EditText etLogPassword;
    ImageButton btnLogin;

    private FirebaseAuth mAuth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLogEmail = findViewById(R.id.editTextTextEmailAddress);
        etLogPassword = findViewById(R.id.editTextTextPassword);
        btnLogin = findViewById(R.id.imageButton);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            LoginUser();
        });

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
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "user Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void linkToRegister(View v) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);

    }


}