package com.orange.githubmash.data.remote;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoteOwner implements Parcelable {
    public static final Creator<RemoteOwner> CREATOR = new Creator<RemoteOwner>() {
        public RemoteOwner createFromParcel(Parcel in) {
            return new RemoteOwner(in);
        }

        public RemoteOwner[] newArray(int size) {
            return new RemoteOwner[size];
        }
    };
    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;
    @SerializedName("html_url")
    @Expose
    private String htmlUrl;
    @SerializedName("login")
    @Expose
    private String login;

    protected RemoteOwner(Parcel in) {
        this.login = in.readString();
        this.avatarUrl = in.readString();
        this.htmlUrl = in.readString();
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login2) {
        this.login = login2;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl2) {
        this.avatarUrl = avatarUrl2;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl2) {
        this.htmlUrl = htmlUrl2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.login);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.htmlUrl);
    }
}