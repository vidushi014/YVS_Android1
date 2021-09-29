package com.example.voice_notes;

import static android.content.ContentValues.TAG;

//import static androidx.core.content.ContextCompat.startActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.load.model.Model;

import java.io.File;
import java.util.ArrayList;

public class customadapter extends ArrayAdapter<File> {
    private ArrayList<File> items=new ArrayList<>();
    private Context mcontext;
    private TimeAgo timeAgo;
    Model temp;
    public customadapter(@NonNull Context context, int resource, ArrayList<File> items) {
        super(context, resource, items);
        this.items = items;
        this.mcontext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        TextView textView2= convertView.findViewById(R.id.textView2);
        TextView textView3= convertView.findViewById(R.id.textView3);
        ImageButton imageButton=convertView.findViewById(R.id.menuMore);
        textView2.setText(items.get(position).getName());
//        textView2.setText(items.get(position));
        textView2.setSelected(true);
        timeAgo=new TimeAgo();
//        String a= items.get(position);

        textView3.setText(timeAgo.getTimeAgo(items.get(position).lastModified()));
        View finalConvertView = convertView;
        View finalConvertView1 = convertView;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(mcontext,view);
                popupMenu.getMenuInflater().inflate(R.menu.item_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.item1:
                                Intent intent = new Intent(mcontext,Community.class);
                                intent.putExtra("FilePath",items.get(position));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mcontext.startActivity(intent);
                                Toast.makeText(mcontext, "item1 clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.item2:
                                Toast.makeText(mcontext, "item2 clicked", Toast.LENGTH_SHORT).show();

                                break;
                            case R.id.share:
                                Toast.makeText(mcontext, "share ", Toast.LENGTH_SHORT).show();
                                String filepath= Environment.getExternalStorageDirectory().getPath()+"/voicenotes/Local/recording..2021_09_29_12_30_38.mp3";
//                                String sharePath = items.get(position).getPath();
                                Log.i(TAG, "path: "+ String.valueOf(items.get(position)));
                                Uri uri = Uri.parse(filepath);
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("audio/mp3");
                                share.putExtra(Intent.EXTRA_STREAM, uri);
//                                share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                Log.i(TAG, "onMenuItemClick: "+uri.toString());
//                               startActivity(Intent.createChooser(share, "Share Sound File"));
                                mcontext.startActivity(Intent.createChooser(share, "Share Sound File"));
                        }
                        return true;
                    }
                });
            }
        });
        return convertView;
    }

}
