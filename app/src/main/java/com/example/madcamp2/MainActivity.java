package com.example.madcamp2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private Context mContext;

    private LoginButton btn_facebook_login;

    private LoginCallback mLoginCallback = new LoginCallback();

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = (accessToken != null) && !accessToken.isExpired();

        if(!isLoggedIn){
            mContext = getApplicationContext();

            mCallbackManager = CallbackManager.Factory.create();

            btn_facebook_login = (LoginButton) findViewById(R.id.login_button);

            btn_facebook_login.setReadPermissions(Arrays.asList("public_profile", "email"));

            btn_facebook_login.registerCallback(mCallbackManager, mLoginCallback);
        } else {
            Intent intent1 = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Intent intent1 = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent1);
        }
    }
}
