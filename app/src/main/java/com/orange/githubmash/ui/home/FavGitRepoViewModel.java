package com.orange.githubmash.ui.home;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.orange.githubmash.data.Repositories.GitRepoRepository;
import com.orange.githubmash.data.local.LocalGitRepoModel;
import com.orange.githubmash.data.remote.RemoteGitRepoModel;

import java.util.List;

/* renamed from: com.orange.githubmash.ui.home.FavRepoViewModel */
public class FavGitRepoViewModel extends AndroidViewModel {

    private LiveData<List<LocalGitRepoModel>> mList;
    private GitRepoRepository mRepository;
    private LiveData<List<RemoteGitRepoModel> > mRemoteList;
    public FavGitRepoViewModel(Application application) {
        super(application);
        GitRepoRepository gitRepoRepository = new GitRepoRepository(application);
        this.mRepository = gitRepoRepository;
        this.mList = gitRepoRepository.getmyfavreps();
    }

    public LiveData<List<LocalGitRepoModel>> getMyRepos() {
        return this.mList;
    }
    public LiveData<List<RemoteGitRepoModel>> getFavRepsremote(){
        mRemoteList=mRepository.getFavRepsremote();
        return mRemoteList;
    }
    public void insert(LocalGitRepoModel repository) {
        mRepository.favinsertHelper(repository);
        this.mRepository.insert(repository);
    }

    public void delete(LocalGitRepoModel rep) {
        mRepository.favdeleteHelper(rep);
        this.mRepository.delete(rep);
    }
    public void deleteAll() {
        this.mRepository.deleteallfavs();
    }
}