package com.example.voice_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class listofcategory extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> items= new ArrayList<>();
    ArrayList<File> all_files= new ArrayList<>();
    MediaPlayer mediaplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofcategory);
        listView = findViewById(R.id.category_list);
        Intent intent=getIntent();

        Bundle bundle = getIntent().getExtras();

        File filepath = new File(Environment.getExternalStorageDirectory().getPath() + "/voicenotes/" + bundle.getString("category"));
        if(!filepath.exists()){
            filepath.mkdir();
        }
        getfiles(filepath);

        ArrayAdapter<String> myadapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(myadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    mediaplayer = new MediaPlayer();
                    mediaplayer.setDataSource(all_files.get(i).getPath());
                    mediaplayer.prepare();
                    mediaplayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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