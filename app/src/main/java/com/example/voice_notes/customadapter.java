package com.example.voice_notes;

import android.content.Context;
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

import java.util.ArrayList;

public class customadapter extends ArrayAdapter<String> {
    private ArrayList<String> items=new ArrayList<>();
    private Context mcontext;
    public customadapter(@NonNull Context context, int resource, ArrayList<String> items) {
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
        TextView textView3= convertView.findViewById(R.id.textView2);
        ImageButton imageButton=convertView.findViewById(R.id.menuMore);
        textView2.setText(items.get(position));
        textView2.setText(items.get(position));
        textView2.setSelected(true);
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
                                Toast.makeText(mcontext, "item1 clicked", Toast.LENGTH_SHORT).show();
                            case R.id.item2:
                                Toast.makeText(mcontext, "item2 clicked", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
            }
        });
        return convertView;
    }
}
