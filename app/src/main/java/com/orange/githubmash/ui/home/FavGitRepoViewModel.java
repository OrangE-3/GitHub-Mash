package com.orange.githubmash.ui.home;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.orange.githubmash.data.Repositories.GitRepoRepository;
import com.orange.githubmash.data.local.LocalGitRepoModel;

import java.util.List;

/* renamed from: com.orange.githubmash.ui.home.FavRepoViewModel */
public class FavGitRepoViewModel extends AndroidViewModel {
    private LiveData<List<LocalGitRepoModel>> mList;
    private GitRepoRepository mRepository;

    public FavGitRepoViewModel(Application application) {
        super(application);
        GitRepoRepository gitRepoRepository = new GitRepoRepository(application);
        this.mRepository = gitRepoRepository;
        this.mList = gitRepoRepository.getmyfavreps();
    }

    public LiveData<List<LocalGitRepoModel>> getMyRepos() {
        return this.mList;
    }

    public void insert(LocalGitRepoModel repository) {
        this.mRepository.insert(repository);
    }

    public void delete(LocalGitRepoModel rep) {
        this.mRepository.delete(rep);
    }
}