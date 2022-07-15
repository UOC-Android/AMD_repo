package com.dev.mrvazguen.indexproductorum.utils;

import com.dev.mrvazguen.indexproductorum.data.model.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface
{
    // http://127.0.0.1:8080/api/v1/products/00YMuvxg3HbgneOVyGov/update
    // http://127.0.0.1:8080/api/v1/products/list

    @GET("list")
    Call<List<Producto>> getProducts();
}
