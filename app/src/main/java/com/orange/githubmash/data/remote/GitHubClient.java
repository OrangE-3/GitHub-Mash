package com.orange.githubmash.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GitHubClient
{
    @GET("/user/repos")
    Call< List<RemoteGitRepoModel> > userreps(@Header("Authorization") String token);

    @GET("/user")
    Call< RemoteOwner > userdata(@Header("Authorization") String token);

    @GET("/search/users")
    Call<OwnerResponse> getUserList(@Query("q") String filter);

    @GET("/search/repositories")
    Call<RemoteGitRepoResponse> getrepList(@Query("q") String filter);


    @Headers("Accept: application/json")
    @POST("/login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken>getAccessToken(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("code") String code
    );
}
