package com.example.slimshady.googlemap.Api;

/**
 * Created by slim shady on 04/25/2018.
 */

public class HelperCommon {

    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";


    public static ApiService getGoogleApiService(){

        return RetrofitClient.getClient(GOOGLE_API_URL).create(ApiService.class);
    }

}
