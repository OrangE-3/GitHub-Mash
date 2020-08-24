package com.orange.githubmash.ui.home;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.orange.githubmash.data.Repositories.OwnerRepository;
import com.orange.githubmash.data.local.LocalOwner;

import java.util.List;

public class FavOwnersViewModel extends AndroidViewModel {
    private LiveData<List<LocalOwner>> mList;
    private OwnerRepository mRepository;

    public FavOwnersViewModel(Application application) {
        super(application);
        OwnerRepository ownerRepository = new OwnerRepository(application);
        this.mRepository = ownerRepository;
        this.mList = ownerRepository.getmyfavusers();
    }

    public LiveData<List<LocalOwner>> getMyUsers() {
        return this.mList;
    }

    public void insert(LocalOwner localOwner) {
        this.mRepository.insert(localOwner);
    }

    public void delete(LocalOwner localOwner) {
        this.mRepository.delete(localOwner);
    }
}