package com.example.mojito.Authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mojito.MainActivity;
import com.example.mojito.Party.PartyActivity;
import com.example.mojito.R;
import com.facebook.CallbackManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "FacebookLogin";
    private FirebaseAuth mAuth;
    final static int RC_SIGN_IN = 0202;
    final static int REQ_EMAIL_LOGIN = 0203;
    private CallbackManager mCallbackManager;
    // Choose authentication providers
//    List<AuthUI.IdpConfig> providers = Arrays.asList(
//            new AuthUI.IdpConfig.FacebookBuilder().build());

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        Button btn_emailSignIn = findViewById(R.id.btn_emailSignIn);
        Button btn_facebookSignIn = findViewById(R.id.btn_facebookSignIn);
        btn_emailSignIn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent_email = new Intent(LoginActivity.this, EmailPasswordActivity.class);
                startActivityForResult(intent_email,REQ_EMAIL_LOGIN);
            }
        });
        //btn_facebookSignIn.setOnClickListener(LoginActhis);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQ_EMAIL_LOGIN) : {
                if (resultCode == Activity.RESULT_OK) {
                    Intent User_Name_Intent = new Intent(LoginActivity.this, MainActivity.class);
                    User_Name_Intent.putExtra("User_Name", data.getStringExtra("User_Name"));
                    startActivity(User_Name_Intent);
                }
                break;
            }
        }
    }
//
//    // Choose authentication providers
//    List<AuthUI.IdpConfig> providers = Arrays.asList(
//            new AuthUI.IdpConfig.FacebookBuilder().build());
//
//    // Create and launch sign-in intent
//    startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .build(),
//    RC_SIGN_IN);
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            if (resultCode == RESULT_OK) {
//                // Successfully signed in
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                // ...
//            } else {
//                // Sign in failed. If response is null the user canceled the
//                // sign-in flow using the back button. Otherwise check
//                // response.getError().getErrorCode() and handle the error.
//                // ...
//            }
//        }
//    }
}

