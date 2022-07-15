package com.dev.mrvazguen.indexproductorum.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCli
{
    private static final String BASE_URL = "http://192.168.99.166:8080/api/v1/products/";
    private static Retrofit retrofit = null;
    // Retrofit builder

    public static ApiInterface getRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}
