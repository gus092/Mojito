package com.example.mojito.Party;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mojito.Authentication.LoginActivity;
import com.example.mojito.MainActivity;
import com.example.mojito.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MakeParty extends AppCompatActivity{
    static final int REQ_OPEN_MAP = 8438;
    private DatabaseReference mDataBase;
    String User_Name;
    String destination;
    String attribute;
    String description;
    String photo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_party);

        ImageButton openMap = findViewById(R.id.openmap);
        openMap.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MakeParty.this, OpenMap.class) ;
                startActivityForResult(intent,REQ_OPEN_MAP);
            }
        });
//        ImageButton save_party = findViewById(R.id.btn_Save);
//        save_party.setOnClickListener(new ImageButton.OnClickListener(){
//            @
//        });
        

//        TextView write_description = findViewById(R.id.description);
//        write_description.setOnClickListener(new TextView.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQ_OPEN_MAP) : {
                if (resultCode == Activity.RESULT_OK) {
                    TextView show_destinations = findViewById(R.id.destinations);
                    String temp_destination = new String();
                    ArrayList<String> destinations = data.getStringArrayListExtra("destinations");
                        for(int i=0; i<destinations.size(); i++){
                            temp_destination += destinations.get(i)+"\n";
                    }
                    show_destinations.setText(temp_destination);
                }
                break;
            }
        }
    }

        @IgnoreExtraProperties
        public class FirebasePost {
            public String User_Name;
            public String destination;
            public String attribute;
            public String description;
            public String photo;
            public FirebasePost() {
                // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
            }
            public FirebasePost(String User_Name, String destination, String attribute, String description, String photo) {
                this.User_Name = User_Name;
                this.destination = destination;
                this.attribute = attribute;
                this.description = description;
                this.photo = photo;
            }

//
//            public FirebasePost(String destination, String User_Name, String attribute, String description, Bitmap photo) {
//                PartyItem partyItem = new PartyItem(destination, User_Name, attribute, description, photo);
//                mDataBase.child("parties").child(User_Name).setValue(partyItem);
//            }

            @Exclude
            public Map<String, Object> toMap() {
                HashMap<String, Object> result = new HashMap<>();
                result.put("destination", destination);
                result.put("User_Name", User_Name);
                result.put("attribute", attribute);
                result.put("description", description);
                result.put("photo", photo);
                return result;
            }
        }
        //add==True면 데이터 저장, 이미 있으면 업데이트, add==False면 데이터 삭제
        public void postFirebaseDatabase(boolean add){
            mDataBase = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Object> postValues = null;
            if (add) {
                FirebasePost post = new FirebasePost(User_Name, destination, attribute, description, photo);
                postValues = post.toMap();
            }
            childUpdates.put("//", postValues);
            mDataBase.updateChildren(childUpdates);
        }
}
