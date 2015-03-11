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

public class JSONParser {

    static InputStream is = null;
    static JSONArray jObj = null;
    static String json = "";
    URL url=null;
    HttpURLConnection connection;

    // constructor
    public JSONParser() {

    }

    // function get json from url
// by making HTTP POST or GET method
    public JSONArray makeHttpRequest(String urlstring, String method
    ) throws IOException {
        //List params;
        // Making HTTP request
        try {
            // check for request method
            if (method.equals("POST")) {
                // request method is POST
                // defaultHttpClient
                //DefaultHttpClient httpClient = new DefaultHttpClient();
                //HttpPost httpPost = new HttpPost(urlstring);
                //httpPost.setEntity(new UrlEncodedFormEntity(params));
                //HttpResponse httpResponse = httpClient.execute(httpPost);
                //HttpEntity httpEntity = httpResponse.getEntity();
                //is = httpEntity.getContent();
            } else if (method.equals("GET")) {
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
            //Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        // try parse the string to a JSON object
        try{
            jObj = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;
    }
}