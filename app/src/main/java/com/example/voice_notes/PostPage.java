package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PostPage extends AppCompatActivity {


    ListView postView;
    postAdapter PostAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private TextView ps_info ;
    private TextView ps_filename ;
    private ImageView play_btn ;
    private SeekBar seekbar;
    private Handler handler;
    private Runnable updateseekbar;
    private ImageView play_left;
    private ImageView play_right;
    private int position;
    private BottomSheetBehavior bottomSheetBehavior;
    List<post> postList = new ArrayList<>();
    Boolean isplaying = false;
    int place;
    private MediaPlayer mediaplayer;


//    private Context mContext;
//
//    private PostPage(Context mContext){
//        this.mContext=mContext;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);


        postView = findViewById(R.id.comView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("posts");
        place = -1;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<post> postList = new ArrayList<>();
                for (DataSnapshot postsnap:snapshot.getChildren()){
                    post Post = postsnap.getValue(post.class);
                    postList.add(Post);
                }
                PostAdapter = new postAdapter(getApplicationContext(),postList);
                postView.setAdapter(PostAdapter);

                postView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//                        Log.i("test123123",postList.get(i).getAudio());
                        position=i;
                        place=i;
                        if(isplaying){
                            stopaudio(i);
                            try {
                                startaudio(i);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            try {
                                startaudio(i);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    private void stopaudio(int position) {
                        isplaying=false;
                        mediaplayer.stop();
                        mediaplayer.release();
//                        play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play,null));
//                        ps_filename.setText(PostAdapter.mData.get(position).getTitle());
//                        ps_info.setText("Stopped");
//                        handler.removeCallbacks(updateseekbar);
                    }

                    private void startaudio(int i) throws IOException {


                        isplaying=true;
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        mediaplayer= new MediaPlayer();
                        try {
                            Log.i("test",postList.get(i).getAudio());
                            mediaplayer.setDataSource(postList.get(i).getAudio());
                            mediaplayer.prepare();
                            mediaplayer.start();

                        } catch (IOException e){
                            e.printStackTrace();
                        }
//                        play_btn.setImageDrawable(getResources().getDrawable(R.drawable.pause,null));
//                        ps_filename.setText(PostAdapter.mData.get(position).getTitle());
//                        ps_info.setText("playing");

//                        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mediaPlayer) {
//                                ps_info.setText("finished");
//                                play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play,null));
//                            }
//                        });

//                seekbar.setMax(mediaplayer.getDuration());
//                handler = new Handler();
//                updaterunnable();
//                handler.postDelayed(updateseekbar,0);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        // get list post from the database
//
//
//
//    }
}