package com.example.mojito;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import static com.example.mojito.Authentication.LoginActivity.userName;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;

    private List<Upload> countryUploadsList;

    private OnItemClickListener mListener;

    private OnItemLongClickListener mLongListener;

    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;


    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Log.e("textViewName","is.."+uploadCurrent.getName());

       // holder.lime_btn.setImageResource(R.drawable.lime2);
        for (int i=0; i<mUploads.get(position).getmlikedUserList().size();i++){
            if(mUploads.get(position).getmlikedUserList().get(i).equals(userName)){//좋아요를 누른 상태


                Glide.with(mContext)
                        .load(R.drawable.like)
//                .fit()
                        //.priority(Priority.HIGH)
                        .centerCrop()
                        .into(holder.lime_btn);


            }else{ //좋아요 안누른 상태
                Glide.with(mContext)
                        .load(R.drawable.unlike)
                        //.placeholder(R.drawable.lime2)
//                .fit()
                        .centerCrop()
                        .into(holder.lime_btn);

            }
        }





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

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        public TextView textViewName;
        public ImageView imageView;
        public ImageButton lime_btn;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            lime_btn = itemView.findViewById(R.id.lime_btn);

            //added
            lime_btn.setOnClickListener(this);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v){
            if(mListener != null){
                int position = getAdapterPosition();
                if(position !=RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                   // Toast.makeText(mContext,"This is aboutclicklistener", Toast.LENGTH_LONG).show();
//                    Glide.with(mContext).load(R.drawable.lime2).into(holder.lime_btn);
                }
            }
        }

        @Override
        public boolean onLongClick(View v){
            if(mLongListener != null){
                int position = getAdapterPosition();
                if(position !=RecyclerView.NO_POSITION){
                    mLongListener.onDeleteClick(position);


                }
           }
            return true;
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemLongClickListener{
        void onDeleteClick(int position);
        //
        //void onDeleteClick(int position);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        mLongListener = listener;
    }
}