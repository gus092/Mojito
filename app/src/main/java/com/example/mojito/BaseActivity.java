package com.example.mojito;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends Activity {

    private GridLayoutManager lLayout;
    private static ArrayList<String> images2;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twofragment);
        List<Item> rowListItem = getAllItemList();
        Intent i = getIntent();
        String welcomupload = i.getStringExtra("main");

        lLayout = new GridLayoutManager(this, 1);//category spancount

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);


        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(this, rowListItem);
        rView.setAdapter(rcAdapter);

    }



//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        //getMenuInflater().inflate(R.menu.menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//        // return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        if(id == R.id.action_refresh){
//            Toast.makeText(requireContext(), "Refresh App", Toast.LENGTH_LONG).show();
//        }
//        if(id == R.id.action_new){
//            Toast.makeText(requireContext(), "Create Text", Toast.LENGTH_LONG).show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private List<Item> getAllItemList(){
//        String [][] countryName =
//                { {"대한민국"}
//                        ,{"중국", "대만"}
//                        ,{"일본"}
//                        ,{"라오스", "베트남", "말레이시아", "태국", "인도네시아", "필리핀", "싱가포르", "캄보디아", "동티모르", "브루나이","몰디브","인도"}
//                        ,{"미국","캐나다"}
//                        , {"독일", "프랑스", "이탈리아", "영국", "스페인", "그리스", "스위스", "네덜란드", "폴란드", "크로아디아", "오스트리아", "스웨덴", "우크라이나", "벨기에", "헝가리", "체코"}
//                        ,{"이집트","수단","리비아","알제리","튀니지","모로코","이란","요르단"}
//                        ,{"러시아","괌","몽골","호주"}  };

        List<Item> allItems = new ArrayList<Item>();

//        allItems.add(new Item("United States", R.drawable.one));
//        allItems.add(new Item("Canada", R.drawable.two));
//        allItems.add(new Item("United Kingdom", R.drawable.three));
//        allItems.add(new Item("Germany", R.drawable.four));
//        allItems.add(new Item("Sweden", R.drawable.five));
//        allItems.add(new Item("United Kingdom", R.drawable.six));
//        allItems.add(new Item("Germany", R.drawable.seven));
//        allItems.add(new Item("Sweden", R.drawable.eight));
        //modified
      //images에는 원하는 사진의 절대경로를 넣으면 됨 //images2.size()로 바꾸기!
            allItems.add(new Item("# KOREA", R.drawable.korea));
            allItems.add(new Item("# CHINA\n/TAIWAN", R.drawable.taiwan));
            allItems.add(new Item("# JAPAN", R.drawable.japan));
            allItems.add(new Item("# ASIA\n/INDIA", R.drawable.seaisa));
            allItems.add(new Item("# USA\n/CANADA", R.drawable.usa));
            allItems.add(new Item("# EUROPE", R.drawable.london));
            allItems.add(new Item("# AFRICA", R.drawable.africa));
            allItems.add(new Item("# OTHERS", R.drawable.others));

        //modified

        return allItems;
    }
//    private ArrayList<String> getAllShownImagesPath(Context activity) {
//        // grouping 된 첫사진만
//        Uri uri;
//        Cursor cursor;
//        int data,album;
//        int check=0;
//
//
//
//        int column_index_data, column_index_folder_name;
//        ArrayList<String> listOfAllImages = new ArrayList<String>();
//        String absolutePathOfImage = null;
//        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        // 수정할부분
//        String[] projection = { MediaStore.MediaColumns.DATA,
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
//
//        cursor = activity.getContentResolver().query(
//                MediaStore.Files.getContentUri("external"),
//                null,
//                MediaStore.Images.Media.DATA + " like ? ",
//                new String[] {"%Screenshots%"},
//                null
//        );
//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//        while (cursor.moveToNext()) {
//
//            absolutePathOfImage = cursor.getString(column_index_data);
//            listOfAllImages.add(absolutePathOfImage);
//
//        }
//        return listOfAllImages;
//    }//modified
}
