package com.orange.githubmash.data.Repositories;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.orange.githubmash.data.local.GitHubRepository;
import com.orange.githubmash.data.local.LocalDatabase;
import com.orange.githubmash.data.local.RepoDao;
import com.orange.githubmash.data.remote.GitHubClient;
import com.orange.githubmash.data.remote.RemoteRepoModel;
import com.orange.githubmash.data.remote.RepositoryResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepoRepository {
    private LiveData<List<GitHubRepository>> favreps;
    private SharedPreferences mPreferences;
    /* access modifiers changed from: private */
    public MutableLiveData<List<RemoteRepoModel>> myreps = new MutableLiveData<>(null);
    private RepoDao repoDao;
    /* access modifiers changed from: private */
    public MutableLiveData<List<RemoteRepoModel>> searchresults = new MutableLiveData<>(null);
    private String sharedPrefFile = "com.example.android.hellosharedprefs";

    private static class deletetask extends AsyncTask<GitHubRepository, Void, Void> {
        private RepoDao mTaskDao;

        deletetask(RepoDao dao) {
            this.mTaskDao = dao;
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(GitHubRepository... reps) {
            this.mTaskDao.deleterep(reps[0]);
            return null;
        }
    }

    private static class inserttask extends AsyncTask<GitHubRepository, Void, Void> {
        private RepoDao mTaskDao;

        inserttask(RepoDao dao) {
            this.mTaskDao = dao;
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(GitHubRepository... gitHubRepositories) {
            this.mTaskDao.insert(gitHubRepositories[0]);
            return null;
        }
    }

    public RepoRepository(Application application) {
        Myrepshelper(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(this.sharedPrefFile, 0);
        this.mPreferences = sharedPreferences;
        String entry_owner = sharedPreferences.getString("USER_NAME", "");
        RepoDao repoDao2 = LocalDatabase.getDatabase(application).repoDao();
        this.repoDao = repoDao2;
        this.favreps = repoDao2.getmyReps(entry_owner);
    }

    public MutableLiveData<List<RemoteRepoModel>> getMyreps() {
        return this.myreps;
    }

    private void Myrepshelper(Application application) {
        SharedPreferences sharedPreferences = application.getSharedPreferences(this.sharedPrefFile, 0);
        this.mPreferences = sharedPreferences;
        String str = "";
        String token = sharedPreferences.getString("TOKEN_NAME", str);
        String tokentype = this.mPreferences.getString("TOKEN_TYPE", str);
        GitHubClient client = (GitHubClient) new Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create()).build().create(GitHubClient.class);
        StringBuilder sb = new StringBuilder();
        sb.append(tokentype);
        String str2 = " ";
        sb.append(str2);
        sb.append(token);
        Call<List<RemoteRepoModel>> atc = client.userreps(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(tokentype);
        sb2.append(str2);
        sb2.append(token);
        Log.i("TOKEN", sb2.toString());
        atc.enqueue(new Callback<List<RemoteRepoModel>>() {

            public void onResponse(Call<List<RemoteRepoModel>> call, Response<List<RemoteRepoModel>> response) {
                List<RemoteRepoModel> l = (List) response.body();
                RepoRepository.this.myreps.postValue(l);
                Log.d("Size", String.valueOf(l.size()));
            }

            public void onFailure(Call<List<RemoteRepoModel>> call, Throwable t) {
                Log.d("Size", String.valueOf(t));
            }
        });
    }

    private void searchreps(String query) {
        ((GitHubClient) new Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create()).build().create(GitHubClient.class)).getrepList(query).enqueue(new Callback<RepositoryResponse>() {

            public void onResponse(Call<RepositoryResponse> call, Response<RepositoryResponse> response) {
                if (response.body() != null) {
                    RepoRepository.this.searchresults.postValue(((RepositoryResponse) response.body()).getItems());
                }
            }

            public void onFailure(Call<RepositoryResponse> call, Throwable t) {
                Log.d("Size", String.valueOf(t));
            }
        });
    }

    public MutableLiveData<List<RemoteRepoModel>> repsearchresults(String query) {
        searchreps(query);
        return this.searchresults;
    }

    public LiveData<List<GitHubRepository>> getmyfavreps() {
        return this.favreps;
    }

    public void insert(GitHubRepository repository) {
        new inserttask(this.repoDao).execute(new GitHubRepository[]{repository});
    }

    public void delete(GitHubRepository rep) {
        new deletetask(this.repoDao).execute(new GitHubRepository[]{rep});
    }
}