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
import com.example.slimshady.googlemap.RecyclerView.HourlyAdapter;
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

public class HourlyForeCastActivity extends AppCompatActivity {
    public static String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private String metric = "metric";
    public static String API_KEY = "206de2de8ed1083ed8447ad281031489";
    String TAG = "HOURLY";

    WeatherAPI.ApiInterface api;
    PersianDate expiry = null;
    private ArrayList<String> hourTime=new ArrayList<>();
    private ArrayList<String> iconWeather=new ArrayList<>();
    private FloatingActionButton hourFab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fore_cast);


        api = WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class);


        hourFab=(FloatingActionButton)findViewById(R.id.hour_fab);
        hourFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HourlyForeCastActivity.this,"Comming Soon",Toast.LENGTH_SHORT).show();
                          }
        });

        String units = "metric";
        String key = WeatherAPI.KEY;

        Bundle bundle=getIntent().getExtras();
        String latitude=bundle.getString("lat");
        String longintud=bundle.getString("lon");




        Call<WeatherData> callForecast = api.getDataForeCast(latitude, longintud, "10", units, key);
        callForecast.enqueue(new Callback<WeatherData>() {
            final LineChart chart = (LineChart) findViewById(R.id.lineChart);




            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {

                    Double[] forecastData = new Double[7];
                    String[] time = new String[7];
                    String[] date = new String[7];

                    String[] dates = new String[7];
                    String[] desc=new String[7];
                    String[] icon=new String[7];
                    for (int j = 0; j < 7; j++) {

                        forecastData[j] = Double.valueOf(Math.round(response.body().getList().get(j).getMain().getTemp()));

                        icon[j]=response.body().getList().get(j).getWeather().get(0).getIcon();
                        time[j] = String.valueOf(response.body().getList().get(j).getDt());

                        Log.d(TAG, String.valueOf(icon[j]));

                        long timestamp = Long.parseLong(String.valueOf(time[j]));
                        expiry = new PersianDate(timestamp * 1000);




                    }



                     TimeWithIcons(time,icon);
                   // createChart(chart, forecastData, time);

                      /*  humidity[i] = String.valueOf(response.body().getList().get(i).getMain().getHumidity());
                        rain_description[i] = String.valueOf(response.body().getList().get(i).getWeather().get(0).getDescription());
                        icon[i] = String.valueOf(response.body().getList().get(i).getWeather().get(0).getIcon());
                        time[i] = String.valueOf(response.body().getList().get(i).getDt());
                        temp[i] = String.valueOf(response.body().getList().get(i).getMain().getTemp());

                        Log.w("humidity", humidity[i]);
                        Log.w("rain_description", rain_description[i]);
                        Log.w("icon", icon[i]);
                        Log.w("time", time[i]);
                        Log.w("temp", temp[i]);


                        weathers.add(new Weather(String.valueOf(response.body().getList().get(i).getWeather().get(0).getIcon()), String.valueOf(response.body().getList().get(i).getMain().getHumidity()), String.valueOf(response.body().getList().get(i).getWeather().get(0).getDescription()), String.valueOf(response.body().getList().get(i).getDt())));
*/

                    createChart(chart,forecastData,time);

                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                chart.setNoDataText(t.getMessage());

            }
        });


    }

    private void TimeWithIcons(String[] time, String[] icon) {


        String[] icons =new String[7];
        for (int i=0; i<7;i++){
            String geticon=icon[i];
            icons[i]=geticon;

        }
        iconWeather.addAll(Arrays.asList(icons));

        try {
            String[] hour=new String[7];
            for (int j = 0; j < 7; j++) {
                long timestamp = Long.parseLong(String.valueOf(time[j]));
               PersianDate persianDate = new PersianDate(timestamp * 1000);

                hour[j]= String.valueOf(persianDate.getHour() + ":00");


            }
            hourTime.addAll(Arrays.asList(hour));
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        initRecyclerView();

    }
    private void initRecyclerView(){

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.recycler_hourly);
        recyclerView.setLayoutManager(linearLayoutManager);
        HourlyAdapter hourlyAdapter=new HourlyAdapter(this,hourTime,iconWeather);
       recyclerView.setAdapter(hourlyAdapter);

    }

    private void createChart(LineChart chart, Double[] forecastData, String[] time) {

        chart.setAutoScaleMinMaxEnabled(true);
        chart.getDescription().setText("");
        List<Entry> entries = new ArrayList<>();
        float x = 0f;
        for (Double eachData : forecastData) {
            entries.add(new Entry(x, eachData.floatValue()));
            x++;
        }
        LineDataSet dataSet = new LineDataSet(entries, "Temperature");

        String[] wdate = new String[7];
        for (int j = 0; j < 7; j++) {
            long timestamp = Long.parseLong(String.valueOf(time[j]));
            expiry = new PersianDate(timestamp * 1000);

            wdate[j] = String.valueOf(expiry.getHour()+ ":00"   );


        }


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(wdate));
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setValueTextSize(15f);
        dataSet.setValueTextColor(Color.BLUE);

       /* YAxis yLeft=chart.getAxis(YAxis.AxisDependency.LEFT);
        yLeft.setDrawGridLines(false);
        yLeft.setDrawAxisLine(false);
        yLeft.setDrawLabels(false);
        yLeft.setValueFormatter(new MyXAxisValueFormatter(wdate));
        dataSet.setValueTextSize(15f);
        dataSet.setValueTextColor(Color.BLUE);*/

        dataSet.setColor(Color.rgb(80, 181, 222));

        dataSet.setFillAlpha(100);
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
        chart.animateY(1000, Easing.EasingOption.EaseInBounce);
        chart.invalidate();


    }
}