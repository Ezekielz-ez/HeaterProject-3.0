package com.example.asd.heaterproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SontrasTestActivity extends AppCompatActivity implements View.OnClickListener {

    static TextView location;
    static TextView weatherDescription;
    static TextView outdoorTemperature;
    static TextView outdoorHumidity;
    static TextView intro;
    // name for shared preference storing lat long data
    private static final String LOCATIONID = "LatLong";
    // we should also get temp and humidity from shared preferences
    // but for now I am using a fake temperature value
    public static int indoorTemperature = 21;
    // counter for testing purposes
    private static int counter2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sontras);

        // find text views
        intro = (TextView) findViewById(R.id.textView1);
        TextView weatherIntro = (TextView) findViewById(R.id.textView2);
        location = (TextView) findViewById(R.id.placeName);
        weatherDescription = (TextView) findViewById(R.id.weatherDescription);
        outdoorTemperature = (TextView) findViewById(R.id.outdoorTemp);
        outdoorHumidity = (TextView) findViewById(R.id.outdoorHumidity);

        // find buttons
        Button goBack = (Button) findViewById(R.id.goBackButton);
        Button openMap = (Button) findViewById(R.id.openMapButton);
        Button forgeData = (Button) findViewById(R.id.forgeryButton);
        goBack.setOnClickListener(this);
        openMap.setOnClickListener(this);
        forgeData.setOnClickListener(this);

        // get location coordinates from shared preferences
        // default coordinates are in Inari, Lapland
        SharedPreferences prefGet = getSharedPreferences(LOCATIONID, Activity.MODE_PRIVATE);
        double latitude = Double.valueOf(prefGet.getString("latitude","68.9105231"));
        double longitude = Double.valueOf(prefGet.getString("longitude","27.0174193"));

        // create an instance of the weather API class and call its execute
        WeatherAPI gibWeather = new WeatherAPI();
        gibWeather.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude +
                        "&lon=" + longitude + "&appid=41bc4335b5c44b26947871ea435a4a49");

        // change a string
        intro.setText("Indoor temperature is now " + indoorTemperature + "°C");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openMapButton:
                Intent maps = new Intent(this, MapsActivity.class);
                startActivity(maps);
                break;
            case R.id.forgeryButton:
                intro.setText("Indoor temperature will change in 10 seconds...");
                startTimer();
                break;
            case R.id.goBackButton:
                finish();
                break;
        }
    }

    // timer for testing purposes
    private Timer timer2;
    private TimerTask timerTask2;
    public void startTimer(){
        timer2 = new Timer();
        initialiseTimerTask();
        // schedule the timer to wake up every 5 seconds
        timer2.schedule(timerTask2, 5000, 5000);
    }
    public void initialiseTimerTask(){
        timerTask2 = new TimerTask(){
            public void run(){
                if(counter2 == 2){
                    indoorTemperature = WeatherAPI.tempCelsius + 4;
                }
                counter2++;
            }
        };
    }
}
