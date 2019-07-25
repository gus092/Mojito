package com.example.mojito;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojito.Party.PartyActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity{
    private GridLayoutManager lLayout;
    private TextView tvData;
    private FloatingActionButton btnParty;
    String[] permission_list = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        checkPermission();
        final FloatingActionButton btnParty = findViewById(R.id.findparty);
        btnParty.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent_party = new Intent(getBaseContext(), PartyActivity.class);
                startActivity(intent_party);
            }
        });

    }

    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        List<String> PermissionList = new ArrayList<>();
        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);
            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                PermissionList.add(permission);
            }
        }
        if(!PermissionList.isEmpty()){
            requestPermissions( PermissionList.toArray(new String[PermissionList.size()]),0);
        }
        else{
            initialize();
        }
    }

    //물어보는 건 두갠데 grandResult의 길이가 3
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            if(grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Required", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Required", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            initialize();
        }
    }

    private List<Item> getAllItemList(){
        List<Item> allItems = new ArrayList<Item>();
                allItems.add(new Item("# KOREA", R.drawable.korea3));
                allItems.add(new Item("# EUROPE", R.drawable.london));
                allItems.add(new Item("\n# USA\n/CANADA", R.drawable.usa));
                allItems.add(new Item("# JAPAN", R.drawable.japan3));
                allItems.add(new Item("# AFRICA", R.drawable.africa));
                allItems.add(new Item("\n# ASIA\n/INDIA", R.drawable.seaisa));
                allItems.add(new Item("\n# CHINA\n/TAIWAN", R.drawable.taiwan));
                allItems.add(new Item("# OTHERS", R.drawable.others2));
        return allItems;
    }
    public void initialize(){
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
}

