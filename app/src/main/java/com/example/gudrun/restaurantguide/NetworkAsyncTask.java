package com.example.gudrun.restaurantguide;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

class NetworkAsyncTask extends AsyncTask  {

    String response = "";

    @Override
    protected Object doInBackground(Object[] objects) {
        URL url = null;

        try {
            url = new URL("https://lz4.overpass-api.de/api/interpreter");
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setDoOutput(true);
            httpsCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpsCon.getOutputStream());
            out.write("<osm-script>\n" +
                    "  <query type=\"node\">\n" +
                    "    <has-kv k=\"amenity\" v=\"restaurant\"/>\n" +
                    "<has-kv k=\"wheelchair\" v=\"yes\"/>\n" +
                    "<around radius=\"1000.0\" lat=\"41.89248629819397\" lon=\"12.51119613647461\"/>\n" +
                    "  </query>\n" +
                    "  <print/>\n" +
                    "</osm-script>");
            out.close();

            int responseCode=httpsCon.getResponseCode();
            System.out.println(httpsCon.getContentType());
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}

