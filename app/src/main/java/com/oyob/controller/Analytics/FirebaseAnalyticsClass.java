package com.oyob.controller.Analytics;

import android.app.Application;
import android.os.Bundle;

import com.android.volley.VolleyLog;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by 121 on 10/7/2017.
 */

public class FirebaseAnalyticsClass  extends Application {

    private static FirebaseAnalytics mFirebaseAnalytics;
    private static Bundle bundle;
    private static FirebaseAnalyticsClass application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        VolleyLog.DEBUG = true;
    }

    public static FirebaseAnalyticsClass getInstance() {
        return application;
    }

    public static FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    //set the bundle value for fireBase analytics
    public static Bundle getBundle(String id, String catagory, String itemName)
    {
        bundle= new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY,catagory);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        return bundle;
    }

}