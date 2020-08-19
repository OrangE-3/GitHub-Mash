package com.orange.githubmash.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.orange.githubmash.ItemClickSupport;

import java.util.List;

@Dao
public interface RepoDao {

    @Query("DELETE from GitHubRepository")
    void deleteAll();

    @Delete
    void deleterep(GitHubRepository gitHubRepository);

    @Query("SELECT * from GitHubRepository")
    LiveData<List<GitHubRepository>> getAllRepos();

    @Query("SELECT * from GitHubRepository where entry_owner=(:str)")
    LiveData<List<GitHubRepository>> getmyReps(String str);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(GitHubRepository gitHubRepository);
}