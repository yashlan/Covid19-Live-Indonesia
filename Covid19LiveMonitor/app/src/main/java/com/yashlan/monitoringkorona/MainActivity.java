package com.yashlan.monitoringkorona;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView tv_region;
    TextView tv_positif;
    TextView tv_sembuh;
    TextView tv_meninggal;
    TextView tv_dirawat;

    RelativeLayout relativeLayout;

    AppCompatButton btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        tv_region = (TextView) findViewById(R.id.tv_region);
        tv_positif = (TextView) findViewById(R.id.tv_positif);
        tv_sembuh = (TextView) findViewById(R.id.tv_sembuh);
        tv_meninggal = (TextView) findViewById(R.id.tv_meniggal);
        tv_dirawat = (TextView) findViewById(R.id.tv_dirawat);

        btn_refresh = (AppCompatButton) findViewById(R.id.btn_refresh);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        FetchData();

        btn_refresh.setOnClickListener(v -> {

            FetchData();

        });


    }

    private void FetchData(){

        relativeLayout.setVisibility(View.VISIBLE);

        tv_region.setText(null);
        tv_positif.setText(null);
        tv_sembuh.setText(null);
        tv_meninggal.setText(null);
        tv_dirawat.setText(null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<DataRegion>> call = api.getData();

        call.enqueue(new Callback<List<DataRegion>>() {
            @Override
            public void onResponse(Call<List<DataRegion>> call, Response<List<DataRegion>> response) {

                List<DataRegion> dataRegionList =  response.body();

                for(DataRegion d : dataRegionList){

                    tv_region.setText(d.getRegionName());
                    tv_positif.setText(d.getPositif());
                    tv_sembuh.setText(d.getSembuh());
                    tv_meninggal.setText(d.getMeninggal());
                    tv_dirawat.setText(d.getDirawat());

                }

                if(call.isExecuted() || call.isCanceled()){
                    relativeLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<DataRegion>> call, Throwable t) {

                relativeLayout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("ERROR", "onFailure: " + t.getMessage());
            }
        });
    }
}