package com.orange.githubmash.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("DELETE from User")
    void deleteAll();

    @Delete
    void deleteuser(User user);

    @Query("SELECT * from User")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * from User where owner=(:str)")
    LiveData<List<User>> getmyUsers(String str);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);
}