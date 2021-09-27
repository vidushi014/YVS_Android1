package com.example.voice_notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class list extends AppCompatActivity  {

    private ListView listview;
    private MediaPlayer mediaplayer;
    ArrayList<String> items= new ArrayList<>();
    ArrayList<File> allfiles= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ui);
        File filepath= new File(Environment.getExternalStorageDirectory().getPath()+"/voicenotes");
        listview=findViewById(R.id.list);

        traverser(filepath);
//        ArrayAdapter<String> myadapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        customadapter myadapter = new customadapter(getApplicationContext(),0,items);
        listview.setAdapter(myadapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        mediaplayer = new MediaPlayer();
                        mediaplayer.setDataSource(allfiles.get(i).getPath());
                        mediaplayer.prepare();
                        mediaplayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){


            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                removeItemFromList(i);
                items.remove(i);
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
}