package com.example.mojito;

import android.content.Context;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.example.mojito.RecyclerViewHolders.countryGalleryNumber;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;

    private List<Upload> countryUploadsList;

    private OnItemClickListener mListener;

    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
//        countryUploadsList = new List<Upload>() {
//            @Override
//            public int size() {
//                return 0;
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return false;
//            }
//
//            @Override
//            public boolean contains(@Nullable Object o) {
//                return false;
//            }
//
//            @NonNull
//            @Override
//            public Iterator<Upload> iterator() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Object[] toArray() {
//                return new Object[0];
//            }
//
//            @NonNull
//            @Override
//            public <T> T[] toArray(@NonNull T[] ts) {
//                return null;
//            }
//
//            @Override
//            public boolean add(Upload upload) {
//                return false;
//            }
//
//            @Override
//            public boolean remove(@Nullable Object o) {
//                return false;
//            }
//
//            @Override
//            public boolean containsAll(@NonNull Collection<?> collection) {
//                return false;
//            }
//
//            @Override
//            public boolean addAll(@NonNull Collection<? extends Upload> collection) {
//                return false;
//            }
//
//            @Override
//            public boolean addAll(int i, Collection<? extends Upload> collection) {
//                return false;
//            }
//
//            @Override
//            public boolean removeAll(@NonNull Collection<?> collection) {
//                return false;
//            }
//
//            @Override
//            public boolean retainAll(@NonNull Collection<?> collection) {
//                return false;
//            }
//
//            @Override
//            public void clear() {
//
//            }
//
//            @Override
//            public Upload get(int i) {
//                return null;
//            }
//
//            @Override
//            public Upload set(int i, Upload upload) {
//                return null;
//            }
//
//            @Override
//            public void add(int i, Upload upload) {
//
//            }
//
//            @Override
//            public Upload remove(int i) {
//                return null;
//            }
//
//            @Override
//            public int indexOf(Object o) {
//                return 0;
//            }
//
//            @Override
//            public int lastIndexOf(Object o) {
//                return 0;
//            }
//
//            @Override
//            public ListIterator<Upload> listIterator() {
//                return null;
//            }
//
//            @Override
//            public ListIterator<Upload> listIterator(int i) {
//                return null;
//            }
//
//            @Override
//            public List<Upload> subList(int i, int i1) {
//                return null;
//            }
//        };

//        for(int i=0;i<mUploads.size();i++){
//
//            if(mUploads.get(i).getcountryName()==countryGalleryNumber){
//                System.out.println("countryGalleryNumber2222222222222222 .................    " +mUploads.get(i).getcountryName());
//                countryUploadsList.add(mUploads.get(i));
//            }
//        }

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
        Glide.with(mContext)
                .load(R.drawable.lime2)
                //.placeholder(R.drawable.lime2)
//                .fit()
                //.centerCrop()
                .into(holder.lime_btn);



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

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        }


        @Override
        public void onClick(View v){
            if(mListener != null){
                int position = getAdapterPosition();
                if(position !=RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                    Toast.makeText(mContext,"This is aboutclicklistener", Toast.LENGTH_LONG).show();
//                    Glide.with(mContext).load(R.drawable.lime2).into(holder.lime_btn);

                }

            }
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int position);
//
//        void onWhatEverClick(int position);
//        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

}