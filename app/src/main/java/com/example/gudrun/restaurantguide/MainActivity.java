package com.example.gudrun.restaurantguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
Button button;
Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button =findViewById(R.id.button);
        spinner=findViewById(R.id.idspinner);
        String[] distancevalues= {"5000","10000","15000"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,distancevalues);
        spinner.setAdapter(adapter);
        int value = Integer.parseInt((String) spinner.getSelectedItem());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOSM();
            }
        });

    }
    void callOSM()  {
        int radius = Integer.parseInt((String) spinner.getSelectedItem());

        //  new NetworkAsyncTask().execute();
        final NetworkAsyncTask httpsTask = new NetworkAsyncTask(radius,0,0);
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
}


//TODO - set the put request and store result in a file.

