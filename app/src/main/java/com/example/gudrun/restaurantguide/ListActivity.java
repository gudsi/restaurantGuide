package com.example.gudrun.restaurantguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ListActivity extends AppCompatActivity {

    ListView lv;
    NodeList receveidRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        String nodeListString = getIntent().getStringExtra("nodeList");
        String[] names =new String[10];
        lv=findViewById(R.id.listview);
        // parse the received XML including the restaurants in the environment
        parsexml(getIntent().getStringExtra("nodeList")).toArray(names);
    }

    // parse information needed
    public ArrayList<String> parsexml(String response) {
        ArrayList<String> tmp = new ArrayList<>();
        final Map<Integer, Node> sortedList = new HashMap<>();
        int counter = 0;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(response)));

            NodeList nList = doc.getElementsByTagName("tag");
            receveidRestaurant = doc.getElementsByTagName("node");    // list of every restaurant

            // iterate over tags to find the name of the restaurant
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("node: " + nNode.getAttributes());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("k").equals("name")) {
                        tmp.add(eElement.getAttribute("v"));
                        sortedList.put(counter, eElement.getParentNode());
                        counter++;
                    }
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tmp );

            // If a restaurant is selected, go to the compass with the selected lat and lon
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getApplicationContext(),"Item Clicked:"+i,Toast.LENGTH_SHORT).show();
                    Element item = (Element) sortedList.get(i);
                    String lat = item.getAttribute("lat");
                    String lon = item.getAttribute("lon");

                    Intent intent = new Intent(getApplicationContext(), Compass.class);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }
}

