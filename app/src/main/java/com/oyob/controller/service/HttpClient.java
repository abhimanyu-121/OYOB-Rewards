package com.oyob.controller.service;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.oyob.controller.utils.AppConstants;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


public class HttpClient extends AsyncTask<String, String, String> {
	private static final String TAG = "DispatcherHandler";
	private NetworkListener listener;
	private String message;
	String requestBody=null;
	HashMap<String,String> header;
	private int eventType;
	String sessionID;
	Bitmap thumbnail =null;

	/**
	 * setListener for setting Newtorklistner  interface
	 * @param listener  ===> instance of Newtorklistner
	 *
	 */
	public void setListener(NetworkListener listener) {
		this.listener = listener;
	}

	private  HttpClient() {
		// TODO Auto-generated constructor stub
	}
	public static HttpClient getWWDispatchHandler() {
		/*if (httpClient == null) {
			httpClient = new HttpClient();
		}*/ //TODO: Un-comment the code if DispatcherHandler has to be singleton
		return new HttpClient();
	}

	/**
	 * sendRequest is a Sync method for sending Synchronous Request to particular client instance and receive a response for both  Post and Get requests based on the input JSON instance
	 * @param URL ===> Url for where we need to send request and get response.ex:http://www.c0de.com.ar/android-library-json2.php
	 * @param requestBody =====> JSONObject is the response object for particular request if it is null i.e Get request otherwise post request
	 * @param header ====> This is hashmap object for setting header of url
	 * @return ===> return a Json object
	 */
	String sendRequest(final String URL, final String requestBody,final HashMap<String,String> header) {
		return sendInternalRequest(URL,requestBody,header);
	}
	private String sendInternalRequest(String URL, String requestEntity,HashMap<String,String> header) {
		Log.d(TAG,"client request sent  Url is: "+URL);
		Log.d(TAG,"client request sent: "+requestEntity);

		try {
			HttpResponse response = null;
			HttpRequestBase httpRequest = null;
			DefaultHttpClient httpclient = new DefaultHttpClient();


			if (requestEntity != null) {
				httpRequest = new HttpPost(URL);
				((HttpPost)httpRequest).addHeader("Content-Type", "application/x-www-form-urlencoded");
				/*((HttpPost)httpRequest).setEntity(new StringEntity(requestEntity,"UTF-8"));
				((HttpPost)httpRequest).addHeader("X-Requested-With", "XMLHttpRequest");*//*
				((HttpPost)httpRequest).addHeader("Content-Type", "application/x-www-form-urlencoded");*/

			}else {
				httpRequest = new HttpGet(URL);
				/*((HttpGet)httpRequest).setHeader("Content-Type", "text/auto; charset=UTF-8");
				((HttpGet)httpRequest).setHeader("Cookie", AppConstants.PHP_SESSION_KEY+ Utility.COOKIE);*/
			}
			response = (HttpResponse) httpclient.execute(httpRequest);

			/*if(eventType == 1 && response != null){
				Header sessionIDHeader = response.getFirstHeader("Set-Cookie");
				String headerString = sessionIDHeader.toString().split(": ")[1];
				headerString = headerString.split(";")[0];
				Utility.COOKIE = headerString.split("=")[1];

			}*/
			Header[] headers = response.getAllHeaders();
			for(int i=0;i<headers.length;i++){
				System.out.println("header:   "+headers[i]);
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String resultString = convertStreamToString(instream);
				instream.close();

				Log.d(TAG,"server response: "+resultString);

				return resultString;
			}

		} catch (Exception e) {
			e.printStackTrace();
			setMessage(e.toString());
		}
		return null;
	}

	/**
	 * sendRequest is a Sync method for sending Synchronous Request to particular client instance and receive a response for both  Post and Get requests based on the input JSON instance
	 * @param URL ===> Url for where we need to send request and get response.ex:http://www.c0de.com.ar/android-library-json2.php
	 * @param jsonObjSend =====> JSONObject is the response object for particular request if it is null i.e Get request otherwise post request
	 * @param header ====> Hashmap instance is using for setting header of particular url like "Accept", "application/json"
	 * @param listener ====> instance of Newtorklistner  for showing response to user/client
	 * @return ==> return a boolean for callback success(true) or failure (false)
	 */
	public boolean sendRequestAsync(String URL, String jsonObjSend,HashMap<String,String> header,NetworkListener listener, int eventType) {
		this.requestBody=jsonObjSend;
		this.listener=listener;
		this.header=header;
		this.eventType = eventType;
		if(eventType == 1){
//	    	getClientBanner(URL);
		}else{
			URL=URL.replace(" ", "%20");
			execute(URL);
		}
		return true;
	}
	//	void getClientBanner(String url1){
//		Drawable drawable = null;
//		try {
//		    URL url = new URL(url1);
//		    URLConnection connection = url.openConnection();
//		    connection.setUseCaches(true);
//		    // try to load and decode
//		    drawable = Drawable.createFromStream(connection.getInputStream(), "src");
//
//		    if(drawable != null) // if succeed - set drawable
//		    	Utility.user.setClientBanner(drawable);
//		} catch (IOException e) {
//		    // ignore IOException if any...
//		} finally {
////		    if(drawable == null) // If drawable are null (not decoded or failed to load (404?))
////		        viewHolder.icon.setImageResource(R.drawable.defaulticon); // set default image
//		}
//	}
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	@Override
	protected String doInBackground(String... URL) {
//		if(eventType == 3){
//	    	getClientBanner(URL[0]);
//	    }
		return sendInternalRequest(URL[0], requestBody,header);
	}

	protected void onPostExecute(String res) {
		if(listener != null && res != null){
//			if(eventType == 3)
//				Utility.user.setClientBanner(thumbnail);
			listener.onRequestCompleted(res, null, eventType);
		}
		else if(listener != null && (res == null || res.length() == 0))
			if(eventType != 1)
			{
				listener.onRequestCompleted(res, "No products found!", eventType);
			}
			else
			{
				listener.onRequestCompleted(res, AppConstants.UNABLETOESTABLISHCONNECTION_URL, eventType);
			}
		else
			Log.w(TAG, "No listener found for request completion. Set the NetworkListener using setNetworkListener");
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



}
