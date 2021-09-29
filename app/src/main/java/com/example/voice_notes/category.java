package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class category extends AppCompatActivity {

//    for firebase authentication
    private FirebaseAuth mAuth;

//    our logout variable
    Button logoutB;
    public ProgressBar pgBar;

//   our grid variable
    private GridView catView;
    MediaPlayer mediaplayer;
//    our list variable to get list from firebase using our 2 self created java classes i.e categoryModel and categoryAdaptor
    private List<CategoryModel> catList = new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        catView = findViewById(R.id.cat_Grid);
        logoutB = findViewById(R.id.LogOut);
        logoutB.setBackgroundColor(R.color.pink);
        pgBar = findViewById(R.id.progressBar2);

        loadCategory();
        CategoryAdaptor adapter = new CategoryAdaptor(catList);
        catView.setAdapter(adapter);

        catView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(category.this,listofcategory.class);
                intent.putExtra("category",catList.get(i).getName());
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        logoutB.setOnClickListener(view ->{
            mAuth.signOut();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            GoogleSignInClient mGoogleClient = GoogleSignIn.getClient(getBaseContext(),gso);

            mGoogleClient.signOut();

            startActivity(new Intent(category.this , MainActivity.class));
        });


    }
    public void linktoCommunity(View view) {
        Intent intent = new Intent(this, PostPage.class);
        startActivity(intent);
        pgBar.setVisibility(View.VISIBLE);
    }

    private void loadCategory(){
        catList.clear();
        catList.add(new CategoryModel("1","Favourites",10,R.drawable.devil));
        catList.add(new CategoryModel("2","Ideas",20,R.drawable.idea));
        catList.add(new CategoryModel("3","Local",15,R.drawable.candidate));
        catList.add(new CategoryModel("4","Work",10,R.drawable.shared));
        catList.add(new CategoryModel("5","Shared",40,R.drawable.smartphone));
        catList.add(new CategoryModel("6","Personal",30,R.drawable.home));
    }

}