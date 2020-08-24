package com.orange.githubmash.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocalOwnerDao {

    @Query("DELETE from LocalOwner")
    void deleteAll();

    @Delete
    void deleteuser(LocalOwner localOwner);

    @Query("SELECT * from LocalOwner")
    LiveData<List<LocalOwner>> getAllUsers();

    @Query("SELECT * from LocalOwner where owner=(:str)")
    LiveData<List<LocalOwner>> getmyUsers(String str);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LocalOwner localOwner);
}