package com.example.mojito;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends Activity {

    private GridLayoutManager lLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseactivity);
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
    private List<Item> getAllItemList(){

        List<Item> allItems = new ArrayList<Item>();

                allItems.add(new Item("\n# KOREA", R.drawable.korea));
                allItems.add(new Item("\n# EUROPE", R.drawable.london));
                allItems.add(new Item("\n# USA\n/CANADA", R.drawable.usa));
                allItems.add(new Item("\n# JAPAN", R.drawable.japan));
                allItems.add(new Item("\n# AFRICA", R.drawable.africa));
                allItems.add(new Item("\n# ASIA\n/INDIA", R.drawable.seaisa));
                allItems.add(new Item("\n# CHINA\n/TAIWAN", R.drawable.taiwan));
                allItems.add(new Item("\n# OTHERS", R.drawable.others));





        //modified

        return allItems;
    }

}
