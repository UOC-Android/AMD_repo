package com.andresgqjob.callingapiresttest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.andresgqjob.callingapiresttest.databinding.ActivityMainBinding;

import java.text.MessageFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getProducts();
    }

    private void getProducts() {
        // Retrofit builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.99.166:8080/api/v1/products/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Instance for interface
        MyAPICall myAPICall = retrofit.create(MyAPICall.class);

        // Call API
        Call<List<DataModel>> call = myAPICall.getProducts();

        // Execute call
        call.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<DataModel>> call,
                                   @NonNull retrofit2.Response<List<DataModel>> response) {
                if (response.isSuccessful()) {
                    List<DataModel> dataModels = response.body();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (DataModel dataModel : dataModels) {
                        stringBuilder.append("PID: ").append(dataModel.getPid()).append("\n");
                        stringBuilder.append("Nombre: ").append(dataModel.getName()).append("\n");
                        stringBuilder.append("Categoría: ").append(dataModel.getCategory()).append("\n");
                        stringBuilder.append("Supermercado: ").append(dataModel.getSupermarket()).append("\n");
                        stringBuilder.append("Descripcion: ").append(dataModel.getDescription()).append("\n");
                        stringBuilder.append("PVP: ").append(dataModel.getPrice()).append("\n");
                        stringBuilder.append("Ref PVP: ").append(dataModel.getRefPrice()).append("\n");
                        stringBuilder.append("Ref Ud: ").append(dataModel.getRefUnit()).append("\n");
                        stringBuilder.append("Fecha: ").append(dataModel.getInsertDate()).append("\n");
                        stringBuilder.append("URL: ").append(dataModel.getUrl()).append("\n").append("\n");
                    }
                    binding.jsonTxt.setText(stringBuilder.toString());
                } else {
                    binding.jsonTxt.setText(MessageFormat.format("Código: {0}", response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DataModel>> call, @NonNull Throwable t) {
                binding.jsonTxt.setText(t.getMessage());
            }
        });
    }
}