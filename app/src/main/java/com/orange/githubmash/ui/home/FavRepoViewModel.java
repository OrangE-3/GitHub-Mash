package com.orange.githubmash.ui.home;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.orange.githubmash.data.Repositories.RepoRepository;
import com.orange.githubmash.data.local.GitHubRepository;
import java.util.List;

/* renamed from: com.orange.githubmash.ui.home.FavRepoViewModel */
public class FavRepoViewModel extends AndroidViewModel {
    private LiveData<List<GitHubRepository>> mList;
    private RepoRepository mRepository;

    public FavRepoViewModel(Application application) {
        super(application);
        RepoRepository repoRepository = new RepoRepository(application);
        this.mRepository = repoRepository;
        this.mList = repoRepository.getmyfavreps();
    }

    public LiveData<List<GitHubRepository>> getMyRepos() {
        return this.mList;
    }

    public void insert(GitHubRepository repository) {
        this.mRepository.insert(repository);
    }

    public void delete(GitHubRepository rep) {
        this.mRepository.delete(rep);
    }
}