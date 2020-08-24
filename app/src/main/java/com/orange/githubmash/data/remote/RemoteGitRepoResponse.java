package com.orange.githubmash.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RemoteGitRepoResponse
{
    @SerializedName("items")
    private List<RemoteGitRepoModel> items;
    public List<RemoteGitRepoModel> getItems()
    {
        return items;
    }
}
