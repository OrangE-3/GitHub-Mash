package com.orange.githubmash.data.local;


import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"entry_owner","name","owner"})
public class LocalGitRepoModel {

    private String description;
    @NonNull
    private String entry_owner;
    @NonNull
    private String name;
    @NonNull
    private String owner;
    private String url;
    private Integer watchers;

    public LocalGitRepoModel(String entry_owner, String owner, String name, String description, String url, Integer watchers) {
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


}