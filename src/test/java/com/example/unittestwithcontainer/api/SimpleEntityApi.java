package com.example.unittestwithcontainer.api;

import java.util.List;

import com.example.unittestwithcontainer.dto.SimpleEntity;
import com.example.unittestwithcontainer.request.SimpleEntityCreateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SimpleEntityApi {
    @GET("simple-entity")
    Call<List<SimpleEntity>> simpleEntityList();

    @POST("simple-entity")
    Call<SimpleEntity> saveSimpleEntity(@Body SimpleEntityCreateRequest request);
}
