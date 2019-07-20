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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojito.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.ArrayList;

public class PartyActivity extends AppCompatActivity {
    static final int REQ_MAKE_PARTY =5921;
    ArrayList<PartyItem> party_items = new ArrayList<PartyItem>();
    RecyclerView recyclerView;
    PartyAdapter adapter;
    Loadpartys loadpartyTask;
    public PartyActivity(){ }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partyactivity);
        recyclerView = findViewById(R.id.party_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadpartyTask = new Loadpartys();
        loadpartyTask.execute();

        // ADD party Button
        Button make_party = findViewById(R.id.make_party);
        make_party.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PartyActivity.this, MakeParty.class) ;
                startActivityForResult(intent, REQ_MAKE_PARTY);
            }
        });

        // SYNCHRONIZATION PARTY Button
        FloatingActionButton sync_party = findViewById(R.id.sync_Button); //TODO root로 쓰는게 맞나?
        sync_party.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
//                getpartyList();
                recreate();
            }
        });
    }
//
//    public View onCreateView(
//            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.partyactivity, container, false);
////        partyDBAdapter db = new partyDBAdapter(getActivity());
//        recyclerView = root.findViewById(R.id.party_recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        loadpartyTask = new Loadpartys();
//        loadpartyTask.execute();
//
//        // ADD party Button
//        Button make_party = root.findViewById(R.id.make_party);
//        make_party.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(PartyActivity.this, MakeParty.class) ;
//                startActivityForResult(intent, REQ_MAKE_PARTY);
//            }
//        });
//
//        // SYNCHRONIZATION PARTY Button
//        FloatingActionButton sync_party = root.findViewById(R.id.sync_Button); //TODO root로 쓰는게 맞나?
//        sync_party.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View view){
////                getpartyList();
//                recreate();
//            }
//        });
//        return root;
//    }

    // Run loadpartys in Background Thread
    class Loadpartys extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            party_items.clear();
        }

        @Override
        protected String doInBackground(String... args) {
            String xml = "";
            //if (isFirstTime()) { getpartyList(); }
            // Load partys from DB
//            party_items= load_partys();
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            adapter = new PartyAdapter(party_items);
            recyclerView.setAdapter(adapter);
//
//            // EDIT & DELETE & CALL party
//            adapter.setOnItemClickListener(new PartyAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position, int request_code){
//                    switch (request_code){
//                        case REQ_EDIT_party:{
//                            Intent intent = new Intent(getActivity(), Edit_party.class);
//                            intent.putExtra("position",position);
//                            startActivityForResult(intent, request_code);
//                            break;
//                        }
//                        case REQ_DELETE_party:{
//                            partyDBAdapter db = new partyDBAdapter(getActivity());
//                            PartyItem PartyItem = party_items.get(position);
//                            String name = PartyItem.getUser_Name();
//                            String number = PartyItem.getUser_phNumber();
//
//                            db.delete_party(name,number);
//                            party_items.remove(position);
//
//                            adapter.onActivityResult(REQ_DELETE_party,1);
//                            break;
//                        }
//                        case REQ_CALL_party:{
//                            PartyItem PartyItem = party_items.get(position);
//                            String number = PartyItem.getPhNumberChanged();
//                            String tel ="tel:" + number;
//                            startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
//                            break;
//                        }
//                    }
//                }
//            });
        }
    }

//    get Result from add or edit party
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        switch(requestCode){
//            case REQ_ADD_party:{
//                if (resultCode == Activity.RESULT_OK){
//                    partyDBAdapter db = new partyDBAdapter(getActivity());
//
//                    String name = intent.getStringExtra("party_name");
//                    String number = intent.getStringExtra("party_number");
//
//                    PartyItem PartyItem = new PartyItem();
//                    PartyItem.setUser_Name(name);
//                    PartyItem.setUser_phNumber(number);
//
//                    db.insert_party(name,number);
//                    ArrayList<PartyItem> new_party_items = load_partys();
//
//                    int pos = new_party_items.indexOf(PartyItem);
//
//                    party_items.add(pos, PartyItem);
//
//                    adapter.onActivityResult(REQ_ADD_party,1);
//                    break;
//                }
//            }
//
//            case REQ_EDIT_party:{
//                if (resultCode == Activity.RESULT_OK){
//
//                    partyDBAdapter db = new partyDBAdapter(getActivity());
//                    int pos = intent.getIntExtra("position",-1);
//
//                    String new_name = intent.getStringExtra("party_name");
//                    String new_number = intent.getStringExtra("party_number");
//
//                    PartyItem PartyItem = party_items.get(pos);
//
//                    String name = PartyItem.getUser_Name();
//                    String number = PartyItem.getUser_phNumber();
//
//                    PartyItem.setUser_Name(new_name);
//                    PartyItem.setUser_phNumber(new_number);
//
//                    db.update_party(name,number,new_name,new_number);
//
//                    //TODO : ASCENDING ORDER WHEN EDIT
//                    //ArrayList<PartyItem> new_party_items = load_partys();
//                    //int new_pos = new_party_items.indexOf(PartyItem);
//
//                    party_items.set(pos, PartyItem);
//
//                    adapter.onActivityResult(REQ_EDIT_party,1);
//                    break;
//                }
//            }
//        }
//    }
//
//    // check firstTime
//    private Boolean firstTime = null;
//
//    private boolean isFirstTime(){
//        if (firstTime == null) {
//            SharedPreferences mPreferences = getActivity().getSharedPreferences("first_time", Context.MODE_PRIVATE);
//            firstTime = mPreferences.getBoolean("firstTime", true);
//            if (firstTime) {
//                SharedPreferences.Editor editor = mPreferences.edit();
//                editor.putBoolean("firstTime", false);
//                editor.commit();
//            }
//        }
//        return firstTime;
//    }
//    public void getpartyList() {
//
//        Uri uri = partysContract.CommonDataKinds.Phone.CONTENT_URI;
//        String[] projection = new String[]{
//                partysContract.CommonDataKinds.Phone.NUMBER,
//                partysContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                partysContract.partys.PHOTO_ID,
//                partysContract.partys._ID
//        };
//        String[] selectionArgs = null;
//        String sortOrder = partysContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
//        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);
//
//        if (cursor.moveToFirst()) {
//            do {
//                long photo_id = cursor.getLong(2);
//                long person_id = cursor.getLong(3);
//                PartyItem PartyItem = new PartyItem();
//                PartyItem.setUser_phNumber(cursor.getString(0));
//                PartyItem.setUser_Name(cursor.getString(1));
//                PartyItem.setPhoto_id(photo_id);
//                PartyItem.setPerson_id(person_id);
//
//                Bitmap photo = loadpartyPhoto(getActivity().getContentResolver(),person_id,photo_id);
//                PartyItem.setUser_photo(photo);
//
//                // put in database (name,phone)
//                partyDBAdapter db = new partyDBAdapter(getActivity());
//                db.insert_party(cursor.getString(1),cursor.getString(0));
//
//            } while (cursor.moveToNext());
//        }
//    }

//    private ArrayList<PartyItem> load_partys(){
//        partyDBAdapter db = new partyDBAdapter(this);
//        return db.retreive_all_partys();
//    }
//
//
//
//    public Bitmap loadpartyPhoto(ContentResolver cr, long id, long photo_id) {
//        Uri uri = ContentUris.withAppendedId(partysContract.partys.CONTENT_URI, id);
//        InputStream input = partysContract.partys.openpartyPhotoInputStream(cr, uri);
//        if (input != null)
//            return resizingBitmap(BitmapFactory.decodeStream(input));
//        else
//            Log.d("PHOTO","first try failed to load photo");
//
//        byte[] photoBytes = null;
//        Uri photoUri = ContentUris.withAppendedId(partysContract.Data.CONTENT_URI, photo_id);
//        Cursor c = cr.query(photoUri, new String[]{partysContract.CommonDataKinds.Photo.PHOTO}, null, null, null);
//        try {
//            if (c.moveToFirst())
//                photoBytes = c.getBlob(0);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            c.close();
//        }
//
//        if (photoBytes != null)
//            return resizingBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));
//
//        else
//            Log.d("PHOTO", "second try also failed");
//        return null;
//    }
//
//    public Bitmap resizingBitmap(Bitmap oBitmap) {
//        if (oBitmap == null)
//            return null;
//        float width = oBitmap.getWidth();
//        float height = oBitmap.getHeight();
//        float resizing_size = 120;
//        Bitmap rBitmap = null;
//        if (width > resizing_size) {
//            float mWidth = (float) (width / 100);
//            float fScale = (float) (resizing_size / mWidth);
//            width *= (fScale / 100);
//            height *= (fScale / 100);
//
//        } else if (height > resizing_size) {
//            float mHeight = (float) (height / 100);
//            float fScale = (float) (resizing_size / mHeight);
//            width *= (fScale / 100);
//            height *= (fScale / 100);
//        }
//        //Log.d("rBitmap : " + width + "," + height);
//        rBitmap = Bitmap.createScaledBitmap(oBitmap, (int) width, (int) height, true);
//        return rBitmap;
//    }

}