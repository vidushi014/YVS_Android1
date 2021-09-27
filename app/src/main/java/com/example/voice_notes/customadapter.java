package com.example.voice_notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class customadapter extends ArrayAdapter<String> {
    private ArrayList<String> items=new ArrayList<>();

    public customadapter(@NonNull Context context, int resource, ArrayList<String> items) {
        super(context, resource, items);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        TextView textView2= convertView.findViewById(R.id.textView2);
        TextView textView3= convertView.findViewById(R.id.textView2);
        textView2.setText(items.get(position));
        textView2.setText(items.get(position));
        textView2.setSelected(true);
        return convertView;
    }
}
