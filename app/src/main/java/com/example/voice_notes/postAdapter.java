package com.example.voice_notes;

import android.content.Context;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class postAdapter extends BaseAdapter {

    List<post> mData;
    Context context;
    String Audio;

    public postAdapter(Context context,List<post> mData) {
        this.mData = mData;
        this.context= context;

    }


    @NonNull
    @Override
    public int getCount() {
        return mData.size();
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
            myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_items,parent,false);
        }
        else {
            myView=convertView;
        }

        TextView tvTitle = myView.findViewById(R.id.postTitle);
        TextView tvDescription = myView.findViewById(R.id.postDescription);
        TextView tvUserName = myView.findViewById(R.id.userName);


        tvTitle.setText((CharSequence) mData.get(position).getTitle());
        tvDescription.setText((CharSequence) mData.get(position).getDescription());
        tvUserName.setText((CharSequence) mData.get(position).getUserName());
        Audio = mData.get(position).getAudio();


        return myView;

    }

//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tvTitle;
//        TextView tvDescription;
//        TextView tvUserName;
//        ImageView audioPost;
//
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvTitle = itemView.findViewById(R.id.postTitle);
//            tvDescription = itemView.findViewById(R.id.postDescription);
//            tvUserName = itemView.findViewById(R.id.userName);
//            audioPost = itemView.findViewById(R.id.PostIcon);
//
//
//        }
//
//
//    }
}
