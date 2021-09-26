package com.example.voice_notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CategoryAdaptor extends BaseAdapter {

    private List<CategoryModel> cat_list;

    public CategoryAdaptor(List<CategoryModel> cat_list) {
        this.cat_list = cat_list;
    }

    @Override
    public int getCount() {
        return cat_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View myView;

        if (convertView==null){
            myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_items_layout,parent,false);
        }
        else {
            myView=convertView;
        }

        TextView catName = myView.findViewById(R.id.CatName);
        TextView noOfTests = myView.findViewById(R.id.noOfTest);
//        ImageView catIcon = myView.findViewById(R.id.CatIcon);

//        catIcon.setImageResource(cat_list.get(position).getCatIcons());
        catName.setText(cat_list.get(position).getName());
        noOfTests.setText(String.valueOf(cat_list.get(position).getNoOfTests()) + "recordings");



        return myView;

    }


}
