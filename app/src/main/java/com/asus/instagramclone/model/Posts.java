package com.asus.instagramclone.model;

public class Posts {

    public String userEmail;
    public String comment;
    public String downloadUrl;

    public Posts(String userEmail, String comment, String downloadUrl) {
        this.userEmail = userEmail;
        this.comment = comment;
        this.downloadUrl = downloadUrl;
    }
}
