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
import static com.example.mojito.RecyclerViewHolders.printName;


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

        mTextViewShowUploads.setText("Go back to\n"+ printName + "\n Gallery");

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
                        ,{"미얀마","라오스", "베트남", "말레이시아", "태국", "인도네시아", "필리핀", "몰디브","싱가포르", "캄보디아", "동티모르", "브루나이","인도","네팔","방글라데시","부탄","스리랑카"}
                        ,{"미국","캐나다","가이아나","과테말라","그레나다","니카라과","도미니카공화국","도미니카연방","멕시코","바베이도스","바하마","베네수엘라","볼리바르","벨리즈","볼리비아","브라질","세인트루시아세인트빈센트그레나딘","세인트키츠네비스","수리남","아르헨티나","아이티","앤티가바부다","에콰도르","엘살바도르","온두라스","우루과이","자메이카","칠레","코스타리카","콜롬비아","쿠바","트리니다드토바고","파나마","파라과이","페루"}
                        ,{"일본"}
                        ,{"리비아","모로코","이집트","튀니지","가나","가봉","감비아","기니","기니비사우","나미비아","나이지리아","남수단","남아프리카공화국","니제르","라이베리아","레소토","르완다","마다가스카르","말라위","말리","모리셔스","모잠비크","베냉","보츠와나","부룬디","부르키나파소","상투메프린시페","세네갈","세이셸","소말리아","수단","시에라리온","앙골라","에리트레아","에스와티니","에티오피아","우간다","잠비아","적도기니","중앙아프리카","지부티","짐바브웨","차드","카메룬","카보베르데","케냐","코모로","코트디부아르","콩고","콩고민주공화국","탄자니아","토고","이란","요르단","레바논","모리타니아","바레인","북키프로스","사우디아라비아","시리아","아랍에미레이트","아르메니아","아제르바이잔","예멘","오만","요르단","이라크","이스라엘","카타르","쿠웨이트","키프로스","터키","팔레스타인","조지아"}
                        ,{"교황청","그리스","네덜란드","노르웨이","덴마크","독일","라트비아","루마니아","룩셈부르크","리투아니아","리히텐슈타인","모나코","몬테네그로","몰도바","몰타","벨기에","벨라루스","보스니아-헤르체고비나","북마케도니아","불가리아","사이프러스","산마리노","세르비아","스웨덴","스위스","스페인","슬로바키아","슬로베니아","아이슬란드","아일랜드","안도라","알바니아","에스토니아","영국","오스트리아","우크라이나","이탈리아","체코","코소보","크로아티아","터키","포르투갈","폴란드","프랑스","핀란드","헝가리"}
                        ,{"중국", "대만"}
                        ,{"나우루","뉴질랜드","니우에","마셜제도","마이크로네시아연방","바누아투","사모아","솔로몬제도","쿡제도","키리바시","통가","투발루","파푸아뉴기니","팔라우","피지","호주"}
                        ,{"러시아","몽골","우즈베키스탄","카자흐스탄","키르기스스탄","타지키스탄","투르크메니스탄"} };

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
                                    likedlist.add("a");
                                    likedlist.add("b");
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
