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
        ArrayAdapter<String> myadapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,displayList());
        listview.setAdapter(myadapter);
    }

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

    private ArrayList<String> displayList() {
        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
        File musicDirectory =contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);

        File[] recording_files = musicDirectory.listFiles() ;
        ArrayList<String> items=new ArrayList<>() ;
        for(int i=0;i<recording_files.length;i++){
            items.add(recording_files[i].getName().toString());
            Log.i("lololol",items.get(i));
        }
        return items;
    }
}