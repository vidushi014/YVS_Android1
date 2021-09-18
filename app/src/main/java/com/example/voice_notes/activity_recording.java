package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class activity_recording extends AppCompatActivity {
    private ImageButton mRecordBtn;
    private TextView mRecordLable;
    private MediaRecorder recorder;
    private String fileName=null;
    private static final String LOG_TAG="Record_log";
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

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
//        fileName= Environment.getExternalStorageDirectory().getAbsolutePath();
//        fileName+="/recorded_audio1.3gp";
        if (isMicrophonePresent()){
            getMicrophonePermission();
        }
        mRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    startRecording();
                    mRecordLable.setText("Recording Started");
                }else if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    stopRecording();
                    mRecordLable.setText("Recording Stopped");
                }
                return false;
            }
        });
    }
    // START RECORDING CODE

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(getRecordingFilePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed"+e);
        }


    }
// STOP RECORDING CODE
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
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
        File file=new File(musicDirectory,"record.mp3");
        Log.i(TAG, "getRecordingFilePath: "+file.getPath());
        return file.getPath();
    }
}