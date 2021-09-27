package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText etRegEmail;
    EditText etRegPassword;
    EditText etRegName;
    EditText etRegConfPass;
    ImageButton btnRegister;
    Button reLogin;

    FirebaseAuth mAuth;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etRegEmail = findViewById(R.id.editTextTextEmailAddress2);
        etRegPassword = findViewById(R.id.editTextTextPassword2);
        etRegName = findViewById(R.id.editTextTextPersonName);
        etRegConfPass = findViewById(R.id.editTextTextPassword3);
        btnRegister = findViewById(R.id.imageButton2);
        reLogin = findViewById(R.id.Button2);


        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        reLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
        });
    }
    private void createUser(){
        String email = etRegEmail.getText().toString() ;
        String password = etRegPassword.getText().toString();
        String Name = etRegName.getText().toString();
        String confpass = etRegConfPass.getText().toString();

        if(TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }

        else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Enter password");
            etRegPassword.requestFocus();
        }

        else if (TextUtils.isEmpty(confpass)){
            etRegConfPass.setError("please confirm password");
            etRegConfPass.requestFocus();
        }
        else if(!confpass.equals(password)){
            etRegConfPass.setError("password don't match");
            etRegConfPass.requestFocus();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegistrationActivity.this, "user registered successfully", Toast.LENGTH_SHORT).show();

                        DbQuery.createUserdata(Name,email, new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(RegistrationActivity.this,"something went wrong. Please try again later",Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                    else{
                        Toast.makeText(RegistrationActivity.this, "Registration failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

//    public void linkToLogin(View v) {
//        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//
//    }
}