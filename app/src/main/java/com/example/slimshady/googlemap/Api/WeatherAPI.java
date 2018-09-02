package com.example.slimshady.googlemap.Api;


import com.example.slimshady.googlemap.Model_Hourly.WeatherData;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;





public class WeatherAPI {


        public static String KEY = "206de2de8ed1083ed8447ad281031489";
        public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
        private static Retrofit retrofit = null;

        public interface ApiInterface {



            @GET("forecast")
            Call<WeatherData> getDataForeCast(
                    @Query("lat") String lat,
                    @Query("lon") String lon,
                    @Query("cnt") String cnt,
                    @Query("units") String units,
                    @Query("appid") String appid
            );



        }

    public static Retrofit getClient() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }
