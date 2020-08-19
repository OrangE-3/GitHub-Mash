package com.orange.githubmash.ui.usersearch;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.orange.githubmash.data.Repositories.RepoRepository;
import com.orange.githubmash.data.Repositories.UserRepository;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.data.remote.RemoteRepoModel;

import java.util.List;

public class SearchUsersViewModel extends AndroidViewModel
{
    private UserRepository mRepository;
    private MutableLiveData<List<RemoteOwner> > myresults;

    public SearchUsersViewModel(Application application) {
        super(application);
        this.mRepository = new UserRepository(application);
    }
    LiveData<List<RemoteOwner> > searchusers(String query)
    {
        myresults=mRepository.usersearchresults(query);
        return myresults;
    }
}