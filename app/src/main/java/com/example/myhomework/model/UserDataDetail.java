package com.example.myhomework.model;

import com.google.gson.annotations.SerializedName;

public class UserDataDetail extends UserData{
    @SerializedName("name")
    public String name;

    @SerializedName("company")
    public String company;

    @SerializedName("blog")
    public String blog;

    @SerializedName("location")
    public String location;

    @SerializedName("email")
    public String email;

    @SerializedName("hireable")
    public boolean hireable;

    @SerializedName("bio")
    public String bio;

    @SerializedName("twitter_username")
    public String twitter_username;

    @SerializedName("public_repos")
    public String public_repos;

    @SerializedName("public_gists")
    public String public_gists;

    @SerializedName("followers")
    public int followers;

    @SerializedName("following")
    public int following;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("updated_at")
    public String updated_at;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHireable() {
        return hireable;
    }

    public void setHireable(boolean hireable) {
        this.hireable = hireable;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTwitter_username() {
        return twitter_username;
    }

    public void setTwitter_username(String twitter_username) {
        this.twitter_username = twitter_username;
    }

    public String getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(String public_repos) {
        this.public_repos = public_repos;
    }

    public String getPublic_gists() {
        return public_gists;
    }

    public void setPublic_gists(String public_gists) {
        this.public_gists = public_gists;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
