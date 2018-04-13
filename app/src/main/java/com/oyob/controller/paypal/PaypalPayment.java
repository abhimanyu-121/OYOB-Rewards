package com.oyob.controller.paypal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.oyob.controller.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

public class PaypalPayment extends Activity
{
	
	// ===========================================================
	// Widgets
	// ===========================================================
		WebView webview_payment;
		ProgressDialog progressDialog;
	// ===========================================================
	// Fields/Variables
	// ===========================================================
		protected static final int INITIALIZE_SUCCESS = 0;
		protected static final int INITIALIZE_FAILURE = 1;
		String b1,b2;
		String Ack,PayerID,token,EMAIL,FIRSTNAME,TRANSACTIONID,amount,finalamount,price;
		boolean isconnect;
		String pricetouser = "10";
	// ===========================================================
	// Methods
	// ===========================================================
		HttpEntity resEntity;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paypalpayment);
		
		//parseMessage=getResources().getString(R.string.wait);
		//price=getIntent().getStringExtra("price");

		progressDialog = new ProgressDialog(PaypalPayment.this);
		progressDialog.setProgressStyle(DialogInterface.BUTTON_NEUTRAL);
		progressDialog.setMessage(parseMessage);
		
		webview_payment=(WebView)findViewById(R.id.webview_payment);
		webview_payment.getSettings().setJavaScriptEnabled(true);
		webview_payment.setWebViewClient(new PPWebViewClient());
		
		new SetExpressCheck().execute();
	}
	
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		PaypalPayment.this.finish();
	}
	
	public class SetExpressCheck extends AsyncTask<Void, Void, Void>
	{
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();
            progressDialog.show();
        }
        
        @Override
		protected Void doInBackground(Void... params)
        {
        	getAccessToken();
			return null;
		}
        
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if(b2.equals("Success"))
            {
            	launchWeb();
            }
        }
	}
	
	protected void launchWeb() {
		// TODO Auto-generated method stub
		String returnurl = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout-mobile&useraction=commit&token="+b1;
		webview_payment.loadUrl(returnurl);
	}
	
	protected void getAccessToken()
	{
		// TODO Auto-generated method stub
		DefaultHttpClient httpclient = new DefaultHttpClient();
        // Your URL
        HttpPost httppost = new HttpPost("https://api-3t.sandbox.paypal.com/nvp?");
        try {
        	
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // Your DATA
            nameValuePairs.add(new BasicNameValuePair("METHOD","SetExpressCheckout"));
            nameValuePairs.add(new BasicNameValuePair("VERSION","88"));
            nameValuePairs.add(new BasicNameValuePair("USER","piyushpatel.ppp_api1.gmail.com"));
            nameValuePairs.add(new BasicNameValuePair("PWD","VKST6ZTMMYD3EB9K"));
            nameValuePairs.add(new BasicNameValuePair("SIGNATURE","ALQj17MJ62KdhsYevBehbKBMwq4aA-HCi5YlBTYQ.sd-s-Wk3Nf4Ys2L"));
            nameValuePairs.add(new BasicNameValuePair("ALLOWNOTE","1"));
            nameValuePairs.add(new BasicNameValuePair("SOLUTIONTYPE","Sole"));
            nameValuePairs.add(new BasicNameValuePair("REQCONFIRMSHIPPING","0"));
            nameValuePairs.add(new BasicNameValuePair("PAYMENTREQUEST_0_AMT",pricetouser));
            nameValuePairs.add(new BasicNameValuePair("PAYMENTREQUEST_0_PAYMENTACTION","Sale"));
            nameValuePairs.add(new BasicNameValuePair("PAYMENTREQUEST_0_CURRENCYCODE","USD"));
            nameValuePairs.add(new BasicNameValuePair("CANCELURL","https://www.myrewards.tk/main/errorAbo"));
            nameValuePairs.add(new BasicNameValuePair("RETURNURL","https://www.myrewards.tk"));
            System.out.println("nameValuePairs =====>" +nameValuePairs.toString());
            
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            System.out.println("Post ====>" +httppost.getURI());
            HttpResponse response;
            
            response = httpclient.execute(httppost);
            
            resEntity = response.getEntity();
            
            String response_str = EntityUtils.toString(resEntity);
            String afterDecode = URLDecoder.decode(response_str, "UTF-8");
            System.out.println("afterDecode ====>" +afterDecode);
            
            StringTokenizer one = new StringTokenizer(afterDecode,"&");
            String a = one.nextToken();
            String b = one.nextToken();
            String c = one.nextToken();
            String d = one.nextToken();
            
            StringTokenizer two = new StringTokenizer(a,"=");
            System.out.println("====>"+two.countTokens());
            String a1 = two.nextToken();
            b1 = two.nextToken();
            
            StringTokenizer three = new StringTokenizer(d,"=");
            System.out.println("====>"+three.countTokens());
            String a2 = three.nextToken();
            b2 = three.nextToken();
            
            System.out.println("My Values ====>" +b1+ "  " +b2);
            
            
        } catch (ClientProtocolException e) {
            
            e.printStackTrace();
        } catch (IOException e) {
           
            e.printStackTrace();
        }
	}
	
	private class PPWebViewClient extends WebViewClient
	{
		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			progressDialog.cancel();
			
			System.out.println("URL in onPageFinished ====>" +url);
			if(url.contains("https://www.myrewards.tk"))
			{
				//GetExCheckoutDetails();
               new GetExpressCheckOutAsyn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null);
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			System.out.println("URL ====>" +url);
			if(url.contains("https://www.myrewards.tk"))
			{
				
			}
			
			return super.shouldOverrideUrlLoading(view, url);
			
		}
	}

	public static boolean isPaymentSuccess = false;
	public class GetExpressCheckOutAsyn extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            GetExCheckoutDetails();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(isPaymentSuccess) {
                showAlert("Payment Success");
            }
        }
    }

    public void GetExCheckoutDetails() {
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
        // Your URL
        HttpPost httppost = new HttpPost("https://api-3t.sandbox.paypal.com/nvp/");
        
        try {
        	
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // Your DATA
            nameValuePairs.add(new BasicNameValuePair("USER","piyushpatel.ppp_api1.gmail.com"));
            nameValuePairs.add(new BasicNameValuePair("PWD","VKST6ZTMMYD3EB9K"));
            nameValuePairs.add(new BasicNameValuePair("SIGNATURE","ALQj17MJ62KdhsYevBehbKBMwq4aA-HCi5YlBTYQ.sd-s-Wk3Nf4Ys2L"));
            nameValuePairs.add(new BasicNameValuePair("VERSION","88"));
            nameValuePairs.add(new BasicNameValuePair("METHOD","GetExpressCheckoutDetails"));
            nameValuePairs.add(new BasicNameValuePair("TOKEN",""+b1));
            
            System.out.println("nameValuePairs in GetExpressCheckoutDetails=====>" +nameValuePairs.toString());
            
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response;
            response = httpclient.execute(httppost);
            resEntity = response.getEntity();
            String response_str = EntityUtils.toString(resEntity);
            
            String afterDecode = URLDecoder.decode(response_str, "UTF-8");
            
            String[] separated = afterDecode.split("&");
            
            LinkedHashMap<String, String> store=new LinkedHashMap<String, String>();
            int pos=separated.length;
            for(int s=0;s<pos;s++)
            {
             
            	StringTokenizer tokenss = new StringTokenizer(separated[s], "=");
            	String first1=tokenss.nextToken();
            	String second2=tokenss.nextToken();
            	store.put(first1, second2);
            	Log.d("first1 ======>",first1);
             	Log.d("second2 ======>",second2);
            }
            
            Iterator iterator = store.keySet().iterator();
            while (iterator.hasNext()) 
            {
             
            	String key = (String) iterator.next();
            	token = store.get("TOKEN");
            	Ack=store.get("ACK");
            	PayerID=store.get("PAYERID");
            	EMAIL = store.get("EMAIL");
            	FIRSTNAME = store.get("FIRSTNAME") +"%20"+store.get("LASTNAME");
            	System.out.println("ACK   & PAYER ID ====>" +Ack+ " " +PayerID );
            	
            }
            
            System.out.println("GetExpressCheckoutDetails Response ====>" +afterDecode);
            if(afterDecode.contains("Success")){
            	
            	DoExCheckout();
            }
            
        } catch (ClientProtocolException e) {
            
            e.printStackTrace();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
	}

	
	private void DoExCheckout() {
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
        // Your URL
        HttpPost httppost = new HttpPost("https://api-3t.sandbox.paypal.com/nvp/");
        
        try {
        	
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // Your DATA
            nameValuePairs.add(new BasicNameValuePair("METHOD","DoExpressCheckoutPayment"));
            nameValuePairs.add(new BasicNameValuePair("USER","piyushpatel.ppp_api1.gmail.com"));
            nameValuePairs.add(new BasicNameValuePair("PWD","VKST6ZTMMYD3EB9K"));
            nameValuePairs.add(new BasicNameValuePair("SIGNATURE","ALQj17MJ62KdhsYevBehbKBMwq4aA-HCi5YlBTYQ.sd-s-Wk3Nf4Ys2L"));
            nameValuePairs.add(new BasicNameValuePair("VERSION","88"));
            nameValuePairs.add(new BasicNameValuePair("TOKEN",""+token));
            nameValuePairs.add(new BasicNameValuePair("PaymentAction","Sale"));
            nameValuePairs.add(new BasicNameValuePair("PAYMENTREQUEST_0_PAYMENTACTION","Sale"));
            nameValuePairs.add(new BasicNameValuePair("PAYMENTREQUEST_0_CURRENCYCODE","USD"));
            nameValuePairs.add(new BasicNameValuePair("PayerID",""+PayerID));
            nameValuePairs.add(new BasicNameValuePair("PaymentDetails","Completed"));
            nameValuePairs.add(new BasicNameValuePair("PAYMENTREQUEST_0_AMT",pricetouser));
            
            System.out.println("nameValuePairs in DoExCheckout=====>" +nameValuePairs.toString());
            
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response;
            response = httpclient.execute(httppost);
            resEntity = response.getEntity();
            String response_str = EntityUtils.toString(resEntity);
            
            String afterDecode = URLDecoder.decode(response_str, "UTF-8");
            
            System.out.println("DoExCheckout Response ====>" +afterDecode);
            String[] separated = afterDecode.split("&");
            
            LinkedHashMap<String, String> store=new LinkedHashMap<String, String>();
            int pos=separated.length;
            for(int s=0;s<pos;s++)
            {
             
            	StringTokenizer tokenss = new StringTokenizer(separated[s], "=");
            	String first1=tokenss.nextToken();
            	String second2=tokenss.nextToken();
            	store.put(first1, second2);
            	Log.d("first1 ======>",first1);
             	Log.d("second2 ======>",second2);
            }
            
            Iterator iterator = store.keySet().iterator();
            while (iterator.hasNext()) 
            {
            	String key = (String) iterator.next();
            	TRANSACTIONID = store.get("PAYMENTINFO_0_TRANSACTIONID");
            	amount = store.get("PAYMENTINFO_0_AMT");
            	StringTokenizer tokenss = new StringTokenizer(amount, ".");
            	finalamount = tokenss.nextToken();
            	System.out.println("========TRANSACTIONID========="+TRANSACTIONID+"=======amount====="+amount+"========finalamount========"+finalamount);
            }
            
            if(afterDecode.contains("Success"))
            {
                isPaymentSuccess = true;
            	/*OudragourApplication.oudraguerPurchaseDataBase.PurchaseInfoInsert(OudragourApplication.prefrences.get_user_emailid(), TRANSACTIONID, amount, StaticData.monthtobuy, "pending");
            	transactionURL=Config.transaction;
            	new Paypal_Transaction_Async(TRANSACTIONID,amount).execute();*/

            }/**/
            
        } catch (ClientProtocolException e) {
            
            e.printStackTrace();
        } catch (IOException e) {
           
            e.printStackTrace();
        }
	}
    
    public String getCurrDate()
 	{
    	String d = "yyyy-MM-dd hh:mm aaa";
    	java.util.Date c = Calendar.getInstance().getTime();
    	String time = (String) DateFormat.format(d, c);
    	return time;	
 	}

    private void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PaypalPayment.this.finish();
                dialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
    
    //=========================================Transation Service=====================================
    String parseMessage="Please wait",responseString,transactionURL;
	boolean isNetwork;
	JSONObject transactionObject,jsonObject;
	JSONArray transactionArray,jsonArray;
	
	 /*public class Paypal_Transaction_Async extends AsyncTask<Void, String, Void> //implements OnCancelListener
	 {
		 String status,message,french,transaction_id,amount;
		 
		public Paypal_Transaction_Async(String transaction_id, String amount) 
		{
			//super();
			this.transaction_id = transaction_id;
			this.amount = amount;
		}
		 
		 @Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			try 
			{
				
				isNetwork=OudragourApplication.connectivity.isNetworkAvailable();
				if(isNetwork == true)
				 {
					// Create a new HttpClient and Post Header
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(transactionURL);
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					
					nameValuePairs.add(new BasicNameValuePair("email",OudragourApplication.prefrences.get_user_emailid()));
					nameValuePairs.add(new BasicNameValuePair("transaction_id",transaction_id));
					nameValuePairs.add(new BasicNameValuePair(StaticData.daysormonth,StaticData.monthtobuy));
					nameValuePairs.add(new BasicNameValuePair("amount",amount));
					nameValuePairs.add(new BasicNameValuePair("device","android"));
					
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					// Execute HTTP Post Request
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String response = httpclient.execute(httppost, responseHandler);
					
					jsonObject=new JSONObject(response);
					jsonArray = jsonObject.getJSONArray("transaction");
					
					if(jsonArray.length()>0)
					{
						for(int i=0;i<jsonArray.length();i++)
						{
							transactionObject=jsonArray.getJSONObject(i);
							if(transactionObject.has("status"))
							{
								status=transactionObject.getString("status");
								message=transactionObject.getString("message");
								french=transactionObject.getString("french");
							}
						}
					}
				 }
				
			}
			catch(Exception e) {
				System.out.println("========Exception=========="+e);
			}
			return null;
		}
		


		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			*//*if(isNetwork == true)
			{
				if(FieldValidation.isEmpty(status))
				{
					if(status.equalsIgnoreCase("true"))
					{
						OudragourApplication.oudraguerPurchaseDataBase.PurchaseInfoUpdate(TRANSACTIONID);
						
						OudragourApplication.prefrences.StoreAccountType("yes");
						
						if(OudragourApplication.prefrences.getLocalLanguage().contains(getResources().getString(R.string.fr_FR)) || OudragourApplication.prefrences.getLocalLanguage().contains(getResources().getString(R.string.fr_CH)))
						{
							OudragourApplication.showToast.ShowMsg(french);
						}
						else
						{
							OudragourApplication.showToast.ShowMsg(message);
						}
						
						PaypalPayment.this.finish();
					}
					else
					{
						if(OudragourApplication.prefrences.getLocalLanguage().contains(getResources().getString(R.string.fr_FR)) || OudragourApplication.prefrences.getLocalLanguage().contains(getResources().getString(R.string.fr_CH)))
						{
							OudragourApplication.showToast.ShowMsg(french);
						}
						else
						{
							OudragourApplication.showToast.ShowMsg(message);
						}
					}
				}
			}
			else
			{
				OudragourApplication.showToast.ShowMsg(getResources().getString(R.string.connection));
			}*//*
		}

	 }*/
}
