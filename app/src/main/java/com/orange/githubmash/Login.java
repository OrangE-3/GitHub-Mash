package com.orange.githubmash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.orange.githubmash.data.remote.AccessToken;
import com.orange.githubmash.data.remote.GitHubClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    public static String clientID="a0c341fa8833c1b5bb84";
    public static String clientSecret="733a0fde0a6142a14642f6dcf1e3727dba72d8a5";
    public static String callback="github-mash://getmycallback";
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.hellosharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void LoginClick(View view) {
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login/oauth/authorize"+"?client_id="+clientID+"&scope=read:user%20repo"+"&redirect_uri="+callback));
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Uri uri= getIntent().getData();
        if(uri!=null && uri.toString().startsWith(callback))
        {
            String code=uri.getQueryParameter("code");


            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://github.com")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            GitHubClient client=retrofit.create(GitHubClient.class);

            Call<AccessToken> atc= client.getAccessToken(Login.clientID,Login.clientSecret,code);

            atc.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    AccessToken token=response.body();
                    mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
                    SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                    preferencesEditor.putString("TOKEN_NAME", token.getAccesstoken());
                    preferencesEditor.putString("TOKEN_TYPE", token.getTokentype());
                    preferencesEditor.apply();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                }
            });


        }
    }
}