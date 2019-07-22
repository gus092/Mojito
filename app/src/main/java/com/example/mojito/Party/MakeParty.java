package com.example.mojito.Party;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mojito.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

public class MakeParty extends AppCompatActivity{
    static final int REQ_OPEN_MAP = 8438;
    private DatabaseReference mDataBase;
    String User_Name;
    String destination;
    String attribute;
    String description;
    String photo;
    public ImageButton openMap;

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
