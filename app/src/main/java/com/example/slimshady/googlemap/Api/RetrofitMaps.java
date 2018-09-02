package com.example.slimshady.googlemap.Api;

import com.example.slimshady.googlemap.POJO.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by navneet on 17/7/16.
 */
public interface RetrofitMaps {

    /*
     * This method will return the details from Google Direction API
     */
@GET("api/directions/json?key=AIzaSyCBXys1aHyQGv_ganXKnJ33OHu8euQvOdM")
    Call<Example> getDetailsFromDirectionAPI(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode, @Query("alternatives") String bool);
}
