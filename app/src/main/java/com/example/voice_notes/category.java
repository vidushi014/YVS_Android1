package com.example.voice_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class category extends AppCompatActivity {

//    for firebase authentication
    FirebaseAuth mAuth;

//    our logout variable
    Button logoutB;

//   our grid variable
    private GridView catView;
//    our list variable to get list from firebase using our 2 self created java classes i.e categoryModel and categoryAdaptor
    private List<CategoryModel> catList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        catView = findViewById(R.id.cat_Grid);
        logoutB = findViewById(R.id.LogOut);

        loadCategory();
        CategoryAdaptor adapter = new CategoryAdaptor(catList);
        catView.setAdapter(adapter);



        logoutB.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(category.this , MainActivity.class));
        });

    }

    private void loadCategory(){
        catList.clear();
        catList.add(new CategoryModel("1","favourites",10));
        catList.add(new CategoryModel("2","office",15));
        catList.add(new CategoryModel("3","personal",30));
        catList.add(new CategoryModel("4","ideas",20));
        catList.add(new CategoryModel("5","uploaded",10));
        catList.add(new CategoryModel("6","saved",40));
    }

}