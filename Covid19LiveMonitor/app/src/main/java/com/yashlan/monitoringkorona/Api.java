package com.yashlan.monitoringkorona;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "https://api.kawalcorona.com/";

    @GET("indonesia")
    Call<List<DataRegion>> getDataIndonesia();

    @GET("indonesia/provinsi")
    Call<List<DataProvinsi>> getDataProvince();

}
