package com.yashlan.monitoringkorona;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {

    TextView tv_provinsi;
    TextView tv_positif;
    TextView tv_sembuh;
    TextView tv_meninggal;

    RelativeLayout relativeLayout;

    AppCompatButton btn_refresh;
    AppCompatButton btn_submit;
    AppCompatButton btn_back;

    Spinner spinner;

    ArrayList<String> province_list;

    private void InitializeComponent(){

        tv_provinsi = (TextView) findViewById(R.id.tv_provinsi);
        tv_positif = (TextView) findViewById(R.id.tv_positif);
        tv_sembuh = (TextView) findViewById(R.id.tv_sembuh);
        tv_meninggal = (TextView) findViewById(R.id.tv_meniggal);

        btn_refresh = (AppCompatButton) findViewById(R.id.btn_refresh);
        btn_submit = (AppCompatButton) findViewById(R.id.btnSubmit);
        btn_back = (AppCompatButton) findViewById(R.id.btn_back);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        spinner = (Spinner) findViewById(R.id.spinner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        try{

            Objects.requireNonNull(getSupportActionBar()).hide();

            InitializeComponent();

            province_list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Provinsi_arrays)));

            Collections.sort(province_list);

            province_list.add(0, "Pilih Wilayah/Provinsi");

            final ArrayAdapter<String> spinnerProvinceArrayAdapter = new ArrayAdapter<String>(this,
                    R.layout.spinner_province_item, province_list){

                @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {

                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };

            spinnerProvinceArrayAdapter.setDropDownViewResource(R.layout.spinner_province_item);
            spinner.setAdapter(spinnerProvinceArrayAdapter);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        btn_submit.setOnClickListener(v -> {
            FetchDataProvinsi();
        });

        btn_refresh.setOnClickListener(v -> {
            FetchDataProvinsi();
        });

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });


    }

    private void FetchDataProvinsi(){


        try{
            relativeLayout.setVisibility(View.VISIBLE);

            tv_provinsi.setText(null);
            tv_positif.setText(null);
            tv_sembuh.setText(null);
            tv_meninggal.setText(null);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);

            Call<List<DataProvinsi>> call = api.getDataProvince();

            call.enqueue(new Callback<List<DataProvinsi>>() {
                @Override
                public void onResponse(Call<List<DataProvinsi>> call, Response<List<DataProvinsi>> response) {

                    List<DataProvinsi> dataProvinsi =  response.body();

                    for(DataProvinsi d : dataProvinsi){

                        if(d.getDataProvinsiList().getProvinsi().contains(spinner.getSelectedItem().toString())){

                            DataProvinsi.Attributes attributes = d.getDataProvinsiList();

                            tv_provinsi.setText(spinner.getSelectedItem().toString());
                            tv_positif.setText(attributes.getKasus_positif());
                            tv_sembuh.setText(attributes.getKasus_sembuh());
                            tv_meninggal.setText(attributes.getKasus_meninggal());


                            Log.d("QWEQWEQWEQWEQWE : " , String.valueOf(attributes) + "\n");
                        }


                        //Log.d("DATA PROVINSI : " , d.getDataProvinsiList().getProvinsi() + "\n");

                    }

                    if(call.isExecuted() || call.isCanceled()){
                        relativeLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<DataProvinsi>> call, Throwable t) {

                    relativeLayout.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "ERROR : " + t.getMessage() + " Silakan Coba Lagi", Toast.LENGTH_LONG).show();
                    Log.d("ERROR", "onFailure: " + t.getMessage());
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}