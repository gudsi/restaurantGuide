package com.example.gudrun.restaurantguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Compass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        Intent i = getIntent();
        String lat = getIntent().getStringExtra("lat");
        String lon = getIntent().getStringExtra("lon");
        System.out.println("lat: " + lat + "    lon: " + lon);
        System.out.println("Hui");
    }
}
