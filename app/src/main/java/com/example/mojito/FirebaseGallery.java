package com.example.mojito;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mojito.ImagesActivity;
import com.example.mojito.R;
import com.example.mojito.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


public class FirebaseGallery extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    public StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;


    public Uri downloadUrl;
    public String convertingurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebasegallery);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(FirebaseGallery.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
    }

    private void openFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Log.e("AAAAAAAAA  AAAAAA",""+mImageUri);
            Log.e("BBBBBBBBBBBBBBB ",""+ getRealPathFromUri(getApplicationContext(),mImageUri));

            Glide.with(this).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) { //사진 절대경로찾아줌
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void uploadFile() {

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));


            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            //added

//                            fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                String instead;
//                                @Override
//                                public void onComplete(@NonNull Task<Uri> task) {
//                                    String profileImageUrl = task.getResult().toString();
//                                    instead = task.getResult().toString();
//                                    Log.e("URL",profileImageUrl);
//                                }
//                            });

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    convertingurl=  uri.toString();
                                    Uri downloadUrl = uri;
                                    Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),downloadUrl.toString());
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);
                                    Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl.toString() , Toast.LENGTH_SHORT).show();
                                }
                            });
                            //added

                            Toast.makeText(FirebaseGallery.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Log.e("fileReference"," is "+fileReference);
                            Log.e("convertingurl"," is "+convertingurl);

                           Log.e("fileReference222","is"+fileReference.getDownloadUrl());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FirebaseGallery.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagesActivity() {
        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
    }
}


//
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.view.View;
//import android.webkit.MimeTypeMap;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.StorageTask;
//import com.google.firebase.storage.UploadTask;
//
//public class FirebaseGallery extends AppCompatActivity {
//    private static final int PICK_IMAGE_REQUEST = 1;
//
//    private Button mButtonChooseImage;
//    private Button mButtonUpload;
//    private TextView mTextViewShowUploads;
//    private EditText mEditTextFileName;
//    private ImageView mImageView;
//    private ProgressBar mProgressBar;
//
//
//    private Uri mImageUri;
//
//    private StorageReference mStorageRef;
//    private DatabaseReference mDatabaseRef;
//
//    private StorageTask mUploadTask;
//
//
//    @Override
//    protected void onCreate(Bundle saveInstanceState){
//        super.onCreate(saveInstanceState);
//        setContentView(R.layout.firebasegallery);
//
//        Intent i = getIntent();
//        String welcomupload = i.getStringExtra("upload");
//
//
//        mButtonChooseImage=findViewById(R.id.button_choose_image);
//        mButtonUpload=findViewById(R.id.button_upload);
//        mTextViewShowUploads=findViewById(R.id.text_view_show_upload);
//        mEditTextFileName=findViewById(R.id.edit_text_file_name);
//        mImageView=findViewById(R.id.image_view);
//        mProgressBar=findViewById(R.id.progress_bar);
//
//        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
//
//        mButtonChooseImage.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                openFileChooser();
//            }
//        });
//        mButtonUpload.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//
//                if(mUploadTask != null && mUploadTask.isInProgress()){
//                    Toast.makeText(FirebaseGallery.this,"Upload in progress",Toast.LENGTH_SHORT).show();
//                }else{
//                    uploadFile();
//                }
//
//
//            }
//        });
//        mTextViewShowUploads.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                openImageActivity();
//
//            }
//        });
//
//    }
//    private void openFileChooser(){
////        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
//
//
//
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//       // startActivityForResult(intent, PICK_FROM_ALBUM);
//        startActivityForResult(intent,PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//    super.onActivityResult(requestCode,resultCode,data);
//
//    if(requestCode == PICK_IMAGE_REQUEST &&resultCode ==RESULT_OK&&data!=null &&data.getData()!=null){
//
//        mImageUri=data.getData();
//
//        Glide.with(this).load(mImageUri).into(mImageView);
//
//        }
//    }
//    private String getFileExtension(Uri uri){
//        ContentResolver cR = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//    }
//    private void uploadFile(){
//        if(mImageUri != null){
//            StorageReference fileReference = mStorageRef.child(+System.currentTimeMillis()+"."+getFileExtension(mImageUri));
//
//            mUploadTask= fileReference.putFile(mImageUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Handler handler = new Handler();//mProgressBar.setProgress(0);
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mProgressBar.setProgress(0);
//                                }
//                            },500);
//                            Toast.makeText(FirebaseGallery.this, "Upload successful",Toast.LENGTH_LONG).show();
//                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),taskSnapshot.getMetadata().getReference().getDownloadUrl().toString()); //getdownloadurl
//                            String uploadId = mDatabaseRef.push().getKey();
//                            mDatabaseRef.child(uploadId).setValue(upload);
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(FirebaseGallery.this,e.getMessage(),Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//                            mProgressBar.setProgress((int)progress);
//                        }
//                    });
//        }else{
//            Toast.makeText(this,"no file selected",Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    private  void openImageActivity(){
//        Intent intent = new Intent(this, ImagesActivity.class);
//        startActivity(intent);
//
//    }
//
//
//}
