package com.example.mojito.Party;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mojito.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

public class MakeParty extends AppCompatActivity {
    private DatabaseReference mDataBase;
    public String User_Name;
    public String destination;
    public String attribute;
    public String description;
    public Bitmap photo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_party);
    }

        @IgnoreExtraProperties
        public class FirebasePost {
            public FirebasePost() {
                // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
            }

            public FirebasePost(String destination, String User_Name, String attribute, String description, Bitmap photo) {
                PartyItem partyItem = new PartyItem(destination, User_Name, attribute, description, photo);
                mDataBase.child("parties").child(User_Name).setValue(partyItem);
            }

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
                FirebasePost post = new FirebasePost(destination, User_Name, attribute, description, photo);
                postValues = post.toMap();
            }
            childUpdates.put("/parties/", postValues);
            mDataBase.updateChildren(childUpdates);
        }
}
