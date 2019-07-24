package com.example.mojito;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mojito.R;
import com.example.mojito.Upload;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.mojito.Authentication.LoginActivity.userName;
import static com.example.mojito.RecyclerViewHolders.countryGalleryNumber;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener,ImageAdapter.OnItemLongClickListener {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagesactivity);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();



        //delete added
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ImageAdapter(ImagesActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ImagesActivity.this);
        mAdapter.setOnItemLongClickListener(ImagesActivity.this);
        mStorage= FirebaseStorage.getInstance();
        //delete added

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    //Log.e("upload에 들어가는것...","::::: "+ upload);

                        if(upload.getcountryName()==countryGalleryNumber){ // countryname별로 띄워줌
                            mUploads.add(upload);
                        }
                    //Log.e("count","the number of count.."+count);
                }

                Log.e("mUpload's size","is"+mUploads.size());

              //  mAdapter = new ImageAdapter(ImagesActivity.this, mUploads);
                //mRecyclerView.setAdapter(mAdapter);

//goback
                //reverse ver added
//                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                mLayoutManager.setReverseLayout(true);
//                mLayoutManager.setStackFromEnd(true);
//                mRecyclerView.setLayoutManager(mLayoutManager);
//                //reverse ver added
//
//                mRecyclerView.setAdapter(mAdapter);
//                mAdapter.setOnItemClickListener(ImagesActivity.this);
//                mAdapter.setOnItemLongClickListener(ImagesActivity.this);
//GOBACK
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
    @Override
    public void onItemClick(int position){ //사진 클릭했을때
        Toast.makeText(getBaseContext(),"This is about clicklistener", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDeleteClick(int position){ //사진 클릭했을때

        new AlertDialog.Builder(this)
                .setTitle("사진삭제")
                .setMessage("사진을 사진하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                         //확인시 처리 로직

                        Upload selectedItem = mUploads.get(position);
                        final String selectedKey = selectedItem.getKey();

                        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
                      //  StorageReference writerRef= mStorage.getReference(selectedItem.getmwriter());
      //writer detect added
//                      DatabaseReference messagesRef =  mDatabaseRef.child(selectedKey);
//                        ValueEventListener eventListener = new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    //for (DataSnapshot nameSnapshot : ds.child("uploads").getChildren()) {
//                                        Upload name = ds.getValue(Upload.class);
//                                        String compareName = name.getmwriter();
//                                        Log.e("TAG", compareName);
//                                   // }
//                                }
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {}
//                        };
//                        messagesRef.addListenerForSingleValueEvent(eventListener);
      //writer added
                        mDatabaseRef.orderByChild("mwriter").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                            //글쓴이만 사진을 지울 수 있도록 포스팅된 writer값 datasnapshot에 받아오기
                            //글쓴이만 사진삭제할수 있도록 처리
                            //우선 userName으로 sort하고 datasnapshot에서 해당 key로 selectitem을 지정해서 제거
                            //compareNaem에는 선택한 사진의 게시자 아이디가 들어있음.
                            //이걸 로그인한 아이디와 비교하기
                            String compareName="noname";
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                   Upload user1 = userSnapshot.getValue(Upload.class);
                                   compareName = user1.getmwriter();
                                    Log.e("userSnapshot","..."+ user1.getmwriter());
                                }

                                Log.e("writerRef","is...."+  mDatabaseRef.child(selectedKey).equals(userName));
                               // Log.e("writerRef22222222","is...."+ writerRef);

                                if(compareName.equals(userName)){
                                    Log.e("compareName","Here is...."+ compareName);

                                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mDatabaseRef.child(selectedKey).removeValue();
                                            Toast.makeText(ImagesActivity.this, "삭제를 완료했습니다.", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }else{
                                    Toast.makeText(ImagesActivity.this,"삭제권한이 없습니다.",Toast.LENGTH_SHORT).show();
                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                      });




                       // finish();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                        Toast.makeText(ImagesActivity.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }})
                .show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}
//onDeleteClick(int position);



//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ImagesActivity extends AppCompatActivity {
//    private RecyclerView mRecyclerView;
//    private  ImageAdapter mAdapter;
//
//    private ProgressBar mProgressCircle;
//
//    private DatabaseReference mDatabaseRef;
//    private List<Upload> mUploads;
//
//
//    @Override
//    protected void onCreate(Bundle saveInstanceState) {
//        super.onCreate(saveInstanceState);
//        setContentView(R.layout.imagesactivity);
//
//        mRecyclerView = findViewById(R.id.recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//
//        mProgressCircle = findViewById(R.id.progress_circle);
//
//        mUploads = new ArrayList<>();
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
//
//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int count=0;
//                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
//                    Upload upload = postSnapshot.getValue(Upload.class);
//                    mUploads.add(upload);
//                    Log.e("I'm here","count"+count);
//                    count++;
//                }
//                Log.e("count,,,,","count"+count);
//                mAdapter = new ImageAdapter(ImagesActivity.this, mUploads);
//                mRecyclerView.setAdapter(mAdapter);
//                mProgressCircle.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(ImagesActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//}
