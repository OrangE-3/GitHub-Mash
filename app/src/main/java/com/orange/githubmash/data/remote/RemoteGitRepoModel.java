package com.orange.githubmash.data.remote;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoteGitRepoModel implements Parcelable {
    public static final Creator<RemoteGitRepoModel> CREATOR = new Creator<RemoteGitRepoModel>() {
        public RemoteGitRepoModel createFromParcel(Parcel in) {
            return new RemoteGitRepoModel(in);
        }

        public RemoteGitRepoModel[] newArray(int size) {
            return new RemoteGitRepoModel[size];
        }
    };
    @SerializedName("description")
    @Expose
    private String mDescription;
    @SerializedName("name")
    @Expose
    private String mName;
    @SerializedName("html_url")
    @Expose
    private String mUrl;
    @SerializedName("watchers")
    @Expose
    private int mWatchers;
    @SerializedName("owner")
    @Expose
    private RemoteOwner owner;

    public RemoteGitRepoModel(Parcel in) {
        this.mDescription = in.readString();
        this.mName = in.readString();
        this.mWatchers = in.readInt();
        this.mUrl = in.readString();

    }

    public RemoteOwner getOwner() {
        return this.owner;
    }

    public String getmName() {
        return this.mName;
    }

    public String getmDescription() {
        return this.mDescription;
    }

    public String getmUrl() {
        return this.mUrl;
    }

    public int getmWatchers() {
        return this.mWatchers;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mDescription);
        parcel.writeString(this.mName);
        parcel.writeInt(this.mWatchers);
        parcel.writeString(this.mUrl);
    }
}