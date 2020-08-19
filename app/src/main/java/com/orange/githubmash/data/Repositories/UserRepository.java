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
    /* access modifiers changed from: private */
    public SharedPreferences mPreferences;
    /* access modifiers changed from: private */
    public MutableLiveData<RemoteOwner> myself = new MutableLiveData<>(null);
    /* access modifiers changed from: private */
    public MutableLiveData<List<RemoteOwner>> searchresults = new MutableLiveData<>(null);
    private String sharedPrefFile = "com.example.android.hellosharedprefs";
    private UserDao userDao;

    private static class deletetask extends AsyncTask<User, Void, Void> {
        private UserDao mTaskDao;

        deletetask(UserDao dao) {
            this.mTaskDao = dao;
        }

        /* access modifiers changed from: protected */
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
        myuserdata(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(this.sharedPrefFile, 0);
        this.mPreferences = sharedPreferences;
        String entry_owner = sharedPreferences.getString("USER_NAME", "");
        UserDao userDao2 = LocalDatabase.getDatabase(application).userDao();
        this.userDao = userDao2;
        this.favusers = userDao2.getmyUsers(entry_owner);
    }

    private void myuserdata(Application application) {
        SharedPreferences sharedPreferences = application.getSharedPreferences(this.sharedPrefFile, 0);
        this.mPreferences = sharedPreferences;
        String str = "";
        String token = sharedPreferences.getString("TOKEN_NAME", str);
        String tokentype = this.mPreferences.getString("TOKEN_TYPE", str);
        GitHubClient client = (GitHubClient) new Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create()).build().create(GitHubClient.class);
        StringBuilder sb = new StringBuilder();
        sb.append(tokentype);
        sb.append(" ");
        sb.append(token);
        client.userdata(sb.toString()).enqueue(new Callback<RemoteOwner>() {
            public void onResponse(Call<RemoteOwner> call, Response<RemoteOwner> response) {
                if (response.body() != null) {
                    RemoteOwner p = (RemoteOwner) response.body();
                    UserRepository.this.myself.postValue(p);
                    Editor preferencesEditor = UserRepository.this.mPreferences.edit();
                    preferencesEditor.putString("USER_NAME", p.getLogin());
                    preferencesEditor.apply();
                }
            }

            public void onFailure(Call<RemoteOwner> call, Throwable t) {
            }
        });
    }

    public MutableLiveData<RemoteOwner> getmyself() {
        return this.myself;
    }

    private void searchusers(String query) {
        ((GitHubClient) new Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create()).build().create(GitHubClient.class)).getUserList(query).enqueue(new Callback<OwnerResponse>() {
            public void onResponse(Call<OwnerResponse> call, Response<OwnerResponse> response) {
                if (response.body() != null) {
                    List<RemoteOwner> p = ((OwnerResponse) response.body()).getItems();
                    Log.i("fasfa", String.valueOf(p.size()));
                    UserRepository.this.searchresults.postValue(p);
                }
            }

            public void onFailure(Call<OwnerResponse> call, Throwable t) {
                Log.d("Size", String.valueOf(t));
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