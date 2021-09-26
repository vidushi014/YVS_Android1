package com.example.voice_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class activity_recording extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;

    private ImageButton mRecordBtn;
    private ImageButton mstopbtn;
    private TextView mRecordLable;
    private MediaRecorder recorder;
    private String filename=null;
    private Chronometer timer;
    private static final String LOG_TAG="Record_log";
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean isRecording=false;
    private static final String TAG = "Activity record";
    private MediaPlayer mediaplayer;
    private Spinner spinner;

//    for firebase storage

    private StorageReference mStorage;
    private ProgressDialog mProgress;

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

        mAuth = FirebaseAuth.getInstance();
        DbQuery.gFireStore = FirebaseFirestore.getInstance();

//        for audio storage in firebase

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);


//        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        mRecordLable=findViewById(R.id.recordLable);
        mRecordBtn=findViewById(R.id.recordBtn);
        mstopbtn=findViewById(R.id.imageButton1);
        timer=findViewById(R.id.recordtimer);


        if (isMicrophonePresent()){
            getMicrophonePermission();
        }


        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.R){
            if(Environment.isExternalStorageManager()){
                Toast.makeText(this,"Access grated",Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent= new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri=Uri.fromParts("package",this.getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
                Toast.makeText(this,"nai hua re",Toast.LENGTH_SHORT).show();
            }
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
                    startRecording();
                    mRecordBtn.setImageDrawable(getResources().getDrawable(R.drawable.mic));
                    aniamte();
                    mRecordLable.setText("Recording Started");
                    Toast.makeText(activity_recording.this, "Started", Toast.LENGTH_SHORT).show();
                    isRecording=true;
                }
            }
        });

//        CODE FOR SPINNER

        spinner=findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        String[] text=getResources().getStringArray(R.array.location);
        ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,text);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


//     START RECORDING CODE
//    Uri audiouri;
//    ParcelFileDescriptor file;
//    private void startRecording() throws IOException {
//        ContentValues values = new ContentValues(4);
//        values.put(MediaStore.Audio.Media.TITLE, filename);
//        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (System.currentTimeMillis() / 1000));
//        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
//        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "hiiibxd.mp3");
////        values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Recordings/");
//
//        audiouri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
//        file = getContentResolver().openFileDescriptor(audiouri, "w");
//
//        timer.setBase(SystemClock.elapsedRealtime());
//        timer.start();
//        if (file != null) {
//            recorder = new MediaRecorder();
//            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//            recorder.setOutputFile(getRecordingFilePath());
//            recorder.setAudioChannels(1);
//            recorder.prepare();
//            recorder.start();
//        }
//    }

//    private void startRecording(){
//        String recordpath =getExternalFilesDir("/").getAbsolutePath();
//        filename="recording.3gp";
//        recorder=new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        recorder.setOutputFile(recordpath+"/"+filename);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//
//        try {
//            recorder.prepare();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        recorder.start();
//    }

    // START RECORDING CODE
//     Uri audiouri;
//     ParcelFileDescriptor file;
//     private void startRecording() throws IOException {
//         ContentValues values = new ContentValues(4);
//         values.put(MediaStore.Audio.Media.TITLE, fileName);
//         values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (System.currentTimeMillis() / 1000));
//         values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
//         values.put(MediaStore.Audio.Media.DISPLAY_NAME, "hiiibxd.mp3");


//        values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Recordings/");

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(getRecordingFilePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {

         timer.setBase(SystemClock.elapsedRealtime());
         timer.start();
//         if (file != null) {
//             recorder = new MediaRecorder();
//             recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//             recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//             recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//             recorder.setOutputFile(file.getFileDescriptor());
//             recorder.setAudioChannels(1);

            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    // STOP RECORDING CODE
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        timer.stop();

        if(spinner.getSelectedItem().toString().equals("Shared")) {
            String filepath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(filepath+"/Shared");
            File files[]=file.listFiles();
            uploadAudio(files[files.length-1]);
            Log.i("Shared",spinner.getSelectedItem().toString());
        }

    }

//    to upload audio in firebase storage

    private void uploadAudio(File file) {

        mProgress.setMessage("Uploading Audio...");

        mProgress.show();


        SimpleDateFormat formatter=new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now =new Date();


        StorageReference filepath = mStorage.child("Audio").child("Recording..."+ formatter.format(now) +".3gp");

        Uri uri = Uri.fromFile(file);

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mProgress.dismiss();

                mRecordLable.setText("Uploading Finished");

            }
        });
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

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now =new Date();

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath+"/"+spinner.getSelectedItem().toString());
        if(!file.exists()){
            file.mkdir();
        }
        Log.i(TAG, "getRecordingFilePath:::: "+file.getAbsolutePath()+"/recording.."+formatter.format(now)+".3gp");
        return file.getAbsolutePath()+"/recording.."+formatter+".3gp";
    }
//    Adding animation to button
    public void aniamte(){
        mRecordBtn=findViewById(R.id.recordBtn);
        final Animation animation= AnimationUtils.loadAnimation(this,R.anim.bounce);
        mRecordBtn.startAnimation(animation);
    }

//    public void playrecent(View view) {
//        try {
//            mediaplayer = new MediaPlayer();
//            mediaplayer.setDataSource(getRecordingFilePath());
//            mediaplayer.prepare();
//            mediaplayer.start();
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
    public void linktolist(View view) {
        Intent intent = new Intent(this, list.class);
        startActivity(intent);
    }


    public void linkToCategory(View v) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, category.class);
//        startActivity(intent);
          DbQuery.loadCategory(new MyCompleteListener() {
              @Override
              public void onSuccess() {

                  Intent intent = new Intent(activity_recording.this, category.class);
                  startActivity(intent);
                  finish();

              }

              @Override
              public void onFailure() {

                  Toast.makeText(activity_recording.this, "Something went wrong please try again after some time", Toast.LENGTH_SHORT).show();

              }
          });

    }
// Methods for spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0){

        }
        if(adapterView.getId()==R.id.spinner){
            // Write code for local and shared storage
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    /*Code to be written for drop down
//    public View getDropDownView(int position, View convertView,
//                                ViewGroup parent) {
//        if (position == 0) {
//            // Set the hint text color gray
//
//        } else {
//
//        }
//        return ;
//    }

     */

//    public File getfile(String str){
//        File[] recording_files= music_dir().listFiles();
//        for(int i=0;i<recording_files.length;i++){
//            if(recording_files[i].getName().toString().endsWith(str)){
//                return recording_files[i];
//            }
//        }
//        return recording_files[0];
//    }
//    public File music_dir(){
//        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
//        File musicDirectory =contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//        return musicDirectory;
//    }
}

