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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.mojito.R.drawable.ic_launcher_background;

public class DetailGallery extends Activity {
        String topic;
        private static ArrayList<String> images;
        int flag=0; //0= 현재 gridview , 1= 현재 wideview

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
        } // dirname으로 어떤 나라의 갤러리 카테고리로 들어왔는지 구분
        TextView topicText = (TextView) findViewById(R.id.topictext);


        topicText.setText(topic); // 들어온 국가파일이름



        final GridView gridview = (GridView) findViewById(R.id.detailGridView);

        gridview.setAdapter(new ImageAdapter(this));

        gridview.setNumColumns(3);


        final ImageButton widebtn = (ImageButton)findViewById(R.id.widebtn);
        widebtn.setImageResource(R.drawable.wider);

        widebtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                if (flag==0){//현재 gird에서 wideVer로!
                    Toast.makeText(getApplicationContext(),"Wide 버전으로 띄웁니다.",Toast.LENGTH_SHORT).show();
                    widebtn.setImageResource(R.drawable.menu);
                    gridview.setNumColumns(1);
                    gridview.setAdapter(new ImageAdapter2(getApplicationContext()));
                    flag=1;
                }
                else{//현재 gird에서 wideVer로!
                    Toast.makeText(getApplicationContext(),"grid 버전으로 띄웁니다.",Toast.LENGTH_SHORT).show();
                    widebtn.setImageResource(R.drawable.wider);
                    gridview.setNumColumns(3);
                    gridview.setAdapter(new ImageAdapter(getApplicationContext()));
                    flag=0;
                }

            }
        });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1,
                                    final int position, long arg3) {
//                positionGlobal = position;
                if (null != images && !images.isEmpty()) {

                    Toast.makeText(DetailGallery.this, "ooookkaayy", Toast.LENGTH_SHORT).show();
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
                picturesView.setScaleType(ImageView.ScaleType.CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(359, 359));

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

        /**
         * The Class ImageAdapter2.
         */
        public static class ImageAdapter2 extends BaseAdapter {

            /** The context. */
            public static Context context;

            /**
             * Instantiates a new image adapter.
             *
             * @param localContext
             *            the local context
             */
            public ImageAdapter2(Context localContext) {
                context = localContext;
                images = getAllShownImagesPath(context);
            }

            public ImageAdapter2(Context localContext,int i) {
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
                    picturesView.setScaleType(ImageView.ScaleType.CENTER);
                    picturesView
                            .setLayoutParams(new GridView.LayoutParams(1100, 1100));

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
