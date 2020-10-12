package com.dmos5.github_dmos5.api;

import com.dmos5.github_dmos5.model.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("users/{user}/repos")
    Call<List<Repository>> getRepository(@Path("user") String user);
}