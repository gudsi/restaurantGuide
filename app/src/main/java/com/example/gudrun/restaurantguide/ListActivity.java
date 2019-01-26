package com.example.gudrun.restaurantguide;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ListActivity extends AppCompatActivity {

    ListView lv;
    String[] List={"value1","value2","value3","value4","value5"};
    NodeList restaurants;
    NodeList receveidRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        String nodeListString = getIntent().getStringExtra("nodeList");
        System.out.println("nlString: " + nodeListString);
        lv=findViewById(R.id.listview);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,List);
       // ArrayAdapter<Node> adapter=new ArrayAdapter<Node>(this,android.R.layout.simple_list_item_1,restaurants);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"Item Clicked:"+i,Toast.LENGTH_SHORT).show();
            }
        });


    }


    public NodeList parsexml(String response) {
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response)));
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("tag");
            receveidRestaurant = doc.getElementsByTagName("node");    // list of every restaurant

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                System.out.println("node: " + nNode.getAttributes());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    //System.out.println("node: " + nNode.getAttributes());
                    Element eElement = (Element) nNode;

                    System.out.println("key : " + eElement.getAttribute("k"));
                    System.out.println("value : " + eElement.getAttribute("v"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receveidRestaurant;
    }

}
