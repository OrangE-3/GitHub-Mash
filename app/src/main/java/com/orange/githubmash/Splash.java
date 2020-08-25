package com.orange.githubmash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.orange.githubmash.utils.fields.GlobalFields;


public class Splash extends AppCompatActivity {
    private SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = getSharedPreferences(GlobalFields.sharedPrefFile, MODE_PRIVATE);
        String token = mPreferences.getString("TOKEN_NAME", "");

        if(token=="")
            startActivity(new Intent(Splash.this, Login.class));
        else
            startActivity(new Intent(Splash.this, MainActivity.class));
        finish();
    }
}