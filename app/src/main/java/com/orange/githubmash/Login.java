package com.orange.githubmash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.orange.githubmash.data.remote.AccessToken;
import com.orange.githubmash.data.remote.GitHubClient;
import com.orange.githubmash.data.remote.GitHubService;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.utils.fields.GlobalFields;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private ProgressBar progressBar;
    private Button Loginbutton;
    public static final String clientID="a0c341fa8833c1b5bb84";
    public static final String clientSecret="733a0fde0a6142a14642f6dcf1e3727dba72d8a5";
    public static final String callback="github-mash://getmycallback";
    private GitHubService gitHubWebService;
    private GitHubService gitHubApiService;
    private GitHubClient webclient;
    private GitHubClient apiclient;
    private FirebaseAnalytics firebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAnalytics=FirebaseAnalytics.getInstance(this);
        gitHubWebService=new GitHubService(GlobalFields.GitHubWebUrl);
        webclient=gitHubWebService.getService();
        gitHubApiService=new GitHubService(GlobalFields.GitHubApiUrl);
        apiclient=gitHubApiService.getService();
        setContentView(R.layout.activity_login);
        progressBar=findViewById(R.id.progressBar);
        Loginbutton = findViewById(R.id.login_button);
        progressBar.setVisibility(View.INVISIBLE);
    }


    public void LoginClick(View view) {
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalFields.GitHubWebUrl+"/login/oauth/authorize"+"?client_id="+clientID+"&scope=read:user%20repo%20user:follow"+"&redirect_uri="+callback));
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Uri uri= getIntent().getData();
        if(uri!=null && uri.toString().startsWith(callback))
        {
            String code=uri.getQueryParameter("code");
            if(code!=null) {

                Loginbutton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                Call<AccessToken> atc = webclient.getAccessToken(Login.clientID, Login.clientSecret, code);
                mPreferences = getSharedPreferences(GlobalFields.sharedPrefFile, MODE_PRIVATE);
                atc.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        AccessToken token = response.body();
                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                        preferencesEditor.putString("TOKEN_NAME", token.getAccesstoken());
                        preferencesEditor.putString("TOKEN_TYPE", token.getTokentype());
                        preferencesEditor.apply();

                        Call<RemoteOwner> calll = apiclient.userdata(token.getTokentype()+" "+token.getAccesstoken());
                        calll.enqueue(new Callback<RemoteOwner>() {
                            public void onResponse(Call<RemoteOwner> call, Response<RemoteOwner> response) {
                                if (response.body() != null) {
                                    RemoteOwner p = (RemoteOwner) response.body();
                                    SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                                    preferencesEditor.putString("USER_NAME", p.getLogin());
                                    preferencesEditor.putString("USER_URL", p.getHtmlUrl());
                                    preferencesEditor.putString("USER_AVATAR", p.getAvatarUrl());
                                    preferencesEditor.apply();
                                    Bundle bundle=new Bundle();
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,p.getLogin());
                                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN,bundle);
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            public void onFailure(Call<RemoteOwner> call, Throwable t) {
                                call.clone().enqueue(this);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t)
                    {
                        call.clone().enqueue(this);
                    }
                });

            }
        }
    }
}