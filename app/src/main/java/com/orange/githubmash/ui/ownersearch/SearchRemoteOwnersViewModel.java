package com.orange.githubmash.ui.ownersearch;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.orange.githubmash.data.Repositories.OwnerRepository;
import com.orange.githubmash.data.remote.RemoteOwner;

import java.util.List;

public class SearchRemoteOwnersViewModel extends AndroidViewModel
{
    private OwnerRepository mRepository;
    private MutableLiveData<List<RemoteOwner> > myresults;

    public SearchRemoteOwnersViewModel(Application application) {
        super(application);
        this.mRepository = new OwnerRepository(application);
    }
    LiveData<List<RemoteOwner> > searchusers(String query)
    {
        myresults=mRepository.usersearchresults(query);
        return myresults;
    }
}