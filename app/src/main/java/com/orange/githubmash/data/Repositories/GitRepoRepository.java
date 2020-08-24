package com.orange.githubmash.data.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.orange.githubmash.data.local.LocalGitRepoDao;
import com.orange.githubmash.data.local.LocalGitRepoModel;
import com.orange.githubmash.data.local.LocalDatabase;
import com.orange.githubmash.data.remote.GitHubClient;
import com.orange.githubmash.data.remote.GitHubService;
import com.orange.githubmash.data.remote.RemoteGitRepoModel;
import com.orange.githubmash.data.remote.RemoteGitRepoResponse;
import com.orange.githubmash.utils.fields.GlobalFields;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitRepoRepository {
    private LiveData<List<LocalGitRepoModel> > favreps;
    private SharedPreferences mPreferences;
    private MutableLiveData<List<RemoteGitRepoModel> > myreps = new MutableLiveData<>(null);
    private LiveData<List<LocalGitRepoModel> > myrepslocal;
    private LocalGitRepoDao localGitRepoDao;
    private GitHubService gitHubService;
    private String entry_owner;
    private GitHubClient client;
    private MutableLiveData<List<RemoteGitRepoModel> > searchresults = new MutableLiveData<>(null);
    public LiveData<List<RemoteGitRepoModel> >getMyRepsremote()
    {
        Myrepshelper();
        return myreps;
    }
    public LiveData<List<LocalGitRepoModel> > getmyownrepslocally()
    {
        return myrepslocal;
    }

    private static class deletetask extends AsyncTask<LocalGitRepoModel, Void, Void> {
        private LocalGitRepoDao mTaskDao;

        deletetask(LocalGitRepoDao dao) {
            mTaskDao = dao;
        }

        /* access modifiers changed from: protected */
        protected Void doInBackground(LocalGitRepoModel... reps) {
            mTaskDao.deleterep(reps[0]);
            return null;
        }
    }

    private static class inserttask extends AsyncTask<LocalGitRepoModel, Void, Void> {
        private LocalGitRepoDao mTaskDao;

        inserttask(LocalGitRepoDao dao) {
            mTaskDao = dao;
        }

        /* access modifiers changed from: protected */
        protected Void doInBackground(LocalGitRepoModel... gitHubRepositories) {
            mTaskDao.insert(gitHubRepositories[0]);
            return null;
        }
    }

    public GitRepoRepository(Application application)
    {
        gitHubService=new GitHubService(GlobalFields.GitHubApiUrl);
        client=gitHubService.getService();
        mPreferences = application.getSharedPreferences(GlobalFields.sharedPrefFile,Context.MODE_PRIVATE);
        entry_owner = mPreferences.getString("USER_NAME", "");
        LocalDatabase db = LocalDatabase.getDatabase(application);
        localGitRepoDao =db.repoDao();
        this.favreps = localGitRepoDao.getmyReps(entry_owner);
        this.myrepslocal= localGitRepoDao.getmyReps(entry_owner+"#@local");
    }

    private void Myrepshelper() {
        String token = mPreferences.getString("TOKEN_NAME", "");
        String tokentype = this.mPreferences.getString("TOKEN_TYPE", "");
        Call<List<RemoteGitRepoModel>> atc = client.userreps(tokentype+" "+token);
        atc.enqueue(new Callback<List<RemoteGitRepoModel>>() {

            public void onResponse(Call<List<RemoteGitRepoModel>> call, Response<List<RemoteGitRepoModel>> response) {
                List<RemoteGitRepoModel> l = response.body();
                myreps.postValue(l);
            }

            public void onFailure(Call<List<RemoteGitRepoModel>> call, Throwable t) {
            }
        });
    }

    private void searchreps(String query) {
        String b=query+" in:name";
        query=b;
        client.getrepList(query).enqueue(new Callback<RemoteGitRepoResponse>() {
            public void onResponse(Call<RemoteGitRepoResponse> call, Response<RemoteGitRepoResponse> response) {
                if (response.body() != null) {
                    GitRepoRepository.this.searchresults.postValue(((RemoteGitRepoResponse) response.body()).getItems());
                }
            }

            public void onFailure(Call<RemoteGitRepoResponse> call, Throwable t) {
            }
        });
    }

    public MutableLiveData<List<RemoteGitRepoModel>> repsearchresults(String query) {
        searchreps(query);
        return this.searchresults;
    }

    public LiveData<List<LocalGitRepoModel>> getmyfavreps() {
        return this.favreps;
    }

    public void insert(LocalGitRepoModel repository) {
        new inserttask(this.localGitRepoDao).execute(repository);
    }

    public void delete(LocalGitRepoModel rep) {
        new deletetask(this.localGitRepoDao).execute(rep);
    }
}