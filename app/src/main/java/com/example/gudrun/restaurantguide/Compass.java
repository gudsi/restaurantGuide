package com.example.gudrun.restaurantguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Compass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        String nodeListString = getIntent().getStringExtra("item");
        System.out.println("item: " + nodeListString);
    }
}
