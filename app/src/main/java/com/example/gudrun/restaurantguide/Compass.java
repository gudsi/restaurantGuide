package com.example.gudrun.restaurantguide;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Compass extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor compass;
    private ImageView image;
    private TextView compassAngle;
    private float currentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        Intent i = getIntent();
        String lat = getIntent().getStringExtra("lat");
        String lon = getIntent().getStringExtra("lon");
        System.out.println("lat: " + lat + "    lon: " + lon);
        System.out.println("Hui");

        //Compass
        image = (ImageView) findViewById(R.id.imageViewCompass);
        compassAngle = (TextView) findViewById(R.id.angle);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        compass = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (compass != null) {
            sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        compassAngle.setText("Heading : " + Float.toString(degree) + " degrees");
        RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(210);
        ra.setFillAfter(true);
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // method to Compute distance according to haversine formula
    public static Double calculatedistance(long latitude1,long latitude2, long longitude1,long longitude2) {
        int R = 6371;//earth radius in kilometers
        Double latoneRad = Math.toRadians(latitude1);//convert latitude one degree to radian
        Double lattwoRad =  Math.toRadians(latitude2);//convert latitude two degree to radian
        Double latdiffRad= Math.toRadians(latitude2-latitude1);
        Double londiffRad= Math.toRadians(longitude2-longitude1);
        //calculate a
        Double a = Math.sin(latdiffRad/2)*Math.sin(latdiffRad/2)+Math.cos(latoneRad)*Math.cos(lattwoRad)*Math.sin(londiffRad/2)*Math.sin(londiffRad/2);
        //calculate c
        Double c= 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        //calculate d
        Double d=R*c;
        return d;
    }

    // method to Compute bearing
    public static Double calculatebearing(long latitude1,long latitude2, long longitude1,long longitude2) {
        Double latoneRad = Math.toRadians(latitude1);//convert latitude one degree to radian
        Double lattwoRad = Math.toRadians(latitude2);//convert latitude two degree to radian
        Double londiffRad= Math.toRadians(longitude2-longitude1);//difference of longitude 1 & 2 to radian
        //calculate y
        Double y = (Math.sin(londiffRad)*Math.cos(lattwoRad));
        //calculate x
        Double x = (Math.cos(latoneRad)*Math.sin(lattwoRad)-Math.sin(latoneRad)*Math.cos(lattwoRad)*Math.cos(londiffRad));
        //calculate bearing formula
        Double b=  Math.toDegrees(Math.atan2(y,x));
        return b;
    }


}
