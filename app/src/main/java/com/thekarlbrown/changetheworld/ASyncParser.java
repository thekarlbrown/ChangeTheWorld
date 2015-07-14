package com.thekarlbrown.changetheworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import java.io.IOException;

/**
 * Class that serves to ASyncTask requests to obtain a JSON Array from my PHP Web API
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class ASyncParser extends AsyncTask<String, String, JSONArray>
{
    JSONParser jsonParser=new JSONParser();
    JSONArray jsonArray;
    String http_request;
    String titleOfOperation;
    Context contextOfParser;
    ProgressDialog progressDialog;
    boolean useProgressDialog;

    /**
     * AsyncParser with Progress Dialog to indicate networking
     * @param url_request Web API access point on server and GET requests
     * @param titleOfOperation Title of Progress Dialog indicating operations are being performed
     * @param context MainActivity context to show Progress Dialog in
     */
    public ASyncParser(String url_request,String titleOfOperation, Context context){
        http_request=url_request;
        this.titleOfOperation=titleOfOperation;
        contextOfParser=context;
        useProgressDialog=true;
    }

    /**
     * AsyncParser base operation
     * @param url_request Web API access point on server and GET requests
     */
    public ASyncParser(String url_request){
        http_request=url_request;
        useProgressDialog=false;
    }
    @Override
    public void onPreExecute(){
        if(useProgressDialog){ progressDialog= ProgressDialog.show(contextOfParser,titleOfOperation,"Please wait"); }
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
    @Override
    protected void onPostExecute(JSONArray jsonResult){
       if (useProgressDialog){ progressDialog.dismiss(); }
    }
}
