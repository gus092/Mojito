package com.example.mojito.Party;

import android.app.FragmentManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.mojito.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenMap extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    public String nation;
    public String area;
    ArrayList<String> destinations = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openmap);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ImageButton btn_Save = findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(new ImageButton.OnClickListener(){
          @Override
          public void onClick(View view){
              Intent intent_save = new Intent();
              intent_save.putExtra("destinations", destinations);
              setResult(RESULT_OK,intent_save);
              finish();
          }
        });
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("Here?");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                nation =  findnation(latitude,longitude);
                area = findarea(latitude,longitude);
                mOptions.snippet(area + ", " + nation);
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);
                String destination = area + ", " + nation;
                destinations.add(destination);
            }
        });
        // Add a marker in KAIST
        LatLng KAIST = new LatLng(36.372290, 127.360409);
        mMap.addMarker(new MarkerOptions().position(KAIST).title("KAIST"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(KAIST));
    }

    private String findnation(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> mResultList = geocoder.getFromLocation(lat, lng, 1);
            nation = mResultList.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.e("nation is ....", nation);
        return nation;
    }

    private String findarea(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> mResultList = geocoder.getFromLocation(lat, lng, 1);
            area = mResultList.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.e("area is ....", area);
        return area;
    }


}
