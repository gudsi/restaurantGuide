package com.example.gudrun.restaurantguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
