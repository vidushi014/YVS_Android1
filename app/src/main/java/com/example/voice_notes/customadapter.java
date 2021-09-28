package com.example.voice_notes;

import android.content.Context;
import android.content.Intent;
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

import java.io.File;
import java.util.ArrayList;

public class customadapter extends ArrayAdapter<File> {
    private ArrayList<File> items=new ArrayList<>();
    private Context mcontext;
    private TimeAgo timeAgo;
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
                        }
                        return true;
                    }
                });
            }
        });
        return convertView;
    }
}
