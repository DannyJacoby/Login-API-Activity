package com.example.loginactivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    private int userId;
    @SerializedName("id")
    private int postId;
    private String title;
    @SerializedName("body")
    private String text;

    public Post(int userId, int postId, String title, String body) {
        this.userId = userId;
        this.postId = postId;
        this.title = title;
        this.text = body;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return text;
    }
    public void setBody(String body) {
        this.text = body;
    }
}
