package com.orange.githubmash.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitHubService
{
    private String str;
    private Retrofit retrofit;

    public GitHubService(String BASE_URL) {
        this.str = BASE_URL;
        retrofit = new Retrofit.Builder()
                .baseUrl(str)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public GitHubClient getService()
    {
        return retrofit.create(GitHubClient.class);
    }

}
