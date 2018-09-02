package com.example.slimshady.googlemap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.slimshady.googlemap.Api.WeatherAPI;
import com.example.slimshady.googlemap.Chart.MyXAxisValueFormatter;
import com.example.slimshady.googlemap.Model_Hourly.WeatherData;
import com.example.slimshady.googlemap.RecyclerView.DayAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;

public class DayForcastActivity extends AppCompatActivity {

    private LineChart chart;

    ArrayList<Entry> ynVals;
    ArrayList<Entry> ymVals;
    private PersianDate persianDate;
    WeatherAPI.ApiInterface api;
    private static final String TAG="DAYFORECASTACTIVITY";

    private ArrayList<String> hourTime=new ArrayList<>();
    private ArrayList<String> iconWeather=new ArrayList<>();
    private ArrayList<String> hourMinute=new ArrayList<>();
    private FloatingActionButton dayFab;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_forcast);
        chart = (LineChart) findViewById(R.id.lineChart_day);

        api = WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class);


        dayFab=(FloatingActionButton)findViewById(R.id.day_fab);
        dayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DayForcastActivity.this,"Comming Soon",Toast.LENGTH_SHORT).show();
            }
        });

        String units = "metric";
        String key = WeatherAPI.KEY;

        Bundle bundle=getIntent().getExtras();
        String latitude=bundle.getString("lat");
        String longintud=bundle.getString("lon");


        Call<WeatherData> retrofit = api.getDataForeCast(latitude, longintud, "45", units, key);
        retrofit.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {

                if (response.isSuccessful()){
                    Double[] forecastData = new Double[40];
                    String[] time = new String[40];
                   // String[] wdate = new String[50];



                    try {


                        for (int j = 0; j < 40; j++) {


                            forecastData[j] = response.body().getList().get(j).getMain().getTemp();
                            //Log.d(TAG, String.valueOf(forecastData[j]));
                            time[j] = String.valueOf(response.body().getList().get(j).getDt());
                            Log.d(TAG, time[j]);


                        }

                    }
                    catch (IndexOutOfBoundsException e ){
                        e.getMessage();
                    }
                    createChart(chart,forecastData,time);

                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }
        });


        Call<WeatherData> callForecast = api.getDataForeCast(latitude, longintud, "45", units, key);
        callForecast.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {

                if (response.isSuccessful()) {


                    String[] time = new String[50];

                    String[] icon=new String[50];

                   // String[] desc=new String[4];


                    try {
                        for (int j = 0; j < 50; j++) {


                            if(j %8==0&j!=0) {

                                icon[j] = response.body().getList().get(j).getWeather().get(0).getIcon();
                                time[j] = String.valueOf(response.body().getList().get(j).getDt());
                                //forecastData[j]= response.body().getList().get(j).getMain().getTemp();

                               // Log.d(TAG, String.valueOf(icon[j]));

                                long timestamp = Long.parseLong(String.valueOf(time[j]));
                                persianDate = new PersianDate(timestamp * 1000);

                               // Log.d(TAG, String.valueOf(forecastData[j]));



                                    iconWeather.addAll(Arrays.asList(icon[j]));
                                    hourTime.addAll(Arrays.asList(persianDate.dayName()));
                                    hourMinute.addAll(Arrays.asList(persianDate.getHour()+":00"));

                                initRecyclerView();


                            }


                            }


                    }  catch (IndexOutOfBoundsException e){
                        e.getMessage();
                    }

                   //  createChart(chart, forecastData,time);


                }


            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

                t.printStackTrace();
            }
        });

    }

    private void initRecyclerView(){

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recycler_day);
        recyclerView.setLayoutManager(linearLayoutManager);
        DayAdapter dayAdapter=new DayAdapter(this,iconWeather,hourTime,hourMinute);
        recyclerView.setAdapter(dayAdapter);

    }



    private void createChart(LineChart chart, Double[] forecastData, String[] time) {

        chart.setAutoScaleMinMaxEnabled(true);
        chart.getDescription().setText("");
        List<Entry> entries = new ArrayList<>();

        float x = 0f;


        entries.add(new Entry(0f,forecastData[7].floatValue()));
        entries.add(new Entry(1f,forecastData[15].floatValue()));
        entries.add(new Entry(2f,forecastData[23].floatValue()));
        entries.add(new Entry(3f,forecastData[31].floatValue()));





        LineDataSet dataSet = new LineDataSet(entries, "Temperature");

        String[] wdate = new String[32];

      // wdate= new String[]{time[7], time[15], time[23], time[31]};
        wdate= new String[]{"", "", "", ""};


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
       xAxis.setValueFormatter(new MyXAxisValueFormatter(wdate));
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setValueTextSize(15f);
        dataSet.setValueTextColor(Color.DKGRAY);



        dataSet.setColor(Color.rgb(213, 109, 98));

        dataSet.setFillColor( Color.rgb(213, 109, 98));
        dataSet.setFillAlpha(200);
        dataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return dataProvider.getYChartMin();
            }
        });

        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int) value + "°C";
            }
        });
        dataSet.setDrawFilled(true);


        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setTextSize(10f);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextColor(Color.BLACK);
        chart.getLegend().setEnabled(false);
        // chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.animateY(1000, Easing.EasingOption.EaseOutQuad);
        chart.invalidate();


    }
}
