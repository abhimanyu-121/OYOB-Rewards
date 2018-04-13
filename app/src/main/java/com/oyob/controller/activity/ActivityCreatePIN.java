package com.oyob.controller.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oyob.controller.R;
import com.oyob.controller.fragment.FragmentAAActivate0;
import com.oyob.controller.fragment.FragmentAACreatePIN;
import com.oyob.controller.fragment.FragmentACP0;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Narender Kumar on 2/27/2018.
 * For Prominent, Faridabad (India)
 * narender.kumar.nishad@gmail.com
 */

public class ActivityCreatePIN extends ActivityBase implements FragmentACP0.CreatePINZero {

    private static final String TAG = ActivityCreatePIN.class.getSimpleName();
    AppCompatImageView acp_aciv_back;
    String _web_Address, _membership_Number, _password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);

        acp_aciv_back = findViewById(R.id.acp_aciv_back);
        acp_aciv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (findViewById(R.id.acp_fl_root) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.acp_fl_root, FragmentACP0.newInstance(), FragmentAAActivate0.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public void zero(String webAddress, String membershipNumber, String password) {
        _web_Address = webAddress;
        _membership_Number = membershipNumber;
        _password = password;
        if (isNetworkConnected(context))
            userLogin(webAddress, membershipNumber, password);
        else
            showToastLong(context, getString(R.string.check_internet_connectivity));
    }

    public void userLogin(final String web_Address, final String membership_Number, final String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        dialog.show();

        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, AppConstants.USER_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                JSONObject obj = null;
                Log.d(TAG, "User Login onResponse: " + response);
                if (response.toString().contains("FAILED"))
                    showToastLong(context, getString(R.string.wrong_credentials_login));
                if (response != null) {
                    try {
                        obj = new JSONObject(response);
                        if (obj.has("client_id")) {
                            showToastShort(context, obj.getString("registered"));
                            sessionManager.updatePerticularField(SessionManager.CLIENT_ID, obj.getString("client_id"));
                            sessionManager.updatePerticularField(SessionManager.USER_ID, obj.getString("user_id"));
                            sessionManager.updateUserName(membership_Number);
                            sessionManager.updatePerticularField(SessionManager.PASSWORD, password);
                        }

                        if (obj.has("client_id")) {
                            sessionManager.updatePerticularField(SessionManager.CLIENT_ID, obj.getString("client_id"));
                            sessionManager.updatePerticularField(SessionManager.USER_ID, obj.getString("user_id"));
                            sessionManager.updateUserName(membership_Number);
                            sessionManager.updatePerticularField(SessionManager.PASSWORD, password);
                            Log.d(TAG, "Registered" + obj.getString("registered"));

                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.acp_fl_root, FragmentAACreatePIN.newInstance(),
                                    FragmentAACreatePIN.class.getSimpleName()).addToBackStack(FragmentAACreatePIN.class.getSimpleName()).commit();
                        } else {
                            showToastLong(context, getString(R.string.wrong_credentials_login));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToastLong(context, getString(R.string.wrong_credentials_login));
                }
                Log.d(TAG, "" + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(context, "Seems like you have entered wrong credentials or not registered");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uname", membership_Number);
                params.put("pwd", password);
                params.put("sub", web_Address);
                params.put("response_type", "json");
                params.put("page", "");
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
            super.onBackPressed();
            return;
        }
    }
}