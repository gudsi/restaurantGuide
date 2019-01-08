package com.example.gudrun.restaurantguide;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView lat;
    TextView lon;

    TextView showResponse;

    //Compass
    private SensorManager sensorManager;
    private Sensor compass;
    private ImageView image;
    private TextView compassAngle;
    private float currentDegree = 0f;
    Spinner spinner;
    Button OSMButton;
    JSONObject test = null;
    // @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat = (TextView) findViewById(R.id.textView2);
        lon = (TextView) findViewById(R.id.textView4);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();
        
        spinner=(Spinner)findViewById(R.id.idSpinner);
        String[] distancevalues= {"5000","10000","15000"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,distancevalues);
        spinner.setAdapter(adapter);
        int value = Integer.parseInt((String) spinner.getSelectedItem());

        // allowing app to get location of phone
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
       //The minimum distance will be the distance selected
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, value, ll);


        //Compass
        image = (ImageView) findViewById(R.id.imageViewCompass);
        compassAngle = (TextView) findViewById(R.id.angle);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        compass = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (compass != null) {

            sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);

        }

        OSMButton = (Button) findViewById(R.id.button);
        OSMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    callOSM();
            }
        });

        showResponse =(TextView)findViewById(R.id.testText);
    }

        void callOSM()  {
            showResponse.setText("hello");
          //  new NetworkAsyncTask().execute();

            final NetworkAsyncTask httpsTask = new NetworkAsyncTask();
            httpsTask.execute();
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        final Object response = httpsTask.get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showResponse.setText(Objects.toString(response));
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


  /*  @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);

    }*/

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    class myLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            if(location!=null){
                double pLat=location.getLatitude();
                double pLong=location.getLongitude();
                lat.setText(Double.toString(pLat));
                lon.setText(Double.toString(pLong));
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    }

    /*
    <osm-script>
  <query type="node">
    <has-kv k="amenity" v="restaurant"/>
<has-kv k="wheelchair" v="yes"/>
<around radius="1000.0" lat="41.89248629819397" lon="12.51119613647461"/>
  </query>
  <print/>
</osm-script>
     */
}

//TODO - set the put request and store result in a file.

