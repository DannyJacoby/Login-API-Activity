package com.example.loginactivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderAPI {

    @GET("posts")
    Call<List<Post>> getAllPosts();

    @GET("comments?postId={userId}")
    Call<List<Post>> getPostsByUserId(int userId);

}
