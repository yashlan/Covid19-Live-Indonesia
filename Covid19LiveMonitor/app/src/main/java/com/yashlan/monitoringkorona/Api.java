package com.yashlan.monitoringkorona;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "https://api.kawalcorona.com/";

    @GET("indonesia")
    Call<List<DataRegion>> getData();

}
