package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class listofcategory extends AppCompatActivity {

    private ConstraintLayout playersheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private ListView listView;
    ArrayList<String> items= new ArrayList<>();
    ArrayList<File> all_files= new ArrayList<>();
    MediaPlayer mediaplayer;
    private boolean isplaying;
    private TextView ps_info ;
    private TextView ps_filename ;
    private ImageView play_btn ;
    private SeekBar seekbar;
    private Handler handler;
    private Runnable updateseekbar;
    private int place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofcategory);
        listView = findViewById(R.id.category_list);

        place=-1;
        isplaying=false;
        ps_info= findViewById(R.id.ps_info);
        ps_filename= findViewById(R.id.ps_filename);
        play_btn= findViewById(R.id.ps_play_btn);
        seekbar= findViewById(R.id.ps_seekbar);


        Intent intent=getIntent();

        Bundle bundle = getIntent().getExtras();

        File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/voicenotes/" + bundle.getString("category"));
        if(!filepath.exists()){
            filepath.mkdir();
        }
        getfiles(filepath);

        customadapter myadapter= new customadapter(this, 0,all_files);
        listView.setAdapter(myadapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                try {
//                    mediaplayer = new MediaPlayer();
//                    mediaplayer.setDataSource(all_files.get(i).getPath());
//                    mediaplayer.prepare();
//                    mediaplayer.start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                place=i;
                if(isplaying){
                    stopaudio(i);
                    try {
                        startaudio(all_files.get(i),i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        startaudio(all_files.get(i),i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            private void stopaudio(int position) {
                isplaying=false;
                mediaplayer.stop();
                play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play,null));
                ps_filename.setText(items.get(position));
                ps_info.setText("Stopped");
                handler.removeCallbacks(updateseekbar);
            }

            private void startaudio(File file,int position) throws IOException {
                isplaying=true;

                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
                mediaplayer= new MediaPlayer();
                try {
                    mediaplayer.setDataSource(all_files.get(position).getAbsolutePath());
                    mediaplayer.prepare();
                    mediaplayer.start();

                } catch (IOException e){
                    e.printStackTrace();
                }
                play_btn.setImageDrawable(getResources().getDrawable(R.drawable.pause,null));
                ps_filename.setText(items.get(position));
                ps_info.setText("playing");

                mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        ps_info.setText("finished");
                        play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play,null));
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
                    }
                }
            }
        });
        playersheet=findViewById(R.id.playersheet);
        bottomSheetBehavior= BottomSheetBehavior.from(playersheet);

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

    public void getfiles(File file) {
        File files[] = file.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                getfiles(files[i]);
            }
            else{
                items.add(files[i].getName());
                all_files.add(files[i]);
            }
        }
    }
}