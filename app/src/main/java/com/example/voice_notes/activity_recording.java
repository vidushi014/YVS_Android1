package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class activity_recording extends AppCompatActivity {
    private ImageButton mRecordBtn;
    private ImageButton mstopbtn;
    private TextView mRecordLable;
    private MediaRecorder recorder;
    private String fileName=null;
    private Chronometer timer;
    private static final String LOG_TAG="Record_log";
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean isRecording=false;

    private static final String TAG = "Activity record";
//    REQUESTING PERMISSIONS FOR AUDIO AND STORAGE
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    // MAIN FUNCTION

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
//        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        mRecordLable=findViewById(R.id.recordLable);
        mRecordBtn=findViewById(R.id.recordBtn);
        mstopbtn=findViewById(R.id.imageButton1);
        timer=findViewById(R.id.recordtimer);
        if (isMicrophonePresent()){
            getMicrophonePermission();
        }
        mRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRecording){
                    stopRecording();
                    Toast.makeText(activity_recording.this, "Stopped", Toast.LENGTH_SHORT).show();
                    mRecordBtn.setImageDrawable(getResources().getDrawable(R.drawable.button));
                    mRecordLable.setText("Recording Stopped");
                    isRecording=false;
                }else{
                    try {
                        startRecording();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mRecordBtn.setImageDrawable(getResources().getDrawable(R.drawable.mic));
                    aniamte();
                    mRecordLable.setText("Recording Started");
                    Toast.makeText(activity_recording.this, "Started", Toast.LENGTH_SHORT).show();
                    isRecording=true;
                }
            }
        });
    }
    // START RECORDING CODE
    Uri audiouri;
    ParcelFileDescriptor file;
    private void startRecording() throws IOException {
        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Audio.Media.TITLE, fileName);
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (System.currentTimeMillis() / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "hiiibxd.mp3");
//        values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Recordings/");

        audiouri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        file = getContentResolver().openFileDescriptor(audiouri, "w");


        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        if (file != null) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(file.getFileDescriptor());
            recorder.setAudioChannels(1);
            recorder.prepare();
            recorder.start();
        }
    }

    // STOP RECORDING CODE
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        timer.stop();
    }
    // PRESENCE OF MICROPHONE
    private boolean isMicrophonePresent(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }else{
            return false;
        }
    }
    // Permission for microphone
    private void getMicrophonePermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }
    // Storing the file path
    private String getRecordingFilePath(){
        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
        File musicDirectory =contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now =new Date();

        File file=new File(musicDirectory,"Recording.."+formatter.format(now)+".3gp");
        Log.i(TAG, "getRecordingFilePath: "+file.getPath());
        return file.getPath();
    }
//    Adding animation to button
    public void aniamte(){
        mRecordBtn=findViewById(R.id.recordBtn);
        final Animation animation= AnimationUtils.loadAnimation(this,R.anim.bounce);
        mRecordBtn.startAnimation(animation);
    }
}