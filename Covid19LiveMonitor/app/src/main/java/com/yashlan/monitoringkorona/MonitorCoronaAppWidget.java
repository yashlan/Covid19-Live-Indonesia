package com.yashlan.monitoringkorona;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.NoCopySpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.BreakIterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementation of App Widget functionality.
 */
public class MonitorCoronaAppWidget extends AppWidgetProvider {

    private static final String Refresh_button = "refresh";
    RemoteViews remoteViews;
    boolean isFetched = false;
    //RelativeLayout relativeLayout;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (Refresh_button.equals(intent.getAction())){

            try{
                FetchData(context);

                if(isFetched){
                    final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                    final ComponentName cn = new ComponentName(context, MonitorCoronaAppWidget.class);
                    mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.tv_dirawat);
                    mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.tv_sembuh);
                    mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.tv_positif);
                    mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.tv_meniggal);
                    mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.tv_region);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


            toast("Refreshing Data", context);
        }

    }

    private void toast(String t, Context context){

        final Toast myToast;
        int toastDurationInMilliSeconds = 1000;
        myToast = Toast.makeText(context, t , Toast.LENGTH_SHORT);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                myToast.show();
            }
            public void onFinish() {
                myToast.cancel();
            }
        };
        // Show the toast and starts the countdown
        myToast.show();
        toastCountDown.start();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        try{
            final int count = appWidgetIds.length;

            for (int i = 0; i < count; i++) {
                int widgetId = appWidgetIds[i];

                FetchData(context);

                if(isFetched){

                    Intent intent = new Intent(context, MonitorCoronaAppWidget.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    remoteViews.setOnClickPendingIntent(R.id.btn_refresh, getPendingSelfIntent(context, Refresh_button));

                    try {
                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }

                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void FetchData(Context context){

        isFetched = false;

/*        relativeLayout = (RelativeLayout)

        relativeLayout.setVisibility(View.VISIBLE);*/

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.monitor_corona_app_widget);


        remoteViews.setTextViewText(R.id.tv_region, null);
        remoteViews.setTextViewText(R.id.tv_dirawat, null);
        remoteViews.setTextViewText(R.id.tv_meniggal, null);
        remoteViews.setTextViewText(R.id.tv_positif, null);
        remoteViews.setTextViewText(R.id.tv_sembuh, null);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<DataRegion>> call = api.getDataIndonesia();

        RemoteViews finalRemoteViews = remoteViews;

        call.enqueue(new Callback<List<DataRegion>>() {
            @Override
            public void onResponse(Call<List<DataRegion>> call, Response<List<DataRegion>> response) {

                List<DataRegion> dataRegionList =  response.body();

                for(DataRegion d : dataRegionList){

                    finalRemoteViews.setTextViewText(R.id.tv_region, d.getRegionName());
                    finalRemoteViews.setTextViewText(R.id.tv_dirawat, d.getDirawat());
                    finalRemoteViews.setTextViewText(R.id.tv_meniggal, d.getMeninggal());
                    finalRemoteViews.setTextViewText(R.id.tv_positif, d.getPositif());
                    finalRemoteViews.setTextViewText(R.id.tv_sembuh, d.getSembuh());

                    isFetched = true;

                }

/*                if(call.isExecuted() || call.isCanceled()){
                    relativeLayout.setVisibility(View.GONE);
                }*/

            }

            @Override
            public void onFailure(Call<List<DataRegion>> call, Throwable t) {

/*
                relativeLayout.setVisibility(View.GONE);
*/
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("ERROR", "onFailure: " + t.getMessage());
            }
        });
    }
}