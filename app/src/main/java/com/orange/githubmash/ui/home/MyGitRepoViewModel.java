package com.orange.githubmash.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.orange.githubmash.data.local.LocalGitRepoModel;
import com.orange.githubmash.data.Repositories.GitRepoRepository;
import com.orange.githubmash.data.remote.RemoteGitRepoModel;

import java.util.List;

public class MyGitRepoViewModel extends AndroidViewModel {

    private GitRepoRepository mRepository;
    private LiveData<List<LocalGitRepoModel> > mList;
    private LiveData<List<RemoteGitRepoModel> > mRemoteList;

    public MyGitRepoViewModel(Application application) {
        super(application);
        mRepository = new GitRepoRepository(application);
        this.mList = mRepository.getmyownrepslocally();
    }
    public void insert(LocalGitRepoModel repository) {
        this.mRepository.insert(repository);
    }
    public LiveData<List<LocalGitRepoModel> > getMyReposfromlocal() {
        return this.mList;
    }
    public LiveData<List<RemoteGitRepoModel> > getMyRepsremote() {
        this.mRemoteList=mRepository.getMyRepsremote();
        return mRemoteList;
    }

}