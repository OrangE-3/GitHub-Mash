package com.orange.githubmash.data.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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
    private LiveData<List<GitHubRepository> > favreps;
    private SharedPreferences mPreferences;
    /* access modifiers changed from: private */
    private MutableLiveData<List<RemoteRepoModel> > myreps = new MutableLiveData<>(null);
    private LiveData<List<GitHubRepository> > myrepslocal;
    private RepoDao repoDao;
    /* access modifiers changed from: private */
    private MutableLiveData<List<RemoteRepoModel> > searchresults = new MutableLiveData<>(null);
    private String sharedPrefFile = "com.example.android.hellosharedprefs";
    Context context;
    public LiveData<List<RemoteRepoModel> >getMyRepsremote()
    {
        Myrepshelper(context);
        return myreps;
    }
    public LiveData<List<GitHubRepository> > getmyownrepslocally()
    {
        return myrepslocal;
    }

    private static class deletetask extends AsyncTask<GitHubRepository, Void, Void> {
        private RepoDao mTaskDao;

        deletetask(RepoDao dao) {
            mTaskDao = dao;
        }

        /* access modifiers changed from: protected */
        protected Void doInBackground(GitHubRepository... reps) {
            mTaskDao.deleterep(reps[0]);
            return null;
        }
    }

    private static class inserttask extends AsyncTask<GitHubRepository, Void, Void> {
        private RepoDao mTaskDao;

        inserttask(RepoDao dao) {
            mTaskDao = dao;
        }

        /* access modifiers changed from: protected */
        protected Void doInBackground(GitHubRepository... gitHubRepositories) {
            mTaskDao.insert(gitHubRepositories[0]);
            return null;
        }
    }

    public RepoRepository(Application application)
    {
        context=application;
        SharedPreferences sharedPreferences = application.getSharedPreferences(this.sharedPrefFile,Context.MODE_PRIVATE);
        this.mPreferences = sharedPreferences;
        String entry_owner = sharedPreferences.getString("USER_NAME", "");
        LocalDatabase db = LocalDatabase.getDatabase(application);
        repoDao =db.repoDao();
        this.favreps = repoDao.getmyReps(entry_owner);
        this.myrepslocal=repoDao.getmyReps(entry_owner+"#@local");
    }

    private void Myrepshelper(final Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(this.sharedPrefFile, 0);
        this.mPreferences = sharedPreferences;
        String token = sharedPreferences.getString("TOKEN_NAME", "");
        String tokentype = this.mPreferences.getString("TOKEN_TYPE", "");
        GitHubClient client = (GitHubClient) new Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitHubClient.class);
        Call<List<RemoteRepoModel>> atc = client.userreps(tokentype+" "+token);
        atc.enqueue(new Callback<List<RemoteRepoModel>>() {

            public void onResponse(Call<List<RemoteRepoModel>> call, Response<List<RemoteRepoModel>> response) {
                List<RemoteRepoModel> l = response.body();
                myreps.postValue(l);
            }

            public void onFailure(Call<List<RemoteRepoModel>> call, Throwable t) {
            }
        });
    }

    private void searchreps(String query) {
        String b=query+" in:name";
        query=b;
        ((GitHubClient) new Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create()).build().create(GitHubClient.class)).getrepList(query).enqueue(new Callback<RepositoryResponse>() {

            public void onResponse(Call<RepositoryResponse> call, Response<RepositoryResponse> response) {
                if (response.body() != null) {
                    RepoRepository.this.searchresults.postValue(((RepositoryResponse) response.body()).getItems());
                }
            }

            public void onFailure(Call<RepositoryResponse> call, Throwable t) {
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
        new inserttask(this.repoDao).execute(repository);
    }

    public void delete(GitHubRepository rep) {
        new deletetask(this.repoDao).execute(rep);
    }
}