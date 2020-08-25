package com.orange.githubmash.data.Repositories;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.orange.githubmash.data.local.LocalDatabase;
import com.orange.githubmash.data.local.LocalOwner;
import com.orange.githubmash.data.local.LocalOwnerDao;
import com.orange.githubmash.data.remote.GitHubClient;
import com.orange.githubmash.data.remote.GitHubService;
import com.orange.githubmash.data.remote.OwnerResponse;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.utils.fields.GlobalFields;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerRepository {
    private LiveData<List<LocalOwner>> favusers;
    private MutableLiveData<List<RemoteOwner>> favownersremote = new MutableLiveData<>(null);
    public SharedPreferences mPreferences;
    public MutableLiveData<List<RemoteOwner>> searchresults = new MutableLiveData<>(null);
    private LocalOwnerDao localOwnerDao;
    private GitHubService gitHubService;
    private GitHubClient client;
    private String entry_owner;
    private String token;
    private String tokentype;

    public OwnerRepository(Application application) {
        gitHubService = new GitHubService(GlobalFields.GitHubApiUrl);
        client = gitHubService.getService();
        mPreferences = application.getSharedPreferences(GlobalFields.sharedPrefFile, 0);
        entry_owner = mPreferences.getString("USER_NAME", "");
        token = mPreferences.getString("TOKEN_NAME", "");
        tokentype = mPreferences.getString("TOKEN_TYPE", "");
        LocalOwnerDao localOwnerDao2 = LocalDatabase.getDatabase(application).userDao();
        this.localOwnerDao = localOwnerDao2;
        this.favusers = localOwnerDao2.getmyUsers(entry_owner);
    }


    public LiveData<List<LocalOwner>> getmyfavusers() {
        return this.favusers;
    }


    public void insert(LocalOwner localOwner) {
        new inserttask(this.localOwnerDao).execute(new LocalOwner[]{localOwner});
    }
    private static class inserttask extends AsyncTask<LocalOwner, Void, Void> {
        private LocalOwnerDao mTaskDao;

        inserttask(LocalOwnerDao dao) {
            this.mTaskDao = dao;
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(LocalOwner... localOwners) {
            this.mTaskDao.insert(localOwners[0]);
            return null;
        }
    }


    public void delete(LocalOwner localOwner) {
        new deletetask(this.localOwnerDao).execute(new LocalOwner[]{localOwner});
    }
    private static class deletetask extends AsyncTask<LocalOwner, Void, Void> {
        private LocalOwnerDao mTaskDao;

        deletetask(LocalOwnerDao dao) {
            this.mTaskDao = dao;
        }

        public Void doInBackground(LocalOwner... localOwners) {
            this.mTaskDao.deleteuser(localOwners[0]);
            return null;
        }
    }


    public void deleteallfavs() {
        new deleteallfavstask(this.localOwnerDao).execute();
    }
    private static class deleteallfavstask extends AsyncTask<Void, Void, Void> {
        private LocalOwnerDao mTaskDao;

        deleteallfavstask(LocalOwnerDao dao) {
            mTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTaskDao.deleteAll();
            return null;
        }
    }


    public LiveData<List<RemoteOwner>> getFavOwnersRemote() {
        favownersremote = new MutableLiveData<>(null);
        Favownershelper();
        return favownersremote;
    }
    private void Favownershelper() {
        Call<List<RemoteOwner>> atc = client.userfavs(tokentype + " " + token);
        atc.enqueue(new Callback<List<RemoteOwner>>() {

            public void onResponse(Call<List<RemoteOwner>> call, Response<List<RemoteOwner>> response) {
                List<RemoteOwner> l = response.body();
                favownersremote.postValue(l);
            }

            public void onFailure(Call<List<RemoteOwner>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }


    public MutableLiveData<List<RemoteOwner>> usersearchresults(String query) {
        searchusers(query);
        return this.searchresults;
    }
    private void searchusers(String query) {
        client.getUserList(query).enqueue(new Callback<OwnerResponse>() {
            public void onResponse(Call<OwnerResponse> call, Response<OwnerResponse> response) {
                if (response.body() != null) {
                    List<RemoteOwner> p = ((OwnerResponse) response.body()).getItems();
                    OwnerRepository.this.searchresults.postValue(p);
                }
            }

            public void onFailure(Call<OwnerResponse> call, Throwable t) {
            }
        });
    }


    public void favinsertHelper(LocalOwner localOwner) {
        client.staruser(tokentype + " " + token, localOwner.getLogin()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }


    public void favdeleteHelper(LocalOwner localOwner) {
        client.unstaruser(tokentype + " " + token, localOwner.getLogin()).enqueue(new Callback<Void>() {
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