package com.example.voice_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class activity_recording extends AppCompatActivity {
    private Button mRecordBtn;
    private TextView mRecordLable;
    private MediaRecorder recorder;
    private String fileName=null;
    private static final String LOG_TAG="Record_log";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        mRecordLable=findViewById(R.id.recordLable);
        mRecordBtn=findViewById(R.id.recordBtn);
        fileName= Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName+="/recorded_audio.3gp";

        mRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    startRecording();
                    mRecordLable.setText("Recording Started");
                }else if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    stopRecording();
                    mRecordLable.setText("Recording Stoped");
                }
                return false;
            }
        });
    }
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}