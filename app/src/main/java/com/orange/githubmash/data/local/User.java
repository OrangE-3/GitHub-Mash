package com.orange.githubmash.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    /* renamed from: ID */
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String avatar_url;
    private String login;
    private String owner;
    private String url;

    public User(String owner,String login,String url,String avatar_url) {
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

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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