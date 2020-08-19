package com.orange.githubmash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import static com.orange.githubmash.R.string.tname;

public class Splash extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.hellosharedprefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String token = mPreferences.getString("TOKEN_NAME", "");

        if(token=="")
            startActivity(new Intent(Splash.this, Login.class));
        else
            startActivity(new Intent(Splash.this, MainActivity.class));
        finish();
    }
}