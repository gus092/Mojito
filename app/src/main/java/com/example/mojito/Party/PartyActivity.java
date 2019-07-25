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

public class PartyActivity extends AppCompatActivity implements PartyAdapter.OnItemClickListener2 {
    static final int REQ_MAKE_PARTY = 5921;
    public static ArrayList<PartyItem> party_items; //누나꺼에서 mUploads
    RecyclerView recyclerView;
    private PartyAdapter partyAdapter;
    private ValueEventListener mDBListener;
    private DatabaseReference mDatabaseRef;
    PartyItem party_item;
    String key;

    //
//    Loadpartys loadpartyTask;
    public PartyActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partyactivity);
        recyclerView = findViewById(R.id.party_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<PartyItem> party_items = new ArrayList<PartyItem>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("parties");

//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
//                    String key = postSnapshot.getKey();
//                    DatabaseReference keyRef = mDatabaseRef.child(key);
//                    ValueEventListener eventListener = new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                            PartyItem partyItem = postSnapshot.getValue(PartyItem.class);
////                            party_items.add(partyItem);
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {}
//                    };
//                    PartyItem partyItem = postSnapshot.child(key).getValue(PartyItem.class);
//                    party_items.add(partyItem);
//                    keyRef.addListenerForSingleValueEvent(eventListener);
//                }
//
//                partyAdapter = new PartyAdapter(party_items);
//                partyAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(partyAdapter);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
//        };
        // mDatabaseRef.addListenerForSingleValueEvent(valueEventListener);
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                party_items.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    PartyItem partyItem = postSnapshot.child(key).getValue(PartyItem.class);
                    party_items.add(partyItem);
//                    PartyItem partyItem = postSnapshot.getValue(PartyItem.class);
//                    partyItem.setKey(key);
                    //Log.e("count","the number of count.."+count);
//                }
//        mDatabaseRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String key = dataSnapshot.getKey();
//                PartyItem partyItem = dataSnapshot.child(key).getValue(PartyItem.class);
//                party_items.add(partyItem);
////                partyAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(partyAdapter);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


//                party_items.add( dataSnapshot.child("-LkbxheYVLBWl-lw-G99").getValue(PartyItem.class));
//                partyAdapter = new PartyAdapter(party_items);
//                partyAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(partyAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
//
        // ADD party Button
        ImageButton make_party = findViewById(R.id.make_party);
        make_party.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PartyActivity.this, MakeParty.class) ;
                startActivityForResult(intent, REQ_MAKE_PARTY);
            }
        });

        ImageButton match_party = findViewById(R.id.btn_Match);
        match_party.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){

            }

        });
                // SYNCHRONIZATION PARTY Button
//        FloatingActionButton sync_party = findViewById(R.id.sync_Button); //TODO root로 쓰는게 맞나?
//        sync_party.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View view){
////                getpartyList();
//                recreate();
//            }
//        })
        }
        @Override
        public void onItemClick ( int position){ //파티 클릭했을 때
            Intent party_Intent = new Intent(PartyActivity.this, ShowParty.class);
            party_item = party_items.get(position);
            party_Intent.putExtra("position", position);
            party_Intent.putExtra("destination", party_item.getDestination());
            party_Intent.putExtra("capacity", party_item.getCapacity());
            party_Intent.putExtra("number", party_item.getNum_people());
            party_Intent.putExtra("attribute", party_item.getAttribute());
            party_Intent.putExtra("description", party_item.getDescription());
            startActivity(party_Intent);
        }
        @Override
        protected void onDestroy(){
            super.onDestroy();
            mDatabaseRef.removeEventListener(mDBListener);
        }
    }

