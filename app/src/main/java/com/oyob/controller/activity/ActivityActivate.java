package com.oyob.controller.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.oyob.controller.R;
import com.oyob.controller.fragment.FragmentAAActivate0;
import com.oyob.controller.fragment.FragmentAAActivate1;
import com.oyob.controller.fragment.FragmentAAActivate2;
import com.oyob.controller.fragment.FragmentAACreatePIN;
import com.oyob.controller.fragment.FragmentAATandC;
import com.oyob.controller.networkCall.PDRestClient;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.oyob.controller.utils.AppConstants.SAVE_DATA;


public class ActivityActivate extends ActivityBase implements FragmentAAActivate0.ActivateZero,
        FragmentAAActivate1.ActivateOne, FragmentAAActivate2.ActivateTwo,
        FragmentAATandC.TandC, View.OnClickListener {

    private static final String TAG = ActivityActivate.class.getSimpleName();
    static boolean isLoginOtp;
    SharedPreferences preference;
    String androidId;
    ArrayList<String> _interest_id = new ArrayList<>();
    AppCompatImageView aa_aciv_back;
    String _web_Address, _membership_Number, _password, _f_n, _l_n, _dob, _news_letter, _country, _state,
            _passcode, _email, _mobile, _pin, _userId, bearer;

    Date currentTime;
    DateFormat df;
    String reportDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        currentTime = Calendar.getInstance().getTime();
        reportDate = df.format(currentTime);

        preference = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        isLoginOtp = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE).getBoolean("isFirstRun2", true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            _userId = bundle.getString("userId");
            sessionManager.updateUserId(_userId);
            SharedPreferenceHelper.set(this, "userId", _userId);
            bearer = bundle.getString("Bearer");
            sessionManager.updateBearer(bearer);
        }

        aa_aciv_back = findViewById(R.id.aa_aciv_back);
        aa_aciv_back.setOnClickListener(this);
        if (findViewById(R.id.aa_fl_root) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.aa_fl_root, FragmentAAActivate0.newInstance(), FragmentAAActivate0.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public void zero(String webAddress, String membershipNumber, String password) {
        _web_Address = webAddress;
        //membership_number = uid/userId
        _membership_Number = membershipNumber;
        _password = password;

        Map<String, String> params = new HashMap<String, String>();
        params.put("uname", "atwork_app_test");
        params.put("pwd", "@work123");
        params.put("sub", "www.atwork.com.au");
        params.put("response_type", "json");
        params.put("page", "");
        if (isNetworkConnected(context))
            userLogin(webAddress, membershipNumber, password);
        else
            showToastLong(context, getString(R.string.check_internet_connectivity));
    }

    @Override
    public void one(String firstName, String lastName, String dob, ArrayList<String> interest_id, String newsletter) {
        _f_n = firstName;
        _l_n = lastName;
        _dob = dob;
        _interest_id = interest_id;
        _news_letter = newsletter;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.aa_fl_root, FragmentAAActivate2.newInstance(),
                FragmentAAActivate2.class.getSimpleName()).addToBackStack(FragmentAAActivate2.class.getSimpleName()).commit();
    }

    @Override
    public void two(String country, String state, String postcode, String email, String mobile) {
        _country = country;
        _state = state;
        _passcode = postcode;
        _email = email;
        _mobile = mobile;
        if (isNetworkConnected(context))
            save();
        else
            showToastShort(context, getString(R.string.check_internet_connectivity));
    }

    @Override
    public void onTermAndCondition() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.aa_fl_root, FragmentAATandC.newInstance(),
                FragmentAATandC.class.getSimpleName()).addToBackStack(FragmentAATandC.class.getSimpleName()).commit();
    }

    public void userLogin(final String web_Address, final String membership_Number, final String Pin) {
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
                            sessionManager.updatePerticularField(SessionManager.PASSWORD, Pin);
                        }

                        if (obj.getString("registered").equalsIgnoreCase("1")) {
                            if (!TextUtils.isEmpty(sessionManager.getParticularField(SessionManager.PIN))) {
                                startActivity(new Intent(context, ActivityLogin.class));
                                Log.d(TAG, "Registered" + obj.getString("registered"));
                                finish();
                            } else {
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.aa_fl_root, FragmentAACreatePIN.newInstance(),
                                        FragmentAACreatePIN.class.getSimpleName()).commit();
                            }
                        } else if (obj.getString("registered").equalsIgnoreCase("0")) {
                            sessionManager.updatePerticularField(SessionManager.CLIENT_ID, obj.getString("client_id"));
                            sessionManager.updatePerticularField(SessionManager.USER_ID, obj.getString("user_id"));
                            sessionManager.updateUserName(membership_Number);
                            sessionManager.updatePassword(Pin);
                            sessionManager.updatePerticularField(SessionManager.PASSWORD, Pin);
                            Log.d(TAG, "Registered" + obj.getString("registered"));

                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.aa_fl_root, FragmentAAActivate1.newInstance(),
                                    FragmentAAActivate1.class.getSimpleName()).addToBackStack(FragmentAAActivate1.class.getSimpleName()).commit();
                        } else {
                            showToastLong(context, getString(R.string.wrong_credentials_login));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToastLong(context, getString(R.string.it_seems_you_are_not_registerd));
                }
                Log.d(TAG, "" + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(context, getString(R.string.wrong_credentials_login));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uname", membership_Number);
                //params.put("pwd", Pin);
                params.put("sub", web_Address);
                params.put("response_type", "json");
                params.put("page", "activate");
                return params;
            }
        };
/*        JsonObjectRequest postReq = new JsonObjectRequest(AppConstants.USER_LOGIN,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        Log.d(TAG, "User Login onResponse: " + response);
                        showToastLong(context, response.toString());
                        client = null;
                        if (response != null) {
                            try {
                                if (response.has("client_id")) {
                                    showToastShort(context, response.getString("registered"));
                                    sessionManager.updatePerticularField(SessionManager.CLIENT_ID, response.getString("client_id"));
                                    sessionManager.updatePerticularField(SessionManager.USER_ID, response.getString("user_id"));
                                    sessionManager.updateUserName(_membership_Number);
                                    sessionManager.updatePerticularField(SessionManager.PASSWORD, Pin);
                                }
                                Log.d(TAG, "" + response.getString("registered"));
                                if (response.getString("registered").equalsIgnoreCase("0")) {
                                    startActivity(new Intent(context, ActivityLogin.class));
                                    finish();
                                } else if (response.getString("registered").equalsIgnoreCase("1")) {
                                    sessionManager.updatePerticularField(SessionManager.CLIENT_ID, response.getString("client_id"));
                                    sessionManager.updatePerticularField(SessionManager.USER_ID, response.getString("user_id"));
                                    sessionManager.updateUserName(_membership_Number);
                                    sessionManager.updatePerticularField(SessionManager.PASSWORD, Pin);
                                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.add(R.id.aa_fl_root, FragmentAAActivate1.newInstance(),
                                            FragmentAAActivate1.class.getSimpleName()).addToBackStack(FragmentAAActivate1.class.getSimpleName()).commit();
                                } else {
                                    showToastLong(context, getString(R.string.failed_to_login));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            showToastLong(context, getString(R.string.it_seems_you_are_not_registerd));
                        }
                        Log.d(TAG, "" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };*/
        requestQueue.add(request);
    }

    private void save() {
        if (_interest_id.size() > 0) {
            final RequestParams params = new RequestParams();
            params.put("uid", sessionManager.getParticularField(SessionManager.USER_ID));
            params.put("pwd", _password);
            params.put("fname", _f_n);
            params.put("lname", _l_n);
            params.put("dob", _dob);
            params.put("interests", _interest_id);
            params.put("newsletter", _news_letter);
            params.put("country", _country);
            params.put("state", _state);
            params.put("pcode", _passcode);
            params.put("email", _email);
            params.put("mobile", _mobile);
            PDRestClient.post(context, SAVE_DATA, params, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    dialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    super.onSuccess(statusCode, headers, obj);
                    if (obj.has("status")) {
                        try {
                            if (obj.getString("msg").equalsIgnoreCase("success")) {
                                showToastShort(context, obj.getString("msg"));
                                sessionManager.updateCountry(_country);
                                sessionManager.updateState(_state);
                                sessionManager.updateFirstName(_f_n);
                                sessionManager.updateLastName(_l_n);
                                sessionManager.updateDOB(_dob);
                                sessionManager.updateEmail(_email);
                                sessionManager.updateMobile(_mobile);
                                sessionManager.updatePasscode(_passcode);
                                sessionManager.updateNewsletter(_news_letter);
                                sessionManager.updateInterests(_interest_id.toString());
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.add(R.id.aa_fl_root, FragmentAACreatePIN.newInstance(),
                                        FragmentAACreatePIN.class.getSimpleName()).addToBackStack(FragmentAACreatePIN.class.getSimpleName()).commit();
                            } else if (obj.getString("status").equalsIgnoreCase("400")) {
                                showToastShort(context, obj.getString("msg"));
                            } else if (obj.getString("status").equalsIgnoreCase("304")) {
                                showToastLong(context, getString(R.string.failed_to_save_data));
                                showToastShort(context, obj.getString("msg"));
                            } else {
                                showToastShort(context, obj.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToastLong(context, getString(R.string.failed_to_get_server_Response));
                    }
                    Log.d(TAG, "" + obj);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    showToastShort(context, PDRestClient.getHTTPErrorMessage(statusCode));
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(context, "Please select interest", Toast.LENGTH_LONG).show();
        }
    }

    private void saveDataToServer() {
        dialog.show();
        final MediaType MEDIA_TYPE =
                MediaType.parse("application/json");
        final OkHttpClient client = new OkHttpClient();
        JSONObject params = new JSONObject();
        try {
            params.put("uid", sessionManager.getParticularField(SessionManager.USER_ID));
            params.put("pwd", _password);
            params.put("fname", _f_n);
            params.put("lname", _l_n);
            params.put("dob", _dob);
            params.put("interests", _interest_id);
            params.put("newsletter", _news_letter);
            params.put("country", _country);
            params.put("state", _state);
            params.put("pcode", _passcode);
            params.put("email", _email);
            params.put("mobile", _mobile);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                params.toString());

        final Request request = new Request.Builder()
                .url(SAVE_DATA)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                dialog.dismiss();
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response okResponse) throws IOException {
                dialog.dismiss();

                String response = okResponse.body().toString();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Save Data onResponse: " + response);
                if (obj.has("status")) {
                    try {
                        if (obj.getString("msg").equalsIgnoreCase("success")) {
                            showToastShort(context, obj.getString("data saved"));
                            sessionManager.updateCountry(_country);
                            sessionManager.updateState(_state);
                            sessionManager.updateFirstName(_f_n);
                            sessionManager.updateLastName(_l_n);
                            sessionManager.updateLastName(_dob);
                            sessionManager.updateEmail(_email);
                            sessionManager.updateMobile(_mobile);
                            sessionManager.updatePasscode(_passcode);
                            sessionManager.updateNewsletter(_news_letter);
                            sessionManager.updateInterests(_interest_id.toString());
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.aa_fl_root, FragmentAACreatePIN.newInstance(),
                                    FragmentAACreatePIN.class.getSimpleName()).addToBackStack(FragmentAACreatePIN.class.getSimpleName()).commit();
                        } else if (obj.getString("status").equalsIgnoreCase("400")) {
                            showToastShort(context, obj.getString("msg"));
                        } else if (obj.getString("status").equalsIgnoreCase("304")) {
                            showToastLong(context, getString(R.string.failed_to_save_data));
                            showToastShort(context, obj.getString("msg"));
                        } else {
                            showToastShort(context, obj.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToastLong(context, getString(R.string.failed_to_get_server_Response));
                }
                Log.d(TAG, "" + response);
            }
        });
    }

    public void saveData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        dialog.show();
        JSONObject params = new JSONObject();
        try {
            params.put("uid", sessionManager.getParticularField(SessionManager.USER_ID));
            params.put("pwd", _password);
            params.put("fname", _f_n);
            params.put("lname", _l_n);
            params.put("dob", _dob);
            params.put("interests", _interest_id);
            params.put("newsletter", _news_letter);
            params.put("country", _country);
            params.put("state", _state);
            params.put("pcode", _passcode);
            params.put("email", _email);
            params.put("mobile", _mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(SAVE_DATA, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.d(TAG, "Save Data onResponse: " + response);
                showToastLong(context, response.toString());
                if (response.has("status")) {
                    try {
                        if (response.getString("msg").equalsIgnoreCase("success")) {
                            showToastShort(context, response.getString("data saved"));
                            sessionManager.updateCountry(_country);
                            sessionManager.updateState(_state);
                            sessionManager.updateFirstName(_f_n);
                            sessionManager.updateLastName(_l_n);
                            sessionManager.updateLastName(_dob);
                            sessionManager.updateEmail(_email);
                            sessionManager.updateMobile(_mobile);
                            sessionManager.updatePasscode(_passcode);
                            sessionManager.updateNewsletter(_news_letter);
                            sessionManager.updateInterests(_interest_id.toString());
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.aa_fl_root, FragmentAACreatePIN.newInstance(),
                                    FragmentAACreatePIN.class.getSimpleName()).addToBackStack(FragmentAACreatePIN.class.getSimpleName()).commit();
                        } else if (response.getString("status").equalsIgnoreCase("400")) {
                            showToastShort(context, response.getString("msg"));
                        } else if (response.getString("status").equalsIgnoreCase("304")) {
                            showToastLong(context, getString(R.string.failed_to_save_data));
                            showToastShort(context, response.getString("msg"));
                        } else {
                            showToastShort(context, response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToastLong(context, getString(R.string.failed_to_get_server_Response));
                }
                Log.d(TAG, "" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        });
        StringRequest postReq = new StringRequest(com.android.volley.Request.Method.POST, SAVE_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "Save Data onResponse: " + response);
                        showToastLong(context, response.toString());
                        if (obj.has("status")) {
                            try {
                                if (obj.getString("msg").equalsIgnoreCase("success")) {
                                    showToastShort(context, obj.getString("data saved"));
                                    sessionManager.updateCountry(_country);
                                    sessionManager.updateState(_state);
                                    sessionManager.updateFirstName(_f_n);
                                    sessionManager.updateLastName(_l_n);
                                    sessionManager.updateLastName(_dob);
                                    sessionManager.updateEmail(_email);
                                    sessionManager.updateMobile(_mobile);
                                    sessionManager.updatePasscode(_passcode);
                                    sessionManager.updateNewsletter(_news_letter);
                                    sessionManager.updateInterests(_interest_id.toString());
                                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.add(R.id.aa_fl_root, FragmentAACreatePIN.newInstance(),
                                            FragmentAACreatePIN.class.getSimpleName()).addToBackStack(FragmentAACreatePIN.class.getSimpleName()).commit();
                                } else if (obj.getString("status").equalsIgnoreCase("400")) {
                                    showToastShort(context, obj.getString("msg"));
                                } else if (obj.getString("status").equalsIgnoreCase("304")) {
                                    showToastLong(context, getString(R.string.failed_to_save_data));
                                    showToastShort(context, obj.getString("msg"));
                                } else {
                                    showToastShort(context, obj.getString("msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            showToastLong(context, getString(R.string.failed_to_get_server_Response));
                        }
                        Log.d(TAG, "" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", sessionManager.getParticularField(SessionManager.USER_ID));
                params.put("pwd", _password);
                params.put("fname", _f_n);
                params.put("lname", _l_n);
                params.put("dob", _dob);
                params.put("newsletter", _news_letter);
                params.put("country", _country);
                params.put("state", _state);
                params.put("pcode", _passcode);
                params.put("email", _email);
                params.put("mobile", _mobile);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void activateUser() {

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

    private void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }
}