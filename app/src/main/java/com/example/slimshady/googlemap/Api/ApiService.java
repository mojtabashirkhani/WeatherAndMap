package com.example.slimshady.googlemap.Api;

import com.example.slimshady.googlemap.Model.MyPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by slim shady on 04/25/2018.
 */

public interface ApiService {

    @GET
    Call<MyPojo> getNearbyPlaces(@Url String url);

}
