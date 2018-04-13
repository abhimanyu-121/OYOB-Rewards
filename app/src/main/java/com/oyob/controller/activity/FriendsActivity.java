package com.oyob.controller.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.oyob.controller.R;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.AppConstants;


import org.json.JSONObject;


/**
 * Created by Ramasamy on 9/2/2017.
 */

public class FriendsActivity extends AppCompatActivity {

    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String bunbleFriends = null, Url, cid, uname, country = "";
    int PAGELIMIT = 30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        bunbleFriends = bundle.getString("client_id");

        setContentView(R.layout.friends_fragment);
        cid = SharedPreferenceHelper.getPref("client_id", FriendsActivity.this);
        uname = SharedPreferenceHelper.getPref("uname", FriendsActivity.this);
        country = SharedPreferenceHelper.getPref("country", FriendsActivity.this);
        Url = AppConstants.SERVER_ROOT + "search_cat.php?cat_id=554&cid=" + cid + "&country=" + country + "&start=0&limit=" + PAGELIMIT;

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        backArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.friends_toolbar);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
        getFaviorites();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getFaviorites() {
        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);
        final ProgressDialog pDialog = new ProgressDialog(FriendsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        Log.d("onResponse", "onResponse: " + response);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        pDialog.hide();
                    }
                });

        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }

}
