package com.example.mojito.Party;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mojito.Authentication.LoginActivity;
import com.example.mojito.FirebaseGallery;
import com.example.mojito.MainActivity;
import com.example.mojito.R;
import com.firebase.ui.auth.data.model.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.mojito.Authentication.LoginActivity.UserName;

public class MakeParty extends AppCompatActivity {
    static final int REQ_OPEN_MAP = 8438;
    private DatabaseReference mPostReference;

    String User_Name;
    String destination = "";
    String attribute = "";
    String description = "";
    Integer capacity;
    Integer num_people = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_party);
        ImageButton openMap = findViewById(R.id.openmap);
        openMap.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakeParty.this, OpenMap.class);
                startActivityForResult(intent, REQ_OPEN_MAP);
            }
        });

        ImageButton save_party = findViewById(R.id.btn_Save);
        save_party.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChipGroup chipgroup1 = findViewById(R.id.chipgroup1);
                Chip selected1 = findViewById(chipgroup1.getCheckedChipId());
                attribute += selected1.getText();
                ChipGroup chipgroup2 = findViewById(R.id.chipgroup2);
                Chip selected2 = findViewById(chipgroup2.getCheckedChipId());
                attribute += " "+selected2.getText();
                ChipGroup chipgroup3 = findViewById(R.id.chipgroup3);
                Chip selected3 = findViewById(chipgroup3.getCheckedChipId());
                attribute += " "+selected3.getText();
                ChipGroup chipgroup4 = findViewById(R.id.chipgroup4);
                Chip selected4 = findViewById(chipgroup4.getCheckedChipId());
                attribute += " "+selected4.getText();
                ChipGroup chipgroup5 = findViewById(R.id.chipgroup5);
                Chip selected5 = findViewById(chipgroup5.getCheckedChipId());
                attribute += " "+selected5.getText();
                ChipGroup chipgroup6 = findViewById(R.id.chipgroup6);
                Chip selected6 = findViewById(chipgroup6.getCheckedChipId());
                attribute += " "+selected6.getText();

                EditText edit_description = findViewById(R.id.description);
                description = edit_description.getText().toString();

                RatingBar ratingBar = findViewById(R.id.capacity);
                capacity = (int) ratingBar.getRating();

                if (destination.length()==0) {
                    Toast.makeText(MakeParty.this, "Select your destination", Toast.LENGTH_SHORT).show();
                } else if (attribute.length()==0) {
                    Toast.makeText(MakeParty.this, "Select some chips", Toast.LENGTH_SHORT).show();
                } else if (description.length()==0) {
                    Toast.makeText(MakeParty.this, "Tell us more!", Toast.LENGTH_SHORT).show();
                } else if (capacity < 2) {
                    Toast.makeText(MakeParty.this, "Capacity must exceed 1", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabase(true);
                    finish();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQ_OPEN_MAP): {
                if (resultCode == Activity.RESULT_OK) {
                    TextView show_destinations = findViewById(R.id.destinations);
                    ArrayList<String> destinations = data.getStringArrayListExtra("destinations");
                    for (int i = 0; i < destinations.size(); i++) {
                        destination += destinations.get(i) + "\n";
                    }
                    show_destinations.setText(destination);
                }
                break;
            }
        }
    }

//    private void uploadParty(String User_Name, String destination, String attribute, String description, Integer capacity, Integer num_people) {
//        PartyItem partyItem = new PartyItem(User_Name, destination, attribute, description, capacity, num_people);
//        String uploadId = mDataBase.push().getKey();
//        mDataBase.child("parties").child(uploadId).setValue(partyItem);
//        Toast.makeText(getBaseContext(), "Upload completed, " + User_Name, Toast.LENGTH_SHORT).show();
//    }

    //add==True면 데이터 저장, 이미 있으면 업데이트, add==False면 데이터 삭제
    public void postFirebaseDatabase(boolean add) {
        mPostReference = FirebaseDatabase.getInstance().getReference("parties");
        User_Name = UserName;
        String uploadId = mPostReference.push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if (add) {
            PartyItem partyItem = new PartyItem(User_Name, destination, attribute, description, capacity, num_people);
            postValues = partyItem.toMap();
        }
        String[] temp = User_Name.split("@");
        mPostReference.child(temp[0]+uploadId).setValue(postValues);
        Toast.makeText(getBaseContext(), "Upload successful, " + User_Name , Toast.LENGTH_SHORT).show();
    }

}