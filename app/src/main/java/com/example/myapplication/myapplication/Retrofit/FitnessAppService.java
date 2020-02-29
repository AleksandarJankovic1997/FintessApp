package com.example.myapplication.myapplication.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FitnessAppService {

    @POST("post")
    Call<Informacija> posaljiInformaciju(@Body Informacija informacija);

    @GET("get")
    Call <List<Informacija>> traziInformacije();
}
