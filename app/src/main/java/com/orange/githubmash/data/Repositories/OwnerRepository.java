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

import java.security.PrivateKey;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class OwnerRepository {
    private LiveData<List<LocalOwner>> favusers;
    public SharedPreferences mPreferences;
    public MutableLiveData<List<RemoteOwner>> searchresults = new MutableLiveData<>(null);
    private LocalOwnerDao localOwnerDao;
    private GitHubService gitHubService;
    private GitHubClient client;
    private String entry_owner;

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

    public OwnerRepository(Application application) {
        gitHubService=new GitHubService(GlobalFields.GitHubApiUrl);
        client=gitHubService.getService();
        mPreferences= application.getSharedPreferences(GlobalFields.sharedPrefFile, 0);
        entry_owner = mPreferences.getString("USER_NAME", "");
        LocalOwnerDao localOwnerDao2 = LocalDatabase.getDatabase(application).userDao();
        this.localOwnerDao = localOwnerDao2;
        this.favusers = localOwnerDao2.getmyUsers(entry_owner);
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

    public MutableLiveData<List<RemoteOwner>> usersearchresults(String query) {
        searchusers(query);
        return this.searchresults;
    }

    public LiveData<List<LocalOwner>> getmyfavusers() {
        return this.favusers;
    }

    public void insert(LocalOwner localOwner) {
        new inserttask(this.localOwnerDao).execute(new LocalOwner[]{localOwner});
    }

    public void delete(LocalOwner localOwner) {
        new deletetask(this.localOwnerDao).execute(new LocalOwner[]{localOwner});
    }
}