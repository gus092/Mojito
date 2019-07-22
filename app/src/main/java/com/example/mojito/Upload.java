package com.example.mojito;

import android.util.Log;

public class Upload {
    private String mName;
    private String mImageUrl;
    private int mcountryName;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl,int countryName) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        mcountryName = countryName;
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
}