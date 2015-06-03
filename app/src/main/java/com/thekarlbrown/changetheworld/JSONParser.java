package com.thekarlbrown.changetheworld;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * JSONParser that turns HTTP GET request into JSON, accompanies ASyncParser
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class JSONParser {

    static InputStream is = null;
    static JSONArray jObj = null;
    static String json = "";
    URL url=null;
    HttpURLConnection connection;

    public JSONArray makeHttpRequest(String urlstring, String method) throws IOException {
        try {
            //Check for request method
            if (method.equals("POST")) {
                //Not currently implemented
            } else if (method.equals("GET")) {
                //Open HTTP connection and obtain JSON data
                try{
                    url=new URL(urlstring);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    is = connection.getInputStream();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        is.close();
                        json = sb.toString();
                    } catch (Exception e) {
                        Log.e("Buffer Error", "Error converting result " + e.toString());
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }catch(Exception e)
                {
                    Log.e("Buffer error","Take a break" + e.toString());
                }
            }
        } catch (Exception ex) {
            Log.d("Networking", ex.getLocalizedMessage());
        }
        //Try parsing the string to a JSON object
        try{
            jObj = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }
}