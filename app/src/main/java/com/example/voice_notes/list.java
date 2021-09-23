package com.example.voice_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class list extends AppCompatActivity {

    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ui);

        listview=findViewById(R.id.list);
        displayList();
    }

    private void displayList() {
        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
        File musicDirectory =contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);

        File[] recording_files = musicDirectory.listFiles() ;
        ArrayList<String> items=new ArrayList<>() ;

        for(File singlefile :recording_files){
            if(!singlefile.isDirectory() && !singlefile.isHidden() && singlefile.getName().toString().endsWith(".3gp")){
                items.add(singlefile.getName().toString().replace(".mp3",""));
            }
        }
        if(items.size()==0){
            Log.i("lulhogaya","shimtz");
        }

        ArrayAdapter<String> myadapter= new ArrayAdapter<>(this,R.layout.list_ui,items);
        listview.setAdapter(myadapter);
    }
}