package com.thekarlbrown.changetheworld;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import java.io.IOException;

/**
 * Class that serves to ASyncTask requests to obtain a JSON Array from my PHP Web API
 * By Karl Brown ( thekarlbrown )
 */
public class ASyncParser extends AsyncTask<String, String, JSONArray>
{
    JSONParser jsonParser=new JSONParser();
    JSONArray jsonArray;
    String http_request;
    public ASyncParser(String url_request){
            http_request=url_request;
    }
    @Override
    protected JSONArray doInBackground(String... params) {
        //Checks that the async has started
        Log.println(0, "", "Starting JSON parse...");
        try {
            jsonArray=jsonParser.makeHttpRequest(http_request, "GET");
        }catch(IOException e){
            Log.println(0, "Error", e.getMessage());
        }
        //Checks that the async has ended
        Log.println(0, "", "Done with JSON parse...");
        return jsonArray;
    }
}
