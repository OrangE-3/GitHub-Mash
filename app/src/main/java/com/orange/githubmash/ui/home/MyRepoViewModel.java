package com.orange.githubmash.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.orange.githubmash.data.local.GitHubRepository;
import com.orange.githubmash.data.Repositories.RepoRepository;
import com.orange.githubmash.data.remote.RemoteRepoModel;

import java.util.List;

public class MyRepoViewModel extends AndroidViewModel {

    private RepoRepository mRepository;
    private MutableLiveData<List<RemoteRepoModel> > mMyReps;

    public MyRepoViewModel (Application application) {
        super(application);
        mRepository = new RepoRepository(application);
        mMyReps = mRepository.getMyreps();
    }
    LiveData<List<RemoteRepoModel> > getMyRepos() {
        return mMyReps;
    }
}