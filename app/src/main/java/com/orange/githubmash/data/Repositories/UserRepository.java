package com.orange.githubmash.data.Repositories;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.orange.githubmash.data.local.LocalDatabase;
import com.orange.githubmash.data.local.User;
import com.orange.githubmash.data.local.UserDao;
import com.orange.githubmash.data.remote.GitHubClient;
import com.orange.githubmash.data.remote.OwnerResponse;
import com.orange.githubmash.data.remote.RemoteOwner;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository {
    private LiveData<List<User>> favusers;
    public SharedPreferences mPreferences;
    public MutableLiveData<List<RemoteOwner>> searchresults = new MutableLiveData<>(null);
    private String sharedPrefFile = "com.example.android.hellosharedprefs";
    private UserDao userDao;

    private static class deletetask extends AsyncTask<User, Void, Void> {
        private UserDao mTaskDao;

        deletetask(UserDao dao) {
            this.mTaskDao = dao;
        }

        public Void doInBackground(User... users) {
            this.mTaskDao.deleteuser(users[0]);
            return null;
        }
    }

    private static class inserttask extends AsyncTask<User, Void, Void> {
        private UserDao mTaskDao;

        inserttask(UserDao dao) {
            this.mTaskDao = dao;
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(User... users) {
            this.mTaskDao.insert(users[0]);
            return null;
        }
    }

    public UserRepository(Application application) {
        SharedPreferences sharedPreferences = application.getSharedPreferences(this.sharedPrefFile, 0);
        this.mPreferences = sharedPreferences;
        String entry_owner = sharedPreferences.getString("USER_NAME", "");
        UserDao userDao2 = LocalDatabase.getDatabase(application).userDao();
        this.userDao = userDao2;
        this.favusers = userDao2.getmyUsers(entry_owner);
    }



    private void searchusers(String query) {
        ((GitHubClient) new Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create()).build().create(GitHubClient.class)).getUserList(query).enqueue(new Callback<OwnerResponse>() {
            public void onResponse(Call<OwnerResponse> call, Response<OwnerResponse> response) {
                if (response.body() != null) {
                    List<RemoteOwner> p = ((OwnerResponse) response.body()).getItems();
                    UserRepository.this.searchresults.postValue(p);
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

    public LiveData<List<User>> getmyfavusers() {
        return this.favusers;
    }

    public void insert(User user) {
        new inserttask(this.userDao).execute(new User[]{user});
    }

    public void delete(User user) {
        new deletetask(this.userDao).execute(new User[]{user});
    }
}