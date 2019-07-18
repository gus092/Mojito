package com.example.mojito;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class Tab1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.tab1, container, false);


                    Button uploadLogin = (Button)view.findViewById(R.id.uploadbtn);
                    uploadLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), uploadPhotoActivity.class);
                            i.putExtra("upload", "upload");
                            startActivity(i);
                        }
                    });
            return view;
    }
}


