package com.example.slimshady.googlemap.Api;


import com.example.slimshady.googlemap.Model_Current_Weather.OpenWeatherMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;





public interface CurrentWeatherApi {

    public static String API_KEY = "206de2de8ed1083ed8447ad281031489";
    public static String API_LINK = "http://api.openweathermap.org/data/2.5/";
    public static String metric="metric";

    @GET("weather")
    Call<OpenWeatherMap> getCurrentWeather(@Query("lat") String latitude,
                                           @Query("lon") String longitude,
                                           @Query("units") String metric,
                                           @Query("appid") String appid)
                                           ;





}
