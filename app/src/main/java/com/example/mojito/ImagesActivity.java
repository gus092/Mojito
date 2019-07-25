package com.example.mojito;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mojito.Party.PartyActivity;
import com.example.mojito.R;
import com.example.mojito.Upload;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
        //getActionBar().hide();

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

        //upload 페이지 전환
        final FloatingActionButton firebaseUploadBtn = findViewById(R.id.firebaseUpload_btn);
        firebaseUploadBtn.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),FirebaseGallery.class);
                startActivity(intent);
                finish();
            }
        });





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
        //Toast.makeText(getBaseContext(),"This is about clicklistener", Toast.LENGTH_SHORT).show();

        Upload selectedItem2 = mUploads.get(position);
        final String selectedKey = selectedItem2.getKey();


        FirebaseDatabase  database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef2 = database.getReference().child("uploads");
        //DatabaseReference messagesRef2 =  mDatabaseRef.child(selectedKey);

        mDatabaseRef2.orderByChild("mlikedUserList").addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<String> likedUsers;
            int flag=0; //like를 누른 사람중 같은 id가 있는지 확인하는작업
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
//                    Upload user1 = userSnapshot.getValue(Upload.class);
//                    likedUsers = user1.getmlikedUserList();
//                  //  Log.e("like list is ...","..."+ user1.getmwriter());
//                }
                Upload user1= dataSnapshot.child(selectedKey).getValue(Upload.class);
                likedUsers = user1.getmlikedUserList();

                for(int i =0;i<likedUsers.size();i++){
                    //System.out.println("===================================================="+likedUsers.get(i));
                    if(likedUsers.get(i).equals(userName)){ //이미 like버튼을 누른 목록에 있음
                        //flag그대로 0
                        flag=0;
                    }else{
                        flag=1;
                  }
                }
                if(flag==1){ //새로 like를 누른다면
                    likedUsers.add(userName);
                    mDatabaseRef2.child(selectedKey).child("mlikedUserList").setValue(likedUsers);
                    Toast.makeText(getBaseContext(),"'좋아요'", Toast.LENGTH_SHORT).show();
                }else{
                    likedUsers.remove(userName);
                    mDatabaseRef2.child(selectedKey).child("mlikedUserList").setValue(likedUsers);
                    Toast.makeText(getBaseContext(),"'좋아요 취소'", Toast.LENGTH_SHORT).show();
                }
                flag=0;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

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
    } //사진 삭제 in DB

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}