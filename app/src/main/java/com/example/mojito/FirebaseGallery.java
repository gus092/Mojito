package com.example.mojito;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.mojito.Authentication.LoginActivity.userName;


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
    private File tempFile;


    //countryname variable
    String nation;//사진을 찍은 나라

    private boolean valid = false;


    private String LATITUDE;
    private String LATITUDE_REF;
    private String LONGITUDE;
    private String LONGITUDE_REF;
    Float Latitude, Longitude;
    private int uploadPhtocountryName=0; //upload하는 사진의 국가


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

            //find countryName


            Cursor cursor = null;

            try {
                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert  mImageUri != null;
                cursor = getContentResolver().query( mImageUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options); // originalBm은 가져온 사진의 bitmap저장됨

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            Knownation(tempFile);
            //find countryName
            Glide.with(this).load(mImageUri).into(mImageView);
        }
    }
    //countryName added2
    private void Knownation(File tempFile) {
//        Uri filename = photoUri;
        String stringtemp = tempFile.toString();
        try {
            ExifInterface exif = new ExifInterface(stringtemp);
            showExif(exif);
            if(Latitude!=null&&Longitude!=null){
                findAddress(Latitude, Longitude);
            }else{                  // 오류해결,캡쳐사진은 default로 others에 넣기
                nation="러시아";
                uploadPhtocountryName=7;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
        }

    } //temFile가지고 showExif에 넘겨주기

    private void showExif(ExifInterface exif) {

        //modified
        LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        // your Final lat Long Values

        if ((LATITUDE != null)
                && (LATITUDE_REF != null)
                && (LONGITUDE != null)
                && (LONGITUDE_REF != null)) {

            if (LATITUDE_REF.equals("N")) {
                Latitude = convertToDegree(LATITUDE);
            } else {
                Latitude = 0 - convertToDegree(LATITUDE);
            }

            if (LONGITUDE_REF.equals("E")) {
                Longitude = convertToDegree(LONGITUDE);
            } else {
                Longitude = 0 - convertToDegree(LONGITUDE);
            }

        }
        //modified


        Log.e("Image Information : ", Latitude + " , " + Longitude);
    } //사진의 위도 경도 알아내기


    //modified22
    private Float convertToDegree(String stringDMS) {

        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;


    }


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return (String.valueOf(Latitude)
                + ", "
                + String.valueOf(Longitude));
    }

    private String findAddress(double lat, double lng) {
        //StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> mResultList = geocoder.getFromLocation(lat, lng, 1);
            nation = mResultList.get(0).getCountryName();


        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("nation is ....", nation);
        countryIndex(nation);


        return nation;
    } //위도 경도를 받아서 나라 반환해주기

    private int countryIndex(String nation) { //분류 다시 할 필요!!
        int index = 7;
        String [][] countryName =
                { {"대한민국"}
                        , {"독일", "프랑스", "이탈리아", "영국", "스페인", "그리스", "스위스", "네덜란드", "폴란드", "크로아디아", "오스트리아", "스웨덴", "우크라이나", "벨기에", "헝가리", "체코"}
                        ,{"미국","캐나다"}
                        ,{"일본"}
                        ,{"이집트","수단","리비아","알제리","튀니지","모로코","이란","요르단"}
                        ,{"라오스", "베트남", "말레이시아", "태국", "인도네시아", "필리핀", "싱가포르", "캄보디아", "동티모르", "브루나이","몰디브","인도"}
                        ,{"중국", "대만"}
                        ,{"러시아","괌","몽골","호주"} };

        for(int i=0;i<8;i++){
            for(int k=0;k<countryName[i].length;k++){
                if(nation.equals(countryName[i][k]))
                    index = i;
            }
        }
        Log.e("CountryIndex is ...","number: "+index);
        uploadPhtocountryName = index;
        return index; //index가 7이면 그 외 국가임!
    }



    //countryName added2




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
                                    ArrayList<String> likedlist = new ArrayList<>();
                                    //likedlist.add(userName);
                                    convertingurl=  uri.toString();
                                    Uri downloadUrl = uri;
                                    Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),downloadUrl.toString(),uploadPhtocountryName,userName,likedlist);
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);
                                    Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl.toString() , Toast.LENGTH_SHORT).show();
                                }
                            });
                            //added

                            Toast.makeText(FirebaseGallery.this, "Upload successful", Toast.LENGTH_LONG).show();
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
