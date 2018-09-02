package com.example.slimshady.googlemap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(!isLocationEnabled(this)){
            Toast.makeText(this,"قبل از ورود به برنامه از روشن بودن "+"GPS"+" گوشی خود اطمینان حاصل کنید",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        };


        pieChart=(PieChart)findViewById(R.id.half_pie);
        pieChart.setMaxAngle(180);

        pieChart.setHoleColor(Color.TRANSPARENT);
        //pieChart.setDrawHoleEnabled(true);
       // pieChart.setHoleRadius(180);
        pieChart.setTransparentCircleRadius(10f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.getDescription().setEnabled(false);

        pieChart.setClickable(true);
        pieChart.setTouchEnabled(true);
        // pieChart.setDescription(null);
        pieChart.getLegend().setEnabled(false);
        pieChart.setRotationAngle(180);
        pieChart.animateY(1200, Easing.EasingOption.EaseInOutElastic);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pieChart.setElevation(5f);
        }


        displayChart();

        setChartData(4,100);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                if (e == null)
                    return;

                if(e.getY()==35.0){
                    Intent about=new Intent(HomeActivity.this,AboutActivity.class);
                    startActivity(about);


                }
                if(e.getY()==30.0){

                    Intent weather=new Intent(HomeActivity.this,WeatherActivity.class);
                    startActivity(weather);

                }
                if(e.getY()==21.0){
                    Intent help=new Intent(HomeActivity.this,HelpActivity.class);
                    startActivity(help);
                }


            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        boolean isAvailable = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            isAvailable = (locationMode != Settings.Secure.LOCATION_MODE_OFF);
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            isAvailable = !TextUtils.isEmpty(locationProviders);
        }

        boolean coarsePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        boolean finePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        return isAvailable && (coarsePermissionCheck || finePermissionCheck);


    }
    private void setChartData(int count, int range){

        ArrayList<PieEntry> chartArrayList=new ArrayList<>();

        chartArrayList.add(new PieEntry(35f,"درباره ما"));
        chartArrayList.add(new PieEntry(30f,"هوا + نقشه"));
        chartArrayList.add(new PieEntry(21f,"راهنما"));
        PieDataSet pieDataSet=new PieDataSet(chartArrayList,"");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);

        PieData pieData=new PieData(pieDataSet);
        pieData.setValueTextSize(0);
        pieChart.setData(pieData);

        pieChart.invalidate();

    }

    private void displayChart(){


        Display display=getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height=displayMetrics.heightPixels;

        int offset=(int)(height*0.5);

        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) pieChart.getLayoutParams();
        params.setMargins(0,0,0,-offset);
        pieChart.setLayoutParams(params);


    }
}
