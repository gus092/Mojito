package com.example.mojito;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

    public class DetailGallery extends Activity {
        String topic;
        private static ArrayList<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailgallery);

        Intent i = getIntent();
        int dirname = i.getIntExtra("namedir", -1); // dirname에 나라 position 분류 들어있음!
        switch (dirname) {
            case 0:
                topic = "KOREA";
                break;
            case 1:
                topic = "EUROPE";
                break;
            case 2:
                topic = "USA / CANADA";
                break;
            case 3:
                topic = "JAPAN";
                break;
            case 4:
                topic = "AFRICA";
                break;
            case 5:
                topic = "ASIA / INDIA";
                break;
            case 6:
                topic = "CHINA / TAIWAN";
                break;
            case 7:
                topic = "OTHERS";
                break;
            default:
                topic = "OTHERS";
                break;
        }
        TextView topicText = (TextView) findViewById(R.id.topictext);
        topicText.setText(topic);


        GridView gridview = (GridView) findViewById(R.id.detailGridView);

        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1,
                                    final int position, long arg3) {
//                positionGlobal = position;
                if (null != images && !images.isEmpty()) {


                    //images를 선택했을때 동작

                }
            }
        });
    }




    /**
     * The Class ImageAdapter.
     */
    public static class ImageAdapter extends BaseAdapter {

        /** The context. */
        public static Context context;

//       public static List photoRemove = new ArrayList();

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext
         *            the local context
         */
        public ImageAdapter(Context localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }

        public ImageAdapter(Context localContext,int i) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }


        public int getCount() {
            return images.size();
        }


        public String getImage (int position) {
            return images.get(position);
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(700, 700));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(images.get(position))
                    //.placeholder(R.drawable.ic_launcher).centerCrop()
                    .into(picturesView);

            return picturesView;
        }

        /**
         * Getting All Images Path.
         *
         * @param activity
         *            the activity
         * @return ArrayList with images Path
         */
        private ArrayList<String> getAllShownImagesPath(Context activity) { //imagepath를 불러오기!
            Uri uri;
            Cursor cursor;
            int data,album;
            int check=0;


            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null, null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {

                absolutePathOfImage = cursor.getString(column_index_data);
                listOfAllImages.add(absolutePathOfImage);

            }
            return listOfAllImages;
        }
    }
}
