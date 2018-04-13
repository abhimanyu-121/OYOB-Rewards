package com.oyob.controller.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.EditText;


import com.oyob.controller.model.User;
import com.oyob.controller.model.WebImageCache;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;



public class Utility {


	public static int screenHeight;
	public static int screenWidth;
	public static String COOKIE=null;
	public static User user = null;
	public static Double mLat;	
	public static Double mLng;
	/*public static ArrayList<Product> productsListAroundMe = null;*/
	public static final String FETCH_DIRECTION_UP = "up";
	public static NetworkInfo netInfo;
	
	public static int fav_position_var;
	
	public static String user_Name;
	public static String user_Password;
	public static String user_website;
	
	public static Typeface font_bold,font_reg;
	
	public static final String FINISHED_STATUS = "ProductDetails"; 
	
	public static String AroundMeScreen = "null";
	public static String hotOffersScreen = "null";
	public static String FavouritesScreen = "null";
	public static String DailyDealsScreen = "null";
	public static String ResultScreen = "null";
	public static String ticketoffersScreen="null";
	//public static String hotOffersScreen = "null";
	
	//this is for notification update
	
	public static AlarmManager alarmMgr0=null;
	public static PendingIntent pendingIntent0=null;
	
	public static int screenheight;
	public static int screenwidth;
	public static Typeface md_light;
	public static Typeface md_regular;
	public static Typeface md_semibold_heading;
	public static Typeface lt_black;
	public static Typeface lt_regular;
	
	/*//New
	public static ProductDetails singleProductInfo;

	//public static int clickedPos;

	public static int clickedPos_PRODUCT_ID;
	public static String clickedPos_PRODUCT_NAME;
	public static String clickedPos_PRODUCT_OFFER;
	public static int selectionCount = 0;
	public static String OfferDoneStr = null;
	
	public static void showMessage(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}*/

	public static boolean isOnline(ConnectivityManager cm) {
        /*ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);*/
        netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        return true;
    }
    return false;
}

	public static boolean copyPasteMethod(View v,int length)
	{
		
		boolean returnValue=false;
		if(length>25)
		{
			returnValue=true;
		}
		
		return returnValue;
		
	}

	/*public static void setNotificationReceiver(Context context) {
		 alarmMgr0 = (AlarmManager)context. getSystemService(Context.ALARM_SERVICE);
		// Create pending intent & register it to your alarm notifier class
		Intent intent0 = new Intent(context, NoticeBoardReceiver.class);
		pendingIntent0 = PendingIntent.getBroadcast(context, 0, intent0, PendingIntent.FLAG_UPDATE_CURRENT);
		// set that timer as a RTC Wakeup to alarm manager object
		alarmMgr0.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), (3*60*60*1000), pendingIntent0);
	}
	public static void cancelNotificationReceiver()
	{
		if(alarmMgr0!=null && pendingIntent0!=null)
		alarmMgr0.cancel(pendingIntent0);
	}*/
	
	public static Drawable getAssetImage(Context context, String filename) throws IOException 
	{
		try {
		    AssetManager assets = context.getResources().getAssets();
		    InputStream buffer = new BufferedInputStream((assets.open("HariDrawable/" + filename + ".png")));
		    Bitmap bitmap = BitmapFactory.decodeStream(buffer);
		    return new BitmapDrawable(context.getResources(), bitmap);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Intent EmailIntent(Context context, String address,
			String subject, String body, String cc) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
		intent.putExtra(Intent.EXTRA_TEXT, body);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_CC, cc);
		intent.setType("message/rfc822");
		return intent;
	}
	
	public static void deleteCmpleteCacheData(Context context) {
		try {
			deleteCache(context);
			WebImageCache cache = new WebImageCache(context);
			cache.clear();
		} catch (Exception e) {
			if (e != null) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static boolean isEmailValid(CharSequence email) {
		return !isEmptyOrNot(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	public static boolean isEditTextNull(EditText editText) {
		if(!isEmptyOrNot(editText.getText()) && editText.getText().length()>1) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmptyOrNot(CharSequence str) {
		if (str == null || str.length() == 0 || str.equals("null") || str.equals(" ") || str.equals("none") || str.equals("0") || str.equals(""))
			return true;
		else
			return false;
	}
}
