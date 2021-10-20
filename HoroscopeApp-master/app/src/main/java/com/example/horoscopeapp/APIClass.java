package com.example.horoscopeapp;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import com.example.horoscopeapp.HoroscopeClass;

public interface APIClass {

    String endPoint = "https://aztro.sameerkumar.website";

    @POST("/")
    Call<HoroscopeClass> horoscope(@Query("sign") String sign, @Query("day") String day);

}
