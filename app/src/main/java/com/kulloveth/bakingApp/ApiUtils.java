package com.kulloveth.bakingApp;

import com.kulloveth.bakingApp.retrofit.BakingApiServiceInterface;
import com.kulloveth.bakingApp.retrofit.RetrofitClient;

public class ApiUtils {

    private static final String BASE_API_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static BakingApiServiceInterface getBakingApiServiceInterface() {
        return RetrofitClient.getRetrofitClient(BASE_API_URL).create(BakingApiServiceInterface.class);
    }
}
