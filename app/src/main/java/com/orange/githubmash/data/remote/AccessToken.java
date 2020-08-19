package com.orange.githubmash.data.remote;

import com.google.gson.annotations.SerializedName;

public class AccessToken
{
    @SerializedName("access_token")
    private String accesstoken;

    @SerializedName("token_type")
    private String tokentype;

    public String getAccesstoken() {
        return accesstoken;
    }

    public String getTokentype() {
        return tokentype;
    }
}
