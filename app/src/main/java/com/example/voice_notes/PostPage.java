package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PostPage extends AppCompatActivity {


    ListView postRecyclerView;
    postAdapter PostAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<post> postList = new ArrayList<>();
//    private Context mContext;
//
//    private PostPage(Context mContext){
//        this.mContext=mContext;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);

        postRecyclerView = findViewById(R.id.comView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("posts");

//
//        postAdapter adapter = new postAdapter(postList);
//        postRecyclerView.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        // get list post from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 List<post> postList = new ArrayList<>();
                for (DataSnapshot postsnap:snapshot.getChildren()){
                    post Post = postsnap.getValue(post.class);
                    postList.add(Post);
                }
                PostAdapter = new postAdapter(getApplicationContext(),postList);
                postRecyclerView.setAdapter(PostAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}