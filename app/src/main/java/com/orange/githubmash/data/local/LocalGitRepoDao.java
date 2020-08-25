package com.orange.githubmash.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocalGitRepoDao {

    @Query("DELETE from LocalGitRepoModel")
    void deleteAll();

    @Query("DELETE from LocalGitRepoModel where entry_owner=(:str)")
    void deletereps(String str);


    @Query("DELETE from LocalGitRepoModel where entry_owner=(:str) AND name=(:name) AND owner=(:owner)")
    void deleterep(String str,String name,String owner);


    @Query("SELECT * from LocalGitRepoModel")
    LiveData<List<LocalGitRepoModel>> getAllRepos();

    @Query("SELECT * from LocalGitRepoModel where entry_owner=(:str)")
    LiveData<List<LocalGitRepoModel>> getmyReps(String str);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LocalGitRepoModel localGitRepoModel);

}