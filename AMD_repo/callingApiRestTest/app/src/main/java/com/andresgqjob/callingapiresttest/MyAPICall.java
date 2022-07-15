package com.andresgqjob.callingapiresttest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyAPICall
{
    // http://127.0.0.1:8080/api/v1/products/00YMuvxg3HbgneOVyGov/update
    // http://127.0.0.1:8080/api/v1/products/list

    @GET("list")
    Call<List<DataModel>> getProducts();
}

