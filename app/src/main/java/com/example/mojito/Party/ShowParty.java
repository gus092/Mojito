package com.example.mojito.Party;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mojito.ImagesActivity;
import com.example.mojito.R;
import com.example.mojito.Upload;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

import static com.example.mojito.Authentication.LoginActivity.userName;
import static com.example.mojito.Party.PartyActivity.party_items;

public class ShowParty extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    Intent intent;
    String temp_string;
    Integer position;
    ArrayList<String> temp_array;
    String temp_members="";
    int i;
    int love;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_party);
        ImageButton btn_join = findViewById(R.id.btn_join);
        intent = getIntent();
        btn_join.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                position= intent.getIntExtra("position",1234);
                join(position);
            }
        });
        TextView destination = findViewById(R.id.destination);
        destination.setText(intent.getStringExtra("destination"));
        destination.setSelected(true);
        RatingBar memberbar = findViewById(R.id.capacity2);
        memberbar.setNumStars(intent.getIntExtra("capacity",1));
        TextView membername = findViewById(R.id.members);
        temp_array = intent.getStringArrayListExtra("members");
        for (i=0; i<temp_array.size();i++){
            temp_members=temp_members+temp_array.get(i)+"\n";
        }
        membername.setText(temp_members);
        memberbar.setRating(temp_array.size());
        temp_string =intent.getStringExtra("attribute");
        String[] attributes = temp_string.split(" ");
        Chip chip1 = findViewById(R.id.chip1_1);
        chip1.setText(attributes[0]);
        Chip chip2 = findViewById(R.id.chip1_2);
        chip2.setText(attributes[1]);
        Chip chip3 = findViewById(R.id.chip1_3);
        chip3.setText(attributes[2]);
        Chip chip4 = findViewById(R.id.chip1_4);
        chip4.setText(attributes[3]);
        Chip chip5 = findViewById(R.id.chip1_5);
        chip5.setText(attributes[4]);
        Chip chip6 = findViewById(R.id.chip1_6);
        chip6.setText(attributes[5]);
        Chip chip7 = findViewById(R.id.chip1_7);
        chip7.setText(attributes[6]);
        Chip chip8 = findViewById(R.id.chip1_8);
        chip8.setText(attributes[7]);
        Chip chip9 = findViewById(R.id.chip1_9);
        chip9.setText(attributes[8]);
        Chip chip10 = findViewById(R.id.chip1_10);
        chip10.setText(attributes[9]);
        TextView description = findViewById(R.id.description2);
        description.setText(intent.getStringExtra("description"));

    }
    public void join(int position){
        new AlertDialog.Builder(this)
                .setTitle("Join")
                .setMessage("Would you like to join this group?")
                .setIcon(R.drawable.lime2)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //확인시 처리 로직
                        PartyItem selectedItem = party_items.get(position);
                        String selectedKey = selectedItem.getKey();
                        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("parties");
                        mDatabaseRef.orderByChild("members").addListenerForSingleValueEvent(new ValueEventListener() {
                            ArrayList<String> members;
                            Integer number_people;
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                PartyItem partyitem = dataSnapshot.child(selectedKey).getValue(PartyItem.class);
                                members = partyitem.getMembers();
                                number_people = partyitem.getNum_people();
                                if (members.contains(userName)) {
                                    Toast.makeText(ShowParty.this, "You are already in party", Toast.LENGTH_SHORT).show();
                                }
                                else if(number_people==partyitem.getCapacity()){
                                    Toast.makeText(ShowParty.this, "Already full", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    members.add(userName);
                                    mDatabaseRef.child(selectedKey).child("members").setValue(members);
                                    Toast.makeText(ShowParty.this, "Success!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                        Toast.makeText(ShowParty.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }})
                .show();

    }
}