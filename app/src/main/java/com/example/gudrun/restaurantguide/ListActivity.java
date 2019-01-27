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
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ListActivity extends AppCompatActivity {

    ListView lv;
    //String[] names={"value1","value2","value3","value4","value5"};
    NodeList restaurants;
    NodeList receveidRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        String nodeListString = getIntent().getStringExtra("nodeList");
        System.out.println("nlString: " + nodeListString);
        String[] names =new String[10];
        lv=findViewById(R.id.listview);
        parsexml(getIntent().getStringExtra("nodeList")).toArray(names);

       // ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
       // ArrayAdapter<Node> adapter=new ArrayAdapter<Node>(this,android.R.layout.simple_list_item_1,restaurants);
       // lv.setAdapter(adapter);
    }
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
    public ArrayList<String> parsexml(String response) {
        ArrayList<String> tmp = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(response)));


            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("tag");
            receveidRestaurant = doc.getElementsByTagName("node");    // list of every restaurant

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                System.out.println("node: " + nNode.getAttributes());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    //System.out.println("node: " + nNode.getAttributes());
                    Element eElement = (Element) nNode;
                    System.out.println("\nRestaurant Names : " + eElement.getElementsByTagName("name"));
                    System.out.println("key : " + eElement.getAttribute("k"));
                    if(eElement.getAttribute("k").equals("name")) {
                        tmp.add(eElement.getAttribute("v"));
                    }
                    System.out.println("value : " + eElement.getAttribute("v"));
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tmp );

            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getApplicationContext(),"Item Clicked:"+i,Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }
}

