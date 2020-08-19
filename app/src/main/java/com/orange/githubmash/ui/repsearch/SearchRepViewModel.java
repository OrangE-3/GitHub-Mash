package com.orange.githubmash.ui.repsearch;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.orange.githubmash.data.Repositories.RepoRepository;
import com.orange.githubmash.data.remote.RemoteRepoModel;

import java.util.List;

public class SearchRepViewModel extends AndroidViewModel
{
    private RepoRepository mRepository;
    private MutableLiveData<List<RemoteRepoModel> > myresults;

    public SearchRepViewModel(Application application) {
        super(application);
        this.mRepository = new RepoRepository(application);
    }
    LiveData<List<RemoteRepoModel> > searchrep(String query)
    {
        myresults=mRepository.repsearchresults(query);
        return myresults;
    }
}
