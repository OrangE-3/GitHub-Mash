package com.orange.githubmash.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
    Call<RemoteGitRepoResponse> getrepList(@Query("q") String filter,@Query("per_page") int per_page,@Query("sort") String sort);

    @GET("/user/following")
    Call< List<RemoteOwner> > userfollowing(@Header("Authorization") String token);

    @GET("/user/starred")
    Call< List<RemoteGitRepoModel> > repfavs(@Header("Authorization") String token);

    @GET("/user/following")
    Call< List<RemoteOwner> > userfavs(@Header("Authorization") String token);


    @Headers("Accept: application/json")
    @POST("/login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken>getAccessToken(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("code") String code
    );

    @Headers({
            "Content-Length: 0",
            "Accept: application/json"
    })
    @PUT("/user/starred/{owner}/{repo}")
    Call<Void> starrepo(@Header("Authorization") String token, @Path("owner") String owner,@Path("repo") String repo);

    @DELETE("/user/starred/{owner}/{repo}")
    Call<Void> unstarrepo(@Header("Authorization") String token, @Path("owner") String owner,@Path("repo") String repo);

    @Headers({
            "Content-Length: 0",
            "Accept: application/json"
    })
    @PUT("/user/following/{username}")
    Call<Void> staruser(@Header("Authorization") String token, @Path("username") String username);

    @DELETE("/user/following/{username}")
    Call<Void> unstaruser(@Header("Authorization") String token, @Path("username") String username);

}
