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
    public static String printName; // 들어온 갤러리 구분하는 string

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        countryName = (TextView)itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView)itemView.findViewById(R.id.country_photo);
    }

    @Override
    public void onClick(View view) {

        countryGalleryNumber=getPosition(); //들어온 갤러리 번호 구분해주기
        String printName;

        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show(); //클릭한 사진 위치 알려줌

        switch (countryGalleryNumber) {
            case 0:
                printName = "KOREA";
                break;
            case 1:
                printName = "INDIA / SE ASIA";
                break;
            case 2:
                printName = "AMERICAS";
                break;
            case 3:
                printName = "JAPAN";
                break;
            case 4:
                printName = "AFRICA / MIDDLE EAST";
                break;
            case 5:
                printName = "EUROPE";
                break;
            case 6:
                printName = "CHINA / TAIWAN";
                break;
            case 7:
                printName = "OCEANIA / GUAM";
                break;
            case 8:
                printName = "RUSSIA / CENTRAL ASIA";
                break;
            default:
                printName = "RUSSIA / CENTRAL ASIA";
                break;
        } // dirname으로 어떤 나라의 갤러리 카테고리로 들어왔는지 구분

        Toast.makeText(view.getContext(), printName+" 앨범으로 이동합니다.", Toast.LENGTH_SHORT).show();

        Intent intent2 = new Intent(view.getContext(), ImagesActivity.class);
        intent2.putExtra("namedir",getPosition());
        view.getContext().startActivity(intent2);

    }
}