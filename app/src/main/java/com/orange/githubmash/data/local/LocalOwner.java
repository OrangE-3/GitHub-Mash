package com.orange.githubmash.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"owner","login"})
public class LocalOwner {

    private String avatar_url;
    @NonNull
    private String login;
    @NonNull
    private String owner;
    private String url;

    public LocalOwner(String owner, String login, String url, String avatar_url) {
        this.avatar_url = avatar_url;
        this.login = login;
        this.owner = owner;
        this.url = url;
    }

    public String getLogin() {
        return this.login;
    }

    public String getUrl() {
        return this.url;
    }

    public String getAvatar_url() {
        return this.avatar_url;
    }

    public String getOwner() {
        return this.owner;
    }


    public void setOwner(String owner2) {
        this.owner = owner2;
    }

    public void setLogin(String login2) {
        this.login = login2;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public void setAvatar_url(String avatar_url2) {
        this.avatar_url = avatar_url2;
    }
}