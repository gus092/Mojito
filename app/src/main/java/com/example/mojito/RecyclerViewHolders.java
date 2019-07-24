package com.example.mojito;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView countryName;
    public ImageView countryPhoto;
    public static int countryGalleryNumber; //들어온 갤러리 구분하는 번호

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        countryName = (TextView)itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView)itemView.findViewById(R.id.country_photo);
    }

    @Override
    public void onClick(View view) {

        countryGalleryNumber=getPosition(); //들어온 갤러리 번호 구분해주기

        Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(view.getContext(), ImagesActivity.class);
        intent2.putExtra("namedir",getPosition());
        view.getContext().startActivity(intent2);

    }
}