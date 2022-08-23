package com.ananas.shrink.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ShrinkApi {
    
    @GET("api")
    Call<Response> shrinkUrl(
            @Query("key") String key,
            @Query("url") String url
    );
    
}
