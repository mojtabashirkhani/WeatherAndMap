package com.example.slimshady.googlemap;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slimshady.googlemap.Api.CurrentWeatherApi;
import com.example.slimshady.googlemap.Model_Current_Weather.OpenWeatherMap;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import saman.zamani.persiandate.PersianDate;

public class WeatherActivity extends AppCompatActivity  implements LocationListener {

    TextView txtTemp, txtCity, txtDesc,txtMaxTemp,txtMinTemp,txtHumidity,txtWind,txtSunrise,txtSunset;
    ImageView icon,wind,humidity,background,sunriseImg,sunsetImg;
    private double latitude, longitude;


    private String description;
    PersianDate sunset,sunrise;
    private String lat,lng;
    private String locality;
    LocationManager locationManager;
    private String provider;
    int MY_PERMMSION = 0;



    public static String API_KEY = "206de2de8ed1083ed8447ad281031489";
    public static String API_LINK = "http://api.openweathermap.org/data/2.5/";
    private static final String TAG = "WeatherActivity";
    private static final int REQUEST_LOCATION = 1888;
   // protected GoogleApiClient googleApiClient;
    protected Location lastLocation;
    private LocationRequest mLocationRequest;
    private PieChart pieChart;
    private static final int SETTINGS_CODE = 200;
    private static final int REQUEST_PERMISSIONS = 20;

    BroadcastReceiver _broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        txtCity = (TextView) findViewById(R.id.txtCity);
        txtTemp = (TextView) findViewById(R.id.txtTemp);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtMaxTemp=(TextView)findViewById(R.id.maxtemp);
        txtMinTemp=(TextView)findViewById(R.id.mintemp);
        icon=(ImageView)findViewById(R.id.image_weather);
        background=(ImageView)findViewById(R.id.background);

        pieChart=(PieChart)findViewById(R.id.pieChart);
        createPieChart(latitude, longitude);

       txtHumidity=(TextView)findViewById(R.id.txtHumidity);
       txtWind=(TextView)findViewById(R.id.txtWind);
        wind=(ImageView)findViewById(R.id.wind);
        humidity=(ImageView)findViewById(R.id.humidity);

        txtSunrise=(TextView)findViewById(R.id.txtSunrise);
        txtSunset=(TextView)findViewById(R.id.txtSunset);

        sunriseImg=(ImageView)findViewById(R.id.sunrise);
        sunsetImg=(ImageView)findViewById(R.id.sunset);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Gps is Enabled", Toast.LENGTH_SHORT).show();

        } else {
            buildGoogleApiClient();
        }*/
       // permissionChecks();
       // buildGoogleApiClient();

        // 1 second, in milliseconds

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,


            }, MY_PERMMSION);
            Location location;
            location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                Log.e("TAG", "no location");
            }


        }
    }

    private void createPieChart(final double latitude, final double longitude){
     pieChart.getDescription().setEnabled(false);
     pieChart.setExtraOffsets(5,10,5,5);
     pieChart.setDragDecelerationFrictionCoef(0.9f);
     pieChart.setDrawHoleEnabled(true);
     pieChart.setHoleColor(Color.TRANSPARENT);

     pieChart.setTransparentCircleRadius(65f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pieChart.setElevation(5f);
            pieChart.animateY(1400,  Easing.EasingOption.EaseInCubic);

        }

        pieChart.setClickable(true);
        pieChart.setTouchEnabled(true);
       // pieChart.setDescription(null);
        pieChart.getLegend().setEnabled(false);

        // float[] yData = { 5, 10, 15, 30, 40 };
        /* final String[] xData = { "hourly", "daily", "city", "help" };
        ArrayList<PieEntry> yVal=new ArrayList<>();

        for(int i=0;i<xData.length;i++){

            float val=(float)((Math.random()/4));
            yVal.add(new PieEntry(val,xData[i]));

        }*/
        ArrayList<PieEntry> yVal=new ArrayList<>();
        yVal.add(new PieEntry(20f,"Hourly"));
        yVal.add(new PieEntry(30f,"Daily"));
        yVal.add(new PieEntry(22f,"City"));
        yVal.add(new PieEntry(28f,"Help"));

        PieDataSet pieDataSet=new PieDataSet(yVal,"");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);

        PieData pieData=new PieData(pieDataSet);
        pieData.setValueTextSize(0);
        pieChart.setData(pieData);

       pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
           @Override
           public void onValueSelected(Entry e, Highlight h) {



               if (e == null)
                   return;

               Bundle bundle=getIntent().getExtras();

               if (bundle!=null) {
                   String latitudes = bundle.getString("lat");
                   String longintud = bundle.getString("lon");
                   lat=latitudes;
                   lng=longintud;

               }
               else if(bundle==null) {
                   lat = String.valueOf(latitude);
                   lng = String.valueOf(longitude);
               }


               if(e.getY()==20.0){

                   Intent hourly=new Intent(WeatherActivity.this,HourlyForeCastActivity.class);

                   hourly.putExtra("lat", lat);
                   hourly.putExtra("lon", lng);

                   startActivity(hourly);
               }
               if(e.getY()==30.0){

                   Intent daily=new Intent(WeatherActivity.this,DayForcastActivity.class);
                   daily.putExtra("lat", lat);
                   daily.putExtra("lon", lng);

                   startActivity(daily);
               }
               if(e.getY()==22.0){

                   Intent city=new Intent(WeatherActivity.this,MapActivity.class);
                   startActivity(city);
               }
               if(e.getY()==28.0){
                   Intent help=new Intent(WeatherActivity.this,HelpActivity.class);
                   startActivity(help);
               }


              /* Toast.makeText(WeatherActivity.this,
                       h.getStackIndex()+" "+ e.getY() + "%", Toast.LENGTH_SHORT).show();*/
           }

           @Override
           public void onNothingSelected() {

           }
       });



    }

    public void permissionChecks() {
        Log.d("Connected", "Check Permissions");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(WeatherActivity.this, "Need to access Location", Toast.LENGTH_LONG).show();
                }
                Log.d("Connected", "REQUEST PERMISSIONS");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
        }
    }

   /* protected synchronized void buildGoogleApiClient() {
        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            googleApiClient.connect();


        }
    }*/

   /* @Override
    public void onConnected(@Nullable Bundle bundle) {
        setLocation();

    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLocation();
            } else {
                Toast.makeText(WeatherActivity.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void setLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastLocation = locationManager.getLastKnownLocation(provider);


            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            Log.d("Latitude", String.valueOf(latitude));
            Log.d("Longitude", String.valueOf(longitude));




        lat= String.valueOf(latitude);
        lng= String.valueOf(longitude);



       /* try {
            Geocoder geo = new Geocoder(WeatherActivity.this.getApplicationContext(), Locale.ENGLISH);
            if(bundle!=null){
                List<Address> addresses = geo.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            txtCity.setText(addresses.get(0).getLocality());

            }
            else if(bundle==null) {
                List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
                if (addresses.isEmpty()) {
                    txtCity.setText("");
                } else {
                    if (addresses.size() > 0) {
                        txtCity.setText(addresses.get(0).getLocality());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

            getWeatherUpdateCall(lat, lng);
            createPieChart(latitude,longitude);


        }



    private void getWeatherUpdateCall(String lat, String lng) {


        Bundle bundle=getIntent().getExtras();

        if (bundle!=null) {
            String latitudes = bundle.getString("lat");
            String longintud = bundle.getString("lon");
            lat=latitudes;
            lng=longintud;

        }
        else if(bundle==null) {
            lat = String.valueOf(latitude);
            lng = String.valueOf(longitude);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_LINK)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentWeatherApi service = retrofit.create(CurrentWeatherApi.class);
        Call<OpenWeatherMap> call = service.getCurrentWeather(lat, lng,"metric",API_KEY);
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {


                if(response.isSuccessful()) {
                    String tolu, ghorub;

                   // String dateNtime;




                    tolu = String.valueOf(response.body().getSys().getSunrise());
                    ghorub = String.valueOf(response.body().getSys().getSunset());

                    long timestamp = Long.parseLong(String.valueOf(tolu));
                    sunrise = new PersianDate(timestamp * 1000);

                    long time = Long.parseLong(String.valueOf(ghorub));
                    sunset = new PersianDate(time * 1000);


                    tolu= String.valueOf((sunrise.getHour())+":"+(sunrise.getMinute())+":"+sunrise.getSecond());
                    ghorub= String.valueOf((sunset.getHour())+":"+(sunset.getMinute())+":"+sunset.getSecond());


                    /*if (sunrise.getMinute()+30<60) {
                        tolu = String.valueOf((sunrise.getHour() + 4)+":" + (sunrise.getMinute() + 30)+":"  + sunrise.getSecond());
                        if (sunrise.getHour()+4 < 24)
                            tolu = String.valueOf((sunrise.getHour() + 4)+":" + (sunrise.getMinute() + 30)+":"  + sunrise.getSecond());
                        else if(sunrise.getHour()+4>24)
                            tolu = String.valueOf((sunrise.getHour() -24 )+":"+ (sunrise.getMinute() + 30)+":"  + sunrise.getSecond());

                    }
                    else if(sunrise.getMinute()+30>60) {
                        tolu = String.valueOf((sunrise.getHour() + 5 )+":"+ (sunrise.getMinute() + 30 - 60)+":" + sunrise.getSecond());

                        if (sunrise.getHour() + 5 < 24)
                            tolu = String.valueOf((sunrise.getHour() + 5 )+":"+ (sunrise.getMinute() + 30 - 60 )+":"+ sunrise.getSecond());
                        else if(sunrise.getHour()+5>24)
                            tolu = String.valueOf((sunrise.getHour() -24)+":" + (sunrise.getMinute() + 30 - 60)+":" + sunrise.getSecond());

                    }
                    if (sunset.getMinute()+30<60) {
                        ghorub = String.valueOf((sunset.getHour() + 4 )+":"+ (sunset.getMinute() + 30 )+":"+ sunset.getSecond());
                        if (sunset.getHour() + 4 < 24)
                            ghorub = String.valueOf((sunset.getHour() + 4 )+":"+ (sunset.getMinute() + 30)+":" + sunset.getSecond());
                        else if (sunset.getHour() + 4 > 24)
                            ghorub = String.valueOf((sunset.getHour() - 24)+":" + (sunset.getMinute() + 30)+":" + sunset.getSecond());
                    }
                    else if(sunset.getMinute()+30>60) {
                        ghorub = String.valueOf((sunset.getHour() + 5)+":" + (sunset.getMinute() + 30 - 60)+":" + sunset.getSecond());
                        if (sunset.getHour() + 5 < 24)
                            ghorub = String.valueOf((sunset.getHour() + 5 )+":"+( sunset.getMinute() + 30-60)+":" + sunset.getSecond());
                        else if (sunset.getHour() + 5 > 24)
                            ghorub = String.valueOf((sunset.getHour() - 24 )+":"+ (sunset.getMinute() + 30 - 60 )+":"+ sunset.getSecond());
                    }*/


                    Log.d(TAG, tolu);
                    Log.d(TAG, ghorub);





                    description = response.body().getWeather().get(0).getDescription();
                    txtTemp.setText(String.valueOf(Math.round(response.body().getMain().getTemp())) + " °C");
                    txtDesc.setText(description);
                    txtCity.setText(String.format(response.body().getName()));
                    txtMaxTemp.setText(String.valueOf(Math.round(response.body().getMain().getTempMax())) + " °C");
                    txtMinTemp.setText(String.valueOf(Math.round(response.body().getMain().getTempMin())) + " °C");
                     txtHumidity.setText(String.valueOf(response.body().getMain().getHumidity())+" %");
                     txtWind.setText(String.valueOf(Math.round(response.body().getWind().getSpeed()*3.6))+" km/h");
                     txtSunrise.setText(tolu);
                     txtSunset.setText(ghorub);

                    /* if (txtCity.getText().equals("Kūy-e Ekhteşāşī-ye Gomrok")) {
                         txtCity.setText("Tehran");
                     }
*/

                    condition();


                }


            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                t.printStackTrace();

            }
        });


    }



   /* @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(WeatherActivity.this, "fail", Toast.LENGTH_LONG).show();
    }*/
    @Override
    public void onStart() {

        super.onStart();
      //  googleApiClient.connect();
        _broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
               // if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0)
                   // time_text.setText(_sdfWatchTime.format(new Date()));
            }
        };

        registerReceiver(_broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

    }

    @Override
    protected void onResume() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,


            }, MY_PERMMSION);
        }
        locationManager.requestLocationUpdates(provider, 400, 1,  this);

        super.onResume();

       // googleApiClient.connect();
    }

    @Override
    public void onPause() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,


            }, MY_PERMMSION);

            locationManager.removeUpdates( this);
        }
        super.onPause();

       /* if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();

        }*/
    }
    @Override
    public void onStop()
    {
        super.onStop();
        if (_broadcastReceiver != null)
            unregisterReceiver(_broadcastReceiver);
    }

    public void condition(){

       PersianDate pdate=new PersianDate();


         if(description.equals("scattered clouds")||description.equals("broken clouds")||description.equals("overcast clouds")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_26));
             background.setImageDrawable(getResources().getDrawable(R.drawable.clouds_p));
         }

           else if (description.equals("drizzle")||description.equals("light intensity drizzle")||description.equals("light intensity drizzle rain")||description.equals("heavy intensity drizzle")||description.equals("drizzle rain")||description.equals("heavy intensity drizzle rain")||description.equals("shower rain and drizzle")||description.equals("heavy shower rain and drizzle")||description.equals("shower drizzle")) {
             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));
             if(pdate.before(sunrise)&pdate.after(sunset))
                 icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_39));

             else
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_45));

         }

           else if (description.equals("light rain")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_39));
             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));

         }
           else if (description.equals("moderate rain")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_09));
             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));

         }
           else if (description.equals("heavy rain")||description.equals("very heavy rain")||description.equals("heavy intensity rain")||description.equals("light intensity shower rain")||description.equals("heavy intensity shower rain")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_12));

             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));
         }

           else if (description.equals("intense rain")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_01));

             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));
         }

           else if (description.equals("extreme rain")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_01));

             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));
         }

           else if (description.equals("freezing rain")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_42));
             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));

         }

           else if (description.equals("light shower")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_39));
             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));

         }

           else if (description.equals("shower")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_39));
             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));

         }

           else if (description.equals("heavy shower")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_39));
             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));
         }

           else if (description.equals("ragged shower")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_01));
             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));

         }

           else if (description.equals("light Snow")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_13));
             background.setImageDrawable(getResources().getDrawable(R.drawable.snow_p));

         }

           else if (description.equals("snow")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_14));
             background.setImageDrawable(getResources().getDrawable(R.drawable.snow_p));
         }


               else if (description.equals("heavy snow")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_18));
             background.setImageDrawable(getResources().getDrawable(R.drawable.snow_p));

         }

               else if (description.equals("sleet")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_06));
             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));

         }

               else if (description.equals("shower sleet")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_07));
             background.setImageDrawable(getResources().getDrawable(R.drawable.snow_p));

         }

               else if (description.equals("rain and snow")||description.equals("light rain and snow")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_42));
             background.setImageDrawable(getResources().getDrawable(R.drawable.snow_p));

         }

               else if (description.equals("shower snow")||description.equals("heavy shower snow")||description.equals("light shower snow")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_16));
             background.setImageDrawable(getResources().getDrawable(R.drawable.snow_p));
         }


               else if (description.equals("mist")||description.equals("smoke")||description.equals("sand, dust whirls")||description.equals("dust")||description.equals("squalls")||description.equals("volcanic ash"))
               icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_20));



              else if (description.equals("haze")||description.equals("fog")) {
             background.setImageDrawable(getResources().getDrawable(R.drawable.foggy_p));

             if(pdate.before(sunrise)& pdate.after(sunset))
                 icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_19));

             else
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_21));


         }


             else if (description.equals("clear")||description.equals("mostly")||description.equals("clear sky")||description.equals("calm")) {
             background.setImageDrawable(getResources().getDrawable(R.drawable.clear_p));

             if(pdate.before(sunrise)&pdate.after(sunset))
                 icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_32));

             else
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_31));


         }
               else if (description.equals("tropical storm")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_35));
             background.setImageDrawable(getResources().getDrawable(R.drawable.storm_p));


         }

         else if(description.equals("few clouds")){

             background.setImageDrawable(getResources().getDrawable(R.drawable.clear_p));

             if(pdate.before(sunrise)&pdate.after(sunset))
                 icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_30));

             else
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_29));

         }

               else if (description.equals("hurricane")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_23));
             background.setImageDrawable(getResources().getDrawable(R.drawable.storm_p));

         }

               else if (description.equals("hot"))
               icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_36));


                else if (description.equals("hail")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_10));
             background.setImageDrawable(getResources().getDrawable(R.drawable.rain_p));


         }

             else if (description.equals("thunderstorm")||description.equals("thunderstorm with rain")||description.equals("thunderstorm with heavy rain")||description.equals("light thunderstorm")||description.equals("heavy thunderstorm")||description.equals("ragged thunderstorm")||description.equals("thunderstorm with light drizzle")||description.equals("thunderstorm with drizzle")||description.equals("thunderstorm with heavy drizzle")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_00));
             background.setImageDrawable(getResources().getDrawable(R.drawable.storm_p));

         }


               else if (description.equals("hurricane")||description.equals("light breeze")||description.equals("gentle breeze")||description.equals("breeze")||description.equals("fresh breeze")||description.equals("strong breeze"))
               icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_23));


               else if (description.equals("storm")||description.equals("violent")|| description.equals("windy")||description.equals("cold")||description.equals("tornado")||description.equals("high wind")||description.equals("severe gale")||description.equals("gale")) {
             icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_23));
             background.setImageDrawable(getResources().getDrawable(R.drawable.storm_p));

         }

          else
               icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_weather_set_1_na));
       // background.setImageDrawable(getResources().getDrawable(R.drawable.back));

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();

        getWeatherUpdateCall(String.valueOf(latitude),String.valueOf(longitude));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /*@Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }*/
}
