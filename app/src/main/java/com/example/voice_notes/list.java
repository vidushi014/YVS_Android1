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
    private File filepath = new File(Environment.getExternalStorageDirectory().getPath()+"/voicenotes");
    ArrayList<String> items = new ArrayList<String>();
    ArrayList<File> allfiles = new ArrayList<File>();
    private static final String TAG = "list";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ui);

        testing(filepath);

        listview=findViewById(R.id.list);
        ArrayAdapter<String> myadapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items);
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
//                        items.remove(deletePosition-1);
//                        myadapter.notifyDataSetChanged();
//                        myadapter.notifyDataSetInvalidated();
                        myadapter.remove(myadapter.getItem(position-1));
                        allfiles.get(position).delete();
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
    public void testing(File file){

        File files[] = file.listFiles();

        for(int i=0;i<files.length;i++){

            if(files[i].isDirectory()){
                testing(files[i]);
            }
            else {
                items.add(files[i].getName());
                allfiles.add(files[i]);
            }
        }
    }
}