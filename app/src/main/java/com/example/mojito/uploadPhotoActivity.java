package com.example.mojito;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class uploadPhotoActivity extends Activity {

    public View imageview;
    String latitude; //위도
    String longitude;//경도
    String latitudeRef, longitudeRef;
    double lat, lng;//double 위도, 경도
    String nation;//사진을 찍은 나라

    private boolean valid = false;


    public String LATITUDE;
    public String LATITUDE_REF;
    public String LONGITUDE;
    public String LONGITUDE_REF;
    Float Latitude, Longitude;


    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;

    Uri photoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadphotoactivity);

        Intent i = getIntent();
        String welcomupload = i.getStringExtra("upload");


        Button galleryLogin = (Button) findViewById(R.id.gogallery);
        galleryLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToAlbum(); //앨범버튼
            }
        });


        imageview = (ImageView) findViewById(R.id.image1);
//        String filename = Environment.getExternalStorageDirectory() //filename should be changed
//                .getPath() + "/20140705_162816.jpg";
//        try {
//            ExifInterface exif = new ExifInterface(filename);
//            showExif(exif);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
//        }
    }

    private void goToAlbum() { //album으로 가는 과정

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        } //사진을 찍지 않고 돌아왔을때의 error처리

        if (requestCode == PICK_FROM_ALBUM) {

            photoUri = data.getData();

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options); // originalBm은 가져온 사진의 bitmap저장됨

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } //사진을 앨범에서 가져와서 bitmap으로 가져오는 과정
        Knownation(tempFile);
    } //앨범으로 가서 tempFile에 선택한 사진 담아오기

    private void Knownation(File tempFile) {
//        Uri filename = photoUri;
        String stringtemp = tempFile.toString();
        try {
            ExifInterface exif = new ExifInterface(stringtemp);
            showExif(exif);
            findAddress(Latitude, Longitude);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
        }

    } //temFile가지고 showExif에 넘겨주기

    private void showExif(ExifInterface exif) {
        //latitude = getTagString(ExifInterface.TAG_GPS_LATITUDE, exif);


        //longitude = getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif);

//        String myAttribute = "[Exif information] \n\n";
//
//        myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif);
//        myAttribute += getTagString(ExifInterface.TAG_FLASH, exif);
//        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE,
//                exif);
//        myAttribute += getTagString(
//                ExifInterface.TAG_GPS_LATITUDE_REF, exif);
//        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE,
//                exif);
//        myAttribute += getTagString(
//                ExifInterface.TAG_GPS_LONGITUDE_REF, exif);
//        myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH,
//                exif);
//        myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH,
//                exif);
//        myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
//        myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);
//        myAttribute += getTagString(ExifInterface.TAG_ORIENTATION,
//                exif);
//        myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE,
//                exif);
//

//        lat = Double.parseDouble(latitude);
//        lng = Double.parseDouble(longitude);

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
//
//    public int getLatitudeE6(){
//        return (int)(Latitude*1000000);
//    }
//
//    public int getLongitudeE6(){
//        return (int)(Longitude*1000000);
//    }


//    private String getTagString(String tag, ExifInterface exif) { //위도 경도 반환해주기
//        return exif.getAttribute(tag);
//    }
//    private String getTagString(String tag, ExifInterface exif) {
//        return (tag + " : " + exif.getAttribute(tag) + "\n");
//    }

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
                                    ,{"중국", "대만"}
                                    ,{"일본"}
                                    ,{"라오스", "베트남", "말레이시아", "태국", "인도네시아", "필리핀", "싱가포르", "캄보디아", "동티모르", "브루나이","몰디브","인도"}
                                    ,{"미국","캐나다"}
                                    , {"독일", "프랑스", "이탈리아", "영국", "스페인", "그리스", "스위스", "네덜란드", "폴란드", "크로아디아", "오스트리아", "스웨덴", "우크라이나", "벨기에", "헝가리", "체코"}
                                    ,{"이집트","수단","리비아","알제리","튀니지","모로코","이란","요르단"}
                                    ,{"러시아","괌","몽골","호주"}  };

//        String[] Korea = {"대한민국"};
//        String[] ChinaTiwan= {"중국", "대만"};
//        String[] Japan = {"일본"};
//        String[] SouthEastAsiaIndia= {"라오스", "베트남", "말레이시아", "태국", "인도네시아", "필리핀", "싱가포르", "캄보디아", "동티모르", "브루나이","몰디브","인도"};
//        String[] UsaCanada = {"미국","캐나다"};
//        String[] Europe = {"독일", "프랑스", "이탈리아", "영국", "스페인", "그리스", "스위스", "네덜란드", "폴란드", "크로아디아", "오스트리아", "스웨덴", "우크라이나", "벨기에", "헝가리", "체코"};
//        String [] MiddleAsiaAfrica = {"이집트","수단","리비아","알제리","튀니지","모로코","이란","요르단"};
//        String [] Else= {"러시아","괌","몽골","호주"};
        for(int i=0;i<8;i++){
            for(int k=0;k<countryName[i].length;k++){
                if(nation.equals(countryName[i][k]))
                    index = i;
            }
        }
        Log.e("CountryIndex is ...","number: "+index);
    return index; //index가 7이면 그 외 국가임!
    }
}




