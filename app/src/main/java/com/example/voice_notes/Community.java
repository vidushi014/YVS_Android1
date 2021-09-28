package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Community extends AppCompatActivity {

    ImageView addBtn;
    EditText popupTitle, popupDescription;
    ProgressBar popupProgress;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private Uri pickedAudUri = null;
    File filePath = null;
    int Like = 0;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        mAuth=FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();

        //   intent input

        Intent intent=getIntent();

        Bundle bundle = getIntent().getExtras();
        Log.i("test1",intent.getSerializableExtra("FilePath").toString());

        filePath = (File) getIntent().getExtras().get("FilePath");

        iniPopup(filePath);

    }


    private void iniPopup(File file) {



        // ini popup widget

        addBtn = findViewById(R.id.imageView12);
        popupDescription = findViewById(R.id.editTextTextPersonName3);
        popupTitle = findViewById(R.id.editTextTextPersonName2);
        popupProgress = findViewById(R.id.progressBar);
        pickedAudUri  = Uri.fromFile(file);

        // add post click listener

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupProgress.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.INVISIBLE);

                if(!popupTitle.getText().toString().isEmpty() && !popupDescription.getText().toString().isEmpty()){
                    // first we need to upload audio file
                    //access firebase storage

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("SharedAudio");
                    StorageReference audioFilePath = storageReference.child(pickedAudUri.getLastPathSegment());
                    audioFilePath.putFile(pickedAudUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            audioFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String audiDownloadLink = uri.toString();
                                    // create post object

                                    post Post = new post(popupTitle.getText().toString(),
                                            popupDescription.getText().toString(),
                                            audiDownloadLink,
                                            currentUser.getUid(),Like);

                                    // add post to firebase database

                                    addPost(Post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //something goes wrong
                                    Toast.makeText(Community.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                    popupProgress.setVisibility(View.INVISIBLE);
                                    addBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                }
                else{
                    Toast.makeText(Community.this,"something went wrong",Toast.LENGTH_LONG).show();
                    popupProgress.setVisibility(View.INVISIBLE);
                    addBtn.setVisibility(View.VISIBLE);
                }

            }
        });




    }

    private void addPost(post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("posts").push();

        // get post unique id and update post key

        String key = myRef.getKey();
        post.setPostKey(key);

        // add post data to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Community.this,"Post added",Toast.LENGTH_LONG).show();
                popupProgress.setVisibility(View.INVISIBLE);
                addBtn.setVisibility(View.VISIBLE);
            }
        });


    }
}