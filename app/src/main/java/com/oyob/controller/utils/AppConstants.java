package com.oyob.controller.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oyob.controller.activity.ActivityDashboard;
import com.oyob.controller.model.ModelClass;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppConstants {

    public static String SERVER_ROOT = "https://www.myrewards.com.au/newapp/";
    public static final String POPULAR_OFFERS = SERVER_ROOT + "get_program_popular_offers.php";
    public static final String POPULAR_OFFERS_IMAGE_PREFIX = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/Popular_offer/";
    public static final String TEMP_PIN_VALIDATION = SERVER_ROOT + "vic_activation.php";
    public static final String PIN_CREATE = SERVER_ROOT + "vic_confirmPassword.php";
    public static final String USER_VIC_LOGIN = SERVER_ROOT + "vic_login.php";
    public static final String USER_LOGIN = SERVER_ROOT + "login.php";
    public static final String GET_USER = SERVER_ROOT + "get_user1.php";
    public static final String SAVE_DATA = SERVER_ROOT + "save_udata.php";
    public static final String GET_INTEREST = SERVER_ROOT + "get_interests.php";
    public static final String GET_COUNTRIES = SERVER_ROOT + "country_list.php?response_type=json";

    public static final String UserACtion = AppConstants.SERVER_ROOT + "put_user_actions.php";
    public static final String redeemAction = AppConstants.SERVER_ROOT + "redeem_tracker.php";

    public static final String CART_ITEMS = SERVER_ROOT
            + "cart_items.php?";

    public static String UNABLETOESTABLISHCONNECTION_URL = null;

    public static ArrayList<ModelClass> offerList = new ArrayList<>();
    public static ArrayList<ModelClass> favList = new ArrayList<>();

    public static void getCardItemCount(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        final String cart_id = sessionManager.getParticularField(SessionManager.CART_ID);
        final String userId = sessionManager.getParticularField(SessionManager.USER_ID);
        String countURL = AppConstants.CART_ITEMS + "uid=" + userId + "&cart_id=" + cart_id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest postReq = new StringRequest(Request.Method.GET, countURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.has("200")) {
                            ActivityDashboard.cartCount = jsonObject.getString("cart_items");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
            }

        });

        requestQueue.add(postReq);
    }

}
