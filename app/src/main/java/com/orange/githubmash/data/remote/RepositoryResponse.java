package com.orange.githubmash.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepositoryResponse
{
    @SerializedName("items")
    private List<RemoteRepoModel> items;
    public List<RemoteRepoModel> getItems()
    {
        return items;
    }
}
