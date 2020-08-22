package com.orange.githubmash;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.orange.githubmash.data.Repositories.RepoRepository;
import com.orange.githubmash.data.Repositories.UserRepository;
import com.orange.githubmash.data.local.User;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.data.remote.RemoteRepoModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MainActivityViewModel extends AndroidViewModel
{
    private MutableLiveData<String> myname=new MutableLiveData<>(null);
    private MutableLiveData<String> myurl=new MutableLiveData<>(null);
    private MutableLiveData<Bitmap> myimage=new MutableLiveData<>(null);
    private SharedPreferences mPreferences;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(MainActivity.sharedPrefFile, MODE_PRIVATE);
        this.mPreferences = sharedPreferences;
        myname.setValue(mPreferences.getString("USER_NAME",""));
        myurl.setValue(mPreferences.getString("USER_URL",""));
        Picasso.get().load(mPreferences.getString("USER_AVATAR", null)).resize(100,100).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                myimage.postValue(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });


    }
    LiveData<String> getname()
    {
        return myname;
    }
    LiveData<String> geturl()
    {
        return myurl;
    }
    LiveData<Bitmap> getimage()
    {
        return myimage;
    }

}
