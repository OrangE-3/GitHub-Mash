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

public class GitRepoRepository {
    private LiveData<List<LocalGitRepoModel> > favreps;
    private MutableLiveData<List<RemoteGitRepoModel> > favrepsremote=new MutableLiveData<>(null);
    private MutableLiveData<List<RemoteGitRepoModel> > searchresults = new MutableLiveData<>(null);
    private MutableLiveData<List<RemoteGitRepoModel> > myreps = new MutableLiveData<>(null);
    private LiveData<List<LocalGitRepoModel> > myrepslocal;
    private SharedPreferences mPreferences;
    private LocalGitRepoDao localGitRepoDao;
    private GitHubService gitHubService;
    private static String entry_owner;
    private GitHubClient client;
    private String token;
    private String tokentype;

    public GitRepoRepository(Application application)
    {
        gitHubService=new GitHubService(GlobalFields.GitHubApiUrl);
        client=gitHubService.getService();
        mPreferences = application.getSharedPreferences(GlobalFields.sharedPrefFile,Context.MODE_PRIVATE);
        entry_owner = mPreferences.getString("USER_NAME", "");
        token = mPreferences.getString("TOKEN_NAME", "");
        tokentype=mPreferences.getString("TOKEN_TYPE", "");
        LocalDatabase db = LocalDatabase.getDatabase(application);
        localGitRepoDao =db.repoDao();
        this.favreps = localGitRepoDao.getmyReps(entry_owner+GlobalFields.localfav);
        this.myrepslocal= localGitRepoDao.getmyReps(entry_owner+GlobalFields.localmin);
    }


    public LiveData<List<LocalGitRepoModel> > getmyownrepslocally()
    {
        return myrepslocal;
    }


    public LiveData<List<LocalGitRepoModel>> getmyfavreps() {
        return this.favreps;
    }


    public void insert(LocalGitRepoModel repository) {
        new inserttask(this.localGitRepoDao).execute(repository);
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


    public void delete(LocalGitRepoModel rep) {
        new deletetask(this.localGitRepoDao).execute(rep);
    }
    private static class deletetask extends AsyncTask<LocalGitRepoModel, Void, Void> {
        private LocalGitRepoDao mTaskDao;

        deletetask(LocalGitRepoDao dao) {
            mTaskDao = dao;
        }

        /* access modifiers changed from: protected */
        protected Void doInBackground(LocalGitRepoModel... reps) {
            mTaskDao.deleterep(reps[0].getEntry_owner(),reps[0].getName(),reps[0].getOwner());
            return null;
        }
    }


    public void deleteallmine() {
        new deleteallminetask(this.localGitRepoDao).execute();
    }
    private static class deleteallminetask extends AsyncTask<Void,Void,Void>
    {
        private LocalGitRepoDao mTaskDao;

        deleteallminetask(LocalGitRepoDao dao) {
            mTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTaskDao.deletereps(entry_owner+GlobalFields.localmin);
            return null;
        }
    }


    public void deleteallfavs()
    {
        new deleteallfavstask(this.localGitRepoDao).execute();
    }
    private static class deleteallfavstask extends AsyncTask<Void,Void,Void>
    {
        private LocalGitRepoDao mTaskDao;

        deleteallfavstask(LocalGitRepoDao dao) {
            mTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTaskDao.deletereps(entry_owner+GlobalFields.localfav);
            return null;
        }
    }


    public LiveData<List<RemoteGitRepoModel> >getMyRepsremote()
    {
        myreps=new MutableLiveData<>(null);
        Myrepshelper();
        return myreps;
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


    public LiveData<List<RemoteGitRepoModel>> getFavRepsremote() {
        favrepsremote=new MutableLiveData<>(null);
        Favrepshelper();
        return favrepsremote;
    }
    private void Favrepshelper() {
        Call<List<RemoteGitRepoModel>> atc = client.repfavs(tokentype+" "+token);
        atc.enqueue(new Callback<List<RemoteGitRepoModel>>() {

            public void onResponse(Call<List<RemoteGitRepoModel>> call, Response<List<RemoteGitRepoModel>> response) {
                List<RemoteGitRepoModel> l = response.body();
                favrepsremote.postValue(l);
            }

            public void onFailure(Call<List<RemoteGitRepoModel>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }


    public MutableLiveData<List<RemoteGitRepoModel>> repsearchresults(String query) {
        searchreps(query);
        return this.searchresults;
    }
    private void searchreps(String query) {
        String b=query+" in:name";
        query=b;
        client.getrepList(query,50,"stars").enqueue(new Callback<RemoteGitRepoResponse>() {
            public void onResponse(Call<RemoteGitRepoResponse> call, Response<RemoteGitRepoResponse> response) {
                if (response.body() != null) {
                    GitRepoRepository.this.searchresults.postValue(((RemoteGitRepoResponse) response.body()).getItems());
                }
            }

            public void onFailure(Call<RemoteGitRepoResponse> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }


    public void favinsertHelper(LocalGitRepoModel repository)
    {
        client.starrepo(tokentype+" "+token,repository.getOwner(),repository.getName()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }


    public void favdeleteHelper(LocalGitRepoModel repository)
    {
        client.unstarrepo(tokentype+" "+token,repository.getOwner(),repository.getName()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }

}