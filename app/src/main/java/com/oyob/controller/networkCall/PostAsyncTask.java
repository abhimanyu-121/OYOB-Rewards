package com.oyob.controller.networkCall;

import android.os.AsyncTask;

/**
 * Created by 121 on 9/12/2017.
 */


public class PostAsyncTask extends AsyncTask<String, String, String> {
        HttpConnection httpConnection;
        String string = new String();
        String url;
        AsyncResponse asyncResponse;


        public PostAsyncTask(String responsestring, String url, AsyncResponse asyncResponse) {
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
            String response = httpConnection.sendPost(url, string);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            asyncResponse.processResponse(result);
        }
}
