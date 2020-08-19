package com.orange.githubmash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.orange.githubmash.data.Repositories.RepoRepository;
import com.orange.githubmash.data.Repositories.UserRepository;
import com.orange.githubmash.data.local.User;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.data.remote.RemoteRepoModel;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel
{
    private UserRepository mRepository;
    private MutableLiveData<RemoteOwner> myself;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = new UserRepository(application);
        myself = mRepository.getmyself();
    }
    LiveData<RemoteOwner> getme() {
        return myself;
    }


}
