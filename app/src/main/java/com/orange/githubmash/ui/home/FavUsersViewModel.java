package com.orange.githubmash.ui.home;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.orange.githubmash.data.Repositories.UserRepository;
import com.orange.githubmash.data.local.User;
import java.util.List;

public class FavUsersViewModel extends AndroidViewModel {
    private LiveData<List<User>> mList;
    private UserRepository mRepository;

    public FavUsersViewModel(Application application) {
        super(application);
        UserRepository userRepository = new UserRepository(application);
        this.mRepository = userRepository;
        this.mList = userRepository.getmyfavusers();
    }

    public LiveData<List<User>> getMyUsers() {
        return this.mList;
    }

    public void insert(User user) {
        this.mRepository.insert(user);
    }

    public void delete(User user) {
        this.mRepository.delete(user);
    }
}