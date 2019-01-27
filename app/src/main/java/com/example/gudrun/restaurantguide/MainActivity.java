package com.example.gudrun.restaurantguide;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView lat;
    TextView lon;
    Spinner spinner;
    Button OSMButton;

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

        OSMButton = (Button) findViewById(R.id.button);
        OSMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOSM();

            }
        });
    }

    void callOSM()  {
        //  new NetworkAsyncTask().execute();
        final NetworkAsyncTask httpsTask = new NetworkAsyncTask();
        httpsTask.execute();
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    final Object response = httpsTask.get();
                    //NodeList nodeList = parsexml();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//  //                              showResponse.setText(Objects.toString(response));
//
//                            }
//                        });
                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                    intent.putExtra("nodeList", Objects.toString(response));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

