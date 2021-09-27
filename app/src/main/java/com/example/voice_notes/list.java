package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContextWrapper;
import android.content.DialogInterface;
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
import java.lang.reflect.Array;
import java.util.ArrayList;

public class list extends AppCompatActivity  {

    private ListView listview;
    private MediaPlayer mediaplayer;
    private ConstraintLayout playersheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean isplaying;
    private TextView ps_info ;
    private TextView ps_filename ;
    private ImageView play_btn ;
    private SeekBar seekbar;
    private Handler handler;
    private Runnable updateseekbar;
    private int place;
    ArrayList<String> items= new ArrayList<>();
    ArrayList<File> allfiles= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ui);

        place=-1;
        isplaying=false;
        ps_info= findViewById(R.id.ps_info);
        ps_filename= findViewById(R.id.ps_filename);
        play_btn= findViewById(R.id.ps_play_btn);
        seekbar= findViewById(R.id.ps_seekbar);

        File filepath= new File(Environment.getExternalStorageDirectory().getPath()+"/voicenotes");
        listview=findViewById(R.id.list);

        traverser(filepath);
//        ArrayAdapter<String> myadapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        customadapter myadapter = new customadapter(getApplicationContext(),0,items);
        listview.setAdapter(myadapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                place=i;
                    if(isplaying){
                        stopaudio(i);
                        try {
                            startaudio(allfiles.get(i),i);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            startaudio(allfiles.get(i),i);
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
                    mediaplayer.setDataSource(allfiles.get(position).getAbsolutePath());
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
        // FOR LONG PRESSS

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){


            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                removeItemFromList(i);
//                items.remove(i);
                return true;
            }
            // method to remove list item
            protected void removeItemFromList(int position) {
                final int deletePosition = position;

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        list.this);

                alert.setTitle("Delete");
                alert.setMessage("Do you want delete this item?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TOD O Auto-generated method stub

                        // main code on after clicking yes
                        items.remove(deletePosition-1);
                        myadapter.notifyDataSetChanged();
//                        myadapter.notifyDataSetInvalidated();

                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                alert.show();
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

//    public File getfile(String str){
//        File[] recording_files= music_dir().listFiles();
//        for(int i=0;i<recording_files.length;i++){
//            if(recording_files[i].getName().toString().endsWith(str)){
//                return recording_files[i];
//            }
//        }
//        return recording_files[0];
//    }
//
//    public File music_dir(){
//        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
//        File musicDirectory =contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//        return musicDirectory;
//    }
//    private ArrayList<String>findrecordings(File file){
//        ArrayList<String> items=new ArrayList<>();
//        File files[]=file.listFiles();
//        for(File singlefile:files){
//            if(singlefile.isDirectory() && !singlefile.isHidden()){
//                findrecordings(singlefile);
//            }
//            else{
//                items.add(singlefile.getName().toString());
//            }
//        }
//        return items;
//    }

//    private ArrayList<String> displayList() {
//        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
//        File musicDirectory =contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//
//        File[] recording_files = musicDirectory.listFiles() ;
//        ArrayList<String> items=new ArrayList<>() ;
//        for(int i=0;i<recording_files.length;i++){
//            items.add(recording_files[i].getName().toString());
//            Log.i("lololol",items.get(i));
//        }
//        return items;
//    }

    void traverser (File file){
        File files[]= file.listFiles();

        for(int i=0;i<files.length;i++){
            if(files[i].isDirectory()){
                traverser(files[i]);
            }
            else{
                items.add(files[i].getName());
                allfiles.add(files[i]);
            }
        }
    }
/// code for media player



}