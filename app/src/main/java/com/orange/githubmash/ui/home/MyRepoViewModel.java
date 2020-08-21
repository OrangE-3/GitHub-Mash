package com.orange.githubmash.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.orange.githubmash.data.local.GitHubRepository;
import com.orange.githubmash.data.Repositories.RepoRepository;
import com.orange.githubmash.data.remote.RemoteRepoModel;

import java.util.List;

public class MyRepoViewModel extends AndroidViewModel {

    private RepoRepository mRepository;
    private LiveData<List<GitHubRepository> > mList;
    private LiveData<List<RemoteRepoModel> > mRemoteList;

    public MyRepoViewModel (Application application) {
        super(application);
        mRepository = new RepoRepository(application);
        this.mList = mRepository.getmyownrepslocally();
    }
    public void insert(GitHubRepository repository) {
        this.mRepository.insert(repository);
    }
    public LiveData<List<GitHubRepository> > getMyReposfromlocal() {
        return this.mList;
    }
    public LiveData<List<RemoteRepoModel> > getMyRepsremote() {
        this.mRemoteList=mRepository.getMyRepsremote();
        return mRemoteList;
    }

}