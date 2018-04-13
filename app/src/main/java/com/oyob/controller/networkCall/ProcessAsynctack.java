package com.oyob.controller.networkCall;

import android.os.AsyncTask;

/**
 * Created by rama on 1/13/2017.
 */

public class ProcessAsynctack extends AsyncTask<String, String, String> {
    HttpConnection httpConnection;
    String string = new String();
    String url;
    AsyncResponse asyncResponse;


    public ProcessAsynctack(String responsestring, String url, AsyncResponse asyncResponse) {
        this.url = url;
        this.string = responsestring;
        this.asyncResponse = asyncResponse;
        httpConnection = new HttpConnection();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls)
    {
        String response = null;
        try {
            response = httpConnection.sendGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        asyncResponse.processResponse(result);
    }
}

