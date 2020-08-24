package com.orange.githubmash.data.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OwnerResponse
{
    @SerializedName("items")
    @Expose
    private List<RemoteOwner> items = null;

    public List<RemoteOwner> getItems() {
        return items;
    }

    public void setItems(List<RemoteOwner> items) {
        this.items = items;
    }
}
