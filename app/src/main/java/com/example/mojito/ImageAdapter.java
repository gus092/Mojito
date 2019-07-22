package com.example.mojito;

import android.content.Context;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;



public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    StorageReference fileReference2;

    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
        for(int i=0;i<mUploads.size();i++){
            System.out.println("++++++++++++++++++++++++++++++++++"+ mUploads.get(i));
        }
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);

        ImageButton limeBtn = (ImageButton)v.findViewById(R.id.lime_btn);
        limeBtn.setImageResource(R.drawable.lime2);
        System.out.println("CLLLLLLLLICKKKKKKKKKKKKKKKKKKK");
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());


       // Log.e("현재 이미지는..","Now.."+uploadCurrent.getImageUrl());

        Glide.with(mContext)
                .load(uploadCurrent.getImageUrl())
                //.placeholder(R.drawable.lime2)
//                .fit()
                .centerCrop()
                .into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);

        }
    }
}

//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//
//import java.util.List;
//
//public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
//    private Context mContext;
//    private List<Upload> mUploads;
//
//    public ImageAdapter(Context context, List<Upload> uploads){
//        mContext = context;
//        mUploads = uploads;
//    }
//
//    @NonNull
//    @Override
//    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//       View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
//       return new ImageViewHolder(v);
//
//    }
//
//    @Override
//    public void onBindViewHolder(ImageViewHolder holder, int position) {
//        Upload uploadCurrent = mUploads.get(position);
//        holder.textViewName.setText(uploadCurrent.getName());
//        Glide.with(mContext).load(uploadCurrent.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imageView); //fit?? //이미지 띄우는 부분?
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mUploads.size();
//    }
//
//    public class ImageViewHolder extends RecyclerView.ViewHolder{
//        public TextView textViewName;
//        public ImageView imageView;
//
//        public ImageViewHolder(View itemView){
//            super(itemView);
//
//            textViewName =itemView.findViewById(R.id.edit_text_file_name);
//            imageView = itemView.findViewById(R.id.image_view_upload);
//        }
//    }
//
//
//}
