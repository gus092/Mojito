package com.example.mojito;

import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public class Tab1 extends Fragment {
    public View mview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.tab1, container, false);

        mview = (TextView)getActivity().findViewById(R.id.textView);

        String filename = Environment.getExternalStorageDirectory()
                .getPath() + "/20140705_162816.jpg";
        try {
            ExifInterface exif = new ExifInterface(filename);
            showExif(exif);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error!", Toast.LENGTH_LONG).show();
        }

        return view;
    }


    private void showExif(ExifInterface exif) {

        String myAttribute = "[Exif information] \n\n";

        myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif);
        myAttribute += getTagString(ExifInterface.TAG_FLASH, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE,
                exif);
        myAttribute += getTagString(
                ExifInterface.TAG_GPS_LATITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE,
                exif);
        myAttribute += getTagString(
                ExifInterface.TAG_GPS_LONGITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH,
                exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH,
                exif);
        myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
        myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);
        myAttribute += getTagString(ExifInterface.TAG_ORIENTATION,
                exif);
        myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE,
                exif);


        Log.e("Image Information : ", myAttribute);
    }

    private String getTagString(String tag, ExifInterface exif) {
        return (tag + " : " + exif.getAttribute(tag) + "\n");
    }


}


