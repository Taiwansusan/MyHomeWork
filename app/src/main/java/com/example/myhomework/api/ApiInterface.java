package com.example.myhomework.api;

import com.example.myhomework.model.UserData;
import com.example.myhomework.model.UserDataDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("users")
    Call<List<UserData>> getAllUser();
    @GET("users/{username}")
    Call<UserDataDetail> getUser(@Path("username") String userName);
}
