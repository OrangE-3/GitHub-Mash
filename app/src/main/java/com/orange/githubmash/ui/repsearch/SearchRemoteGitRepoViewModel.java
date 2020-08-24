package com.orange.githubmash.ui.repsearch;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.orange.githubmash.data.Repositories.GitRepoRepository;
import com.orange.githubmash.data.remote.RemoteGitRepoModel;

import java.util.List;

public class SearchRemoteGitRepoViewModel extends AndroidViewModel
{
    private GitRepoRepository mRepository;
    private MutableLiveData<List<RemoteGitRepoModel> > myresults;

    public SearchRemoteGitRepoViewModel(Application application) {
        super(application);
        this.mRepository = new GitRepoRepository(application);
    }
    LiveData<List<RemoteGitRepoModel> > searchrep(String query)
    {
        myresults=mRepository.repsearchresults(query);
        return myresults;
    }
}
