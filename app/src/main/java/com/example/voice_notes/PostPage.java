package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    public ProgressBar pgBar;
    private TextView ps_info ;
    private ConstraintLayout playersheet;
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
        ps_info= findViewById(R.id.ps_info);
        ps_filename= findViewById(R.id.ps_filename);
        play_btn= findViewById(R.id.ps_play_btn);
        seekbar= findViewById(R.id.ps_seekbar);
        play_left = findViewById(R.id.ps_imageView10);
        play_right = findViewById(R.id.ps_imageView12);
        pgBar= findViewById(R.id.progressBar2);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<post> postList = new ArrayList<>();
                for (DataSnapshot postsnap:snapshot.getChildren()){
                    post Post = postsnap.getValue(post.class);
                    postList.add(Post);
                    pgBar.setVisibility(View.INVISIBLE);

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
                        play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play,null));
                        ps_filename.setText(PostAdapter.mData.get(position).getTitle());
                        ps_info.setText("Stopped");
                        handler.removeCallbacks(updateseekbar);
                    }

                    private void startaudio(int i) throws IOException {


                        isplaying=true;
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        mediaplayer= new MediaPlayer();
                        try {
                            Log.i("test",postList.get(i).getAudio());
                            mediaplayer.setDataSource(postList.get(i).getAudio());
                            mediaplayer.prepare();
                            mediaplayer.start();

                        } catch (IOException e){
                            e.printStackTrace();
                        }
                        play_btn.setImageDrawable(getResources().getDrawable(R.drawable.pause,null));
                        ps_filename.setText(PostAdapter.mData.get(position).getTitle());
                        ps_info.setText("playing");

                        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                ps_info.setText("finished");
                                play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play,null));
                                isplaying=false;
                            }
                        });

                        seekbar.setMax(mediaplayer.getDuration());
                        handler = new Handler();
                        updaterunnable();
                        handler.postDelayed(updateseekbar,0);
                    }
                });
                seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mediaplayer.pause();
                        play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play,null));
                        isplaying=false;
                        handler.removeCallbacks(updateseekbar);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if(place!=-1) {
                            int progress= seekBar.getProgress();
                            mediaplayer.seekTo(progress);
                            mediaplayer.start();
                            play_btn.setImageDrawable(getResources().getDrawable(R.drawable.pause, null));
                            isplaying = true;
                            updaterunnable();
                            handler.postDelayed(updateseekbar,0);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        playersheet=findViewById(R.id.playersheet);
        bottomSheetBehavior=BottomSheetBehavior.from(playersheet);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isplaying){
                    mediaplayer.pause();
                    play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play,null));
                    isplaying=false;
                    handler.removeCallbacks(updateseekbar);
                }
                else{
                    if(place!=-1) {
                        mediaplayer.start();
                        play_btn.setImageDrawable(getResources().getDrawable(R.drawable.pause, null));
                        isplaying = true;
                        updaterunnable();
                        handler.postDelayed(updateseekbar,0);
                    }else{
                        Toast.makeText(PostPage.this, "Choose a file to play", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }
    private void updaterunnable(){
        updateseekbar= new Runnable() {
            @Override
            public void run() {
                seekbar.setProgress(mediaplayer.getCurrentPosition());
                handler.postDelayed(this,50);
            }
        };
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