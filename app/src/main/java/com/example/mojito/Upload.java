package com.example.mojito;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;


public class Upload {
    private String mName;
    private String mImageUrl;
    private int mcountryName; //찍은 국가 분류해주는 int
    private String mwriter;  //글쓴이
    private ArrayList<String> mlikedUserList; //좋아요를 누른 사람의 목록

    public String mKey;


    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl,int countryName,String writer,ArrayList<String> likedUserList) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
        mcountryName = countryName;
        mwriter = writer;
        mlikedUserList = likedUserList;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() { return mImageUrl; }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public int getcountryName() {
        return mcountryName;
    }

    public void setcountryName(int countryname) {
        mcountryName = countryname;
    }

//글쓴이
    public String getmwriter() {
        return mwriter;
    }

    public void setmwriter(String writer) { mwriter = writer; }
//likedUser

    public ArrayList<String> getmlikedUserList() {

        for (int k=0; k<mlikedUserList.size();k++)
        Log.e("REturn..",",,,,"+mlikedUserList.get(k));

        return mlikedUserList;
    }

    public void setmlikedUserList(ArrayList<String> likedUserList) { mlikedUserList = likedUserList; }

    @Exclude
    public String getKey(){
        return mKey;
    }
    @Exclude
    public void setKey(String key){
        mKey =key;
    }
}