package com.orange.githubmash.data.local;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"entry_owner","name","owner"})
public class GitHubRepository {

    private String description;
    @NonNull
    private String entry_owner;
    @NonNull
    private String name;
    @NonNull
    private String owner;
    private String url;
    private Integer watchers;

    public GitHubRepository(String entry_owner, String owner, String name, String description, String url, Integer watchers) {
        this.description = description;
        this.entry_owner = entry_owner;
        this.name = name;
        this.owner = owner;
        this.url = url;
        this.watchers = watchers;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getWatchers() {
        return this.watchers;
    }

    public String getUrl() {
        return this.url;
    }

    public String getEntry_owner() {
        return this.entry_owner;
    }


    public void setEntry_owner(String entry_owner2) {
        this.entry_owner = entry_owner2;
    }

    public void setOwner(String owner2) {
        this.owner = owner2;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public void setWatchers(Integer watchers2) {
        this.watchers = watchers2;
    }

}