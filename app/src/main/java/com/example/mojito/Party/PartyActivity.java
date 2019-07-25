package com.example.mojito.Party;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojito.ImageAdapter;
import com.example.mojito.R;
import com.example.mojito.Upload;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.Map;

public class PartyActivity extends AppCompatActivity implements PartyAdapter.OnItemClickListener{
    static final int REQ_MAKE_PARTY = 5921;
    public static ArrayList<PartyItem> party_items = new ArrayList<PartyItem>();
    RecyclerView recyclerView;
    private PartyAdapter partyAdapter;
    private ValueEventListener mDBListener;
    private DatabaseReference mDatabaseRef;
    PartyItem party_item;
    String key;
    //
//    Loadpartys loadpartyTask;
    public PartyActivity(){ }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partyactivity);
        recyclerView = findViewById(R.id.party_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
//        ArrayList<PartyItem> party_items = new ArrayList<PartyItem>();
//        ArrayList<PartyItem> party_items222 = new ArrayList<PartyItem>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("parties");
        PartyAdapter partyAdapter = new PartyAdapter(party_items);//추가한거
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                party_items.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    PartyItem partyItem = postSnapshot.getValue(PartyItem.class);
                    party_items.add(partyItem);
                    partyItem.setKey(key);
                }
                PartyAdapter partyAdapter = new PartyAdapter(party_items);
                partyAdapter.notifyDataSetChanged();
                partyAdapter.setOnItemClickListener(PartyActivity.this);
                recyclerView.setAdapter(partyAdapter);
                System.out.println("-------------------------------" + party_items.size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
//
        // ADD party Button
        ImageButton make_party = findViewById(R.id.make_party);
        make_party.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PartyActivity.this, MakeParty.class);
                startActivityForResult(intent, REQ_MAKE_PARTY);
            }
        });


    }
    @Override
    public void onItemClick(View v, int position){
        Intent party_Intent = new Intent(PartyActivity.this, ShowParty.class);
        party_item = party_items.get(position);
        party_Intent.putExtra("position", position);
        party_Intent.putExtra("destination", party_item.getDestination());
        party_Intent.putExtra("capacity", party_item.getCapacity());
        party_Intent.putExtra("members", party_item.getMembers());
        party_Intent.putExtra("number", party_item.getMembers().size());
        party_Intent.putExtra("attribute", party_item.getAttribute());
        party_Intent.putExtra("description", party_item.getDescription());
        startActivity(party_Intent);
    }
    //
//
//    @Override
//    public void onItemClick(View v, int position) {
//        Intent party_Intent = new Intent(PartyActivity.this, ShowParty.class);
//        party_item = party_items.get(position);
//        party_Intent.putExtra("position",position);
//        party_Intent.putExtra("destination", party_item.getDestination());
//        party_Intent.putExtra("capacity", party_item.getCapacity());
//        party_Intent.putExtra("number", party_item.getNum_people());
//        party_Intent.putExtra("attribute",party_item.getAttribute());
//        party_Intent.putExtra("description", party_item.getDescription());
//        startActivity(party_Intent);
//}
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}
