package com.example.mojito;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
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

public class CustomGallery extends Activity {
    String topic;
    private static ArrayList<String> images;
    int flag = 0; //0= 현재 gridview , 1= 현재 wideview
    int heartFlag = 0;//누르기전0, 누르면1


    //modified22
    private static LayoutInflater inflater = null;
    String[] result;
    Context context;
    public static String[] osNameList;
    public static int osImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customgridframe);

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

        osNameList = getAllShownImagesPath(getBaseContext()).toArray(new String[0]);
        osImages=R.drawable.lime1;
        gridview.setAdapter(new ImageAdapter(this, osNameList, osImages));
        //gridview.setAdapter(new ImageAdapter(this));
        gridview.setNumColumns(3); //처음 girdview모드
        final ImageView heart = (ImageView) findViewById(R.id.heart2);
       //heart.setImageResource(R.drawable.lime1);


        final ImageButton widebtn = (ImageButton) findViewById(R.id.widebtn);
        widebtn.setImageResource(R.drawable.wider);

        widebtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {//현재 gird에서 wideVer로!
                    Toast.makeText(getApplicationContext(), "Wide 버전으로 띄웁니다.", Toast.LENGTH_SHORT).show();
                    widebtn.setImageResource(R.drawable.menu);
                    gridview.setNumColumns(1);
                    gridview.setAdapter(new ImageAdapter2(CustomGallery.this, osNameList, osImages));
                    flag = 1;
                } else {//현재 gird에서 wideVer로!
                    Toast.makeText(getApplicationContext(), "grid 버전으로 띄웁니다.", Toast.LENGTH_SHORT).show();
                    widebtn.setImageResource(R.drawable.wider);
                    gridview.setNumColumns(3);
                    gridview.setAdapter(new ImageAdapter(CustomGallery.this, osNameList, osImages));
                    flag = 0;
                }

            }
        });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1,
                                    final int position, long arg3) {
//                positionGlobal = position;
//                if (null != images && !images.isEmpty()) {
//                    if (heartFlag == 0) {
//                        Toast.makeText(CustomGallery.this, "'좋아요'", Toast.LENGTH_SHORT).show();
//                        heart.setImageResource(R.drawable.lime2);
//                        heartFlag = 1;
//                    } else {
//                        Toast.makeText(CustomGallery.this, "'좋아요 취소'", Toast.LENGTH_SHORT).show();
//                        heart.setImageResource(R.drawable.lime1);
//                        heartFlag = 0;
//                    }
//                }
            }
        });
    }


    /**
     * The Class ImageAdapter.
     */
//    public static class ImageAdapter extends BaseAdapter {
//
//        /** The context. */
//        public static Context context;
//
//        /**
//         * Instantiates a new image adapter.
//         *
//         * @param localContext
//         *            the local context
//         */
//        public ImageAdapter(Context localContext) {
//            context = localContext;
//            images = getAllShownImagesPath(context);
//        }
//
//        public ImageAdapter(Context localContext,int i) {
//            context = localContext;
//            images = getAllShownImagesPath(context);
//        }
//
//
//        public int getCount() {
//            return images.size();
//        }
//
//
//        public String getImage (int position) {
//            return images.get(position);
//        }
//
//        public Object getItem(int position) {
//            return position;
//        }
//
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public View getView(final int position, View convertView,
//                            ViewGroup parent) {
//            ImageView picturesView;
//            if (convertView == null) {
//                picturesView = new ImageView(context);
//                picturesView.setScaleType(ImageView.ScaleType.CENTER);
//                picturesView
//                        .setLayoutParams(new GridView.LayoutParams(359, 359));
//
//            } else {
//                picturesView = (ImageView) convertView;
//            }
//
//            Glide.with(context).load(images.get(position))
//                    //.placeholder(R.drawable.ic_launcher).centerCrop()
//                    .into(picturesView);
//
//            return picturesView;
//        }
//
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
            //osNameList= listOfAllImages.toArray(new String[0]);
            return listOfAllImages;
        }
//    }


    //modified
    public class ImageAdapter extends BaseAdapter {

        String[] result;
        Context context;
        int imageId;

        //        private static LayoutInflater inflater=null;
        public ImageAdapter(CustomGallery mainActivity, String[] osNameList, int osImages) {
            // TODO Auto-generated constructor stub
            result = osNameList;
            context = mainActivity;
            imageId = osImages;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return result.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder {
            ImageButton os_btn;
            ImageView os_img;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.customgrid, null);
            holder.os_btn = (ImageButton) rowView.findViewById(R.id.heart2);
            holder.os_img = (ImageView) rowView.findViewById(R.id.photo);

            //holder.os_btn.setImageResource(imageId);
           // holder.os_img.setImageBitmap(BitmapFactory.decodeFile(osNameList[position]));
            Glide.with(context).load(osNameList[position]).into(holder.os_img);
            Glide.with(context).load(R.drawable.lime1).into( holder.os_btn);


            holder.os_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (heartFlag == 0) {
                        Toast.makeText(CustomGallery.this, "'좋아요'", Toast.LENGTH_SHORT).show();
                        Log.e("heart number is ...","now :"+heartFlag);
                        Glide.with(context).load(R.drawable.lime2).into( holder.os_btn);
                        //Glide.with(context).load(R.drawable.lime2).into( holder.os_btn);
                        heartFlag = 1;
                    } else{
                        Log.e("heart number is ...","now111 :"+heartFlag);
                        Toast.makeText(CustomGallery.this, "'좋아요 취소'", Toast.LENGTH_SHORT).show();
                        Glide.with(context).load(R.drawable.lime1).into( holder.os_btn);
                        heartFlag = 0;
                    }
                }
            });

            return rowView;
        }


        //modified


        /**
         * The Class ImageAdapter2.
         */
//        public static class ImageAdapter2 extends BaseAdapter {
//
//            /**
//             * The context.
//             */
//            public static Context context;
//
//            /**
//             * Instantiates a new image adapter.
//             *
//             * @param localContext the local context
//             */
//            public ImageAdapter2(Context localContext) {
//                context = localContext;
//                images = getAllShownImagesPath(context);
//            }
//
//            public ImageAdapter2(Context localContext, int i) {
//                context = localContext;
//                images = getAllShownImagesPath(context);
//            }
//
//
//            public int getCount() {
//                return images.size();
//            }
//
//
//            public String getImage(int position) {
//                return images.get(position);
//            }
//
//            public Object getItem(int position) {
//                return position;
//            }
//
//            public long getItemId(int position) {
//                return position;
//            }
//
//            public View getView(final int position, View convertView,
//                                ViewGroup parent) {
//                ImageView picturesView;
//                if (convertView == null) {
//                    picturesView = new ImageView(context);
//                    picturesView.setScaleType(ImageView.ScaleType.CENTER);
//                    picturesView
//                            .setLayoutParams(new GridView.LayoutParams(1100, 1100));
//
//                } else {
//                    picturesView = (ImageView) convertView;
//                }
//
//                Glide.with(context).load(images.get(position))
//                        //.placeholder(R.drawable.ic_launcher).centerCrop()
//                        .into(picturesView);
//
//                return picturesView;
//            }
//
//            /**
//             * Getting All Images Path.
//             *
//             * @param activity the activity
//             * @return ArrayList with images Path
//             */
//            private ArrayList<String> getAllShownImagesPath(Context activity) { //imagepath를 불러오기!
//                Uri uri;
//                Cursor cursor;
//                int data, album;
//                int check = 0;
//
//
//                int column_index_data, column_index_folder_name;
//                ArrayList<String> listOfAllImages = new ArrayList<String>();
//                String absolutePathOfImage = null;
//                uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//                String[] projection = {MediaStore.MediaColumns.DATA,
//                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
//
//                cursor = activity.getContentResolver().query(uri, projection, null, null, null);
//
//                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                column_index_folder_name = cursor
//                        .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//                while (cursor.moveToNext()) {
//
//                    absolutePathOfImage = cursor.getString(column_index_data);
//                    listOfAllImages.add(absolutePathOfImage);
//
//                }
//                return listOfAllImages;
//            }
//        }
    }


    public class ImageAdapter2 extends BaseAdapter {

        String[] result;
        Context context;
        int imageId;

        //        private static LayoutInflater inflater=null;
        public ImageAdapter2(CustomGallery mainActivity, String[] osNameList, int osImages) {
            // TODO Auto-generated constructor stub
            result = osNameList;
            context = mainActivity;
            imageId = osImages;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return result.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder {
            ImageButton os_btn;
            ImageView os_img;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.customgrid2, null);
            holder.os_btn = (ImageButton) rowView.findViewById(R.id.heart2);
            holder.os_img = (ImageView) rowView.findViewById(R.id.photo);

//            holder.os_btn.setImageResource(R.drawable.lime1);
//            holder.os_img.setImageBitmap(BitmapFactory.decodeFile(osNameList[position]));
            Glide.with(context).load(osNameList[position]).into(holder.os_img);
           // Glide.with(context).load(R.drawable.lime1).into( holder.os_btn);
            if (heartFlag == 0) {
                Glide.with(context).load(R.drawable.lime1).into( holder.os_btn);
            } else{
                Glide.with(context).load(R.drawable.lime2).into( holder.os_btn);
            }






            holder.os_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_SHORT).show();
//                    holder.os_btn.setImageResource(R.drawable.lime2);

                    if (heartFlag == 0) {
                        Toast.makeText(CustomGallery.this, "'좋아요'", Toast.LENGTH_SHORT).show();
                        Log.e("heart number is ...","now :"+heartFlag);
                        Glide.with(context).load(R.drawable.lime2).into( holder.os_btn);
                        //Glide.with(context).load(R.drawable.lime2).into( holder.os_btn);
                        heartFlag = 1;
                    } else{
                        Log.e("heart number is ...","now111 :"+heartFlag);
                        Toast.makeText(CustomGallery.this, "'좋아요 취소'", Toast.LENGTH_SHORT).show();
                        Glide.with(context).load(R.drawable.lime1).into( holder.os_btn);
                        heartFlag = 0;
                    }



                }
            });

            return rowView;
        }


        //modified


        /**
         * The Class ImageAdapter2.
         */
//        public static class ImageAdapter2 extends BaseAdapter {
//
//            /**
//             * The context.
//             */
//            public static Context context;
//
//            /**
//             * Instantiates a new image adapter.
//             *
//             * @param localContext the local context
//             */
//            public ImageAdapter2(Context localContext) {
//                context = localContext;
//                images = getAllShownImagesPath(context);
//            }
//
//            public ImageAdapter2(Context localContext, int i) {
//                context = localContext;
//                images = getAllShownImagesPath(context);
//            }
//
//
//            public int getCount() {
//                return images.size();
//            }
//
//
//            public String getImage(int position) {
//                return images.get(position);
//            }
//
//            public Object getItem(int position) {
//                return position;
//            }
//
//            public long getItemId(int position) {
//                return position;
//            }
//
//            public View getView(final int position, View convertView,
//                                ViewGroup parent) {
//                ImageView picturesView;
//                if (convertView == null) {
//                    picturesView = new ImageView(context);
//                    picturesView.setScaleType(ImageView.ScaleType.CENTER);
//                    picturesView
//                            .setLayoutParams(new GridView.LayoutParams(1100, 1100));
//
//                } else {
//                    picturesView = (ImageView) convertView;
//                }
//
//                Glide.with(context).load(images.get(position))
//                        //.placeholder(R.drawable.ic_launcher).centerCrop()
//                        .into(picturesView);
//
//                return picturesView;
//            }
//
//            /**
//             * Getting All Images Path.
//             *
//             * @param activity the activity
//             * @return ArrayList with images Path
//             */
//            private ArrayList<String> getAllShownImagesPath(Context activity) { //imagepath를 불러오기!
//                Uri uri;
//                Cursor cursor;
//                int data, album;
//                int check = 0;
//
//
//                int column_index_data, column_index_folder_name;
//                ArrayList<String> listOfAllImages = new ArrayList<String>();
//                String absolutePathOfImage = null;
//                uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//                String[] projection = {MediaStore.MediaColumns.DATA,
//                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
//
//                cursor = activity.getContentResolver().query(uri, projection, null, null, null);
//
//                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                column_index_folder_name = cursor
//                        .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//                while (cursor.moveToNext()) {
//
//                    absolutePathOfImage = cursor.getString(column_index_data);
//                    listOfAllImages.add(absolutePathOfImage);
//
//                }
//                return listOfAllImages;
//            }
//        }
    }
}

