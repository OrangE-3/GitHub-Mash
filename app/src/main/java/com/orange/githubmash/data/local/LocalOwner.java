package com.orange.githubmash.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"owner","login"})
public class LocalOwner {

    private int id;
    private String avatar_url;
    @NonNull
    private String login;
    @NonNull
    private String owner;
    private String url;

    public LocalOwner(String owner, String login, String url, String avatar_url,int id) {
        this.avatar_url = avatar_url;
        this.login = login;
        this.owner = owner;
        this.url = url;
        this.id=id;
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

    public int getId() {
        return id;
    }
    public void setUrl(String url2) {
        this.url = url2;
    }

    public void setAvatar_url(String avatar_url2) {
        this.avatar_url = avatar_url2;
    }
}