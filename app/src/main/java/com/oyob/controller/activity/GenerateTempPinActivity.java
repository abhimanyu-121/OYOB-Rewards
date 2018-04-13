package com.oyob.controller.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oyob.controller.R;
import com.oyob.controller.networkCall.PostAsyncTask;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.Mylogger;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * Created by Ramasamy on 9/6/2017.
 */

public class GenerateTempPinActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = GenerateTempPinActivity.class.getSimpleName();
    EditText editText_one, editText_two, editText_three, editText_four, editText_five, editText_six;
    String one, two, three, four, five, six;
    SessionManager sessionManager;
    SharedPreferences preference;
    Context context;
    static boolean isValidOtp;
    TextView insertPin;
    TextView headerActivate;
    Button button;
    PostAsyncTask postAsyncTask;
    String stringResponse;
    String URLString;
    static String TempPinCheck;
    String androidId;
    Date currentTime;
    String userId = "";
    String errorcode = "";
    DateFormat df;
    String reportDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_temp_pin);
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Mylogger.getInstance().Logit("deviceID", androidId);

        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currentTime = Calendar.getInstance().getTime();
        reportDate = df.format(currentTime);
        insertPin = findViewById(R.id.insert_generate_pin);
        headerActivate = findViewById(R.id.al_tv_login);

        button = findViewById(R.id.loginButton);
        button.setOnClickListener(this);

        editText_one = findViewById(R.id.editText1);
        editText_two = findViewById(R.id.editText2);
        editText_three = findViewById(R.id.editText3);
        editText_four = findViewById(R.id.editText4);

        editText_one.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_one.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_two.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_two.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_three.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_three.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_four.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_four.setTransformationMethod(new MyPasswordTransformationMethod());

        one = editText_one.getText().toString();
        two = editText_two.getText().toString();
        three = editText_three.getText().toString();
        four = editText_four.getText().toString();
        five = editText_five.getText().toString();
        six = editText_six.getText().toString();
        context = GenerateTempPinActivity.this;
        sessionManager = new SessionManager(context);
        preference = context.getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        isValidOtp = context.getSharedPreferences("PREFERENCES", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (!isValidOtp && !TextUtils.isEmpty(SharedPreferenceHelper.getPref("client_id", this))) {
            Intent intent = new Intent(GenerateTempPinActivity.this, ActivityDashboard.class);
            startActivity(intent);
        }

        editText_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_two.requestFocus();
                }
            }
        });

        editText_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_three.requestFocus();
                }
            }
        });

        editText_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_four.requestFocus();
                }
            }
        });
        editText_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_five.requestFocus();
                }
            }
        });

        editText_five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_six.requestFocus();
                }
            }
        });

        editText_six.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    hideKeyboardwithoutPopulate(GenerateTempPinActivity.this);
                }
            }
        });

        editText_two.setOnKeyListener(new View.OnKeyListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String two = editText_two.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && two.trim().length() == 0) {
                    editText_one.setText("");
                    editText_one.requestFocus();
                    System.out.println("text delete in  editText2" + editText_two);
                }
                return false;
            }
        });

        editText_three.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String three = editText_three.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && three.trim().length() == 0) {
                    editText_two.setText("");
                    editText_two.requestFocus();
                    System.out.println("text delete in  editText3" + editText_three);
                }
                return false;
            }
        });

        editText_four.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String four = editText_four.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && four.trim().length() == 0) {
                    editText_three.setText("");
                    editText_three.requestFocus();
                    System.out.println("text delete in  editText3" + editText_four);
                }
                return false;
            }
        });
        editText_five.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String five = editText_five.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && five.trim().length() == 0) {
                    editText_four.setText("");
                    editText_four.requestFocus();
                    System.out.println("text delete in  editText3" + editText_four);
                }
                return false;
            }
        });
        editText_six.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String six = editText_six.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && six.trim().length() == 0) {
                    editText_five.setText("");
                    editText_five.requestFocus();
                    System.out.println("text delete in  editText3" + editText_four);
                }
                return false;
            }
        });
        ImageView btn_back = (ImageView) findViewById(R.id.al_iv_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        editText_one.requestFocus();

    }

    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        }
    }

    public class MyPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    @Override
    public void onClick(View v) {

        TempPinCheck = editText_one.getText().toString() + editText_two.getText().toString() + editText_three.getText().toString() +
                editText_four.getText().toString() + editText_five.getText().toString() + editText_six.getText().toString();
        /*String secret = "123456";*/
        String secret = "2C9tYC";

        if (TempPinCheck != null && TempPinCheck.length() == 6) {
            SharedPreferenceHelper.set(getApplicationContext(), "isvalid", TempPinCheck);
            ApiCall(TempPinCheck);
             /*  APILink.getApiLinkInstance().TempPinValidation(GenerateTempPinActivity.this, TempPinCheck);
               if(!isSuccessApiCall)
                {
                    postAsyncTask = new PostAsyncTask(secret, TEMP_PIN_VALIDATION, GenerateTempPinActivity.this);
                }
            Intent intent = new Intent(GenerateTempPinActivity.this, PinActivity.class);
            startActivity(intent);*/
        } else if ((editText_one.getText().toString() + editText_two.getText().toString() + editText_three.getText().toString() +
                editText_four.getText().toString() + editText_five.getText().toString() + editText_six.getText().toString()).length() == 6) {
            showAlert("Please enter valid PIN");
            //Toast.makeText(getApplicationContext(), "Please enter valid PIN", Toast.LENGTH_SHORT).show();
        } else {
            showAlert("Please enter valid PIN");
            // Toast.makeText(getApplicationContext(), "Please enter valid PIN", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


  /*  @Override
    public void onServiceComplete(Object response, int eventType) {
        if (eventType == 1) {
            if (response != null) {
                String data = "eyJkYXRhIjp7InVzZXJfaWQiOiI2NzIxNzk5IiwiYWN0aXZhdGlvbl9jb2RlIjoiZmQyMzFkNDg5MGI2YjNlMzgyYzcwZWQ2N2RlNTYzOTUifSwic3RhdHVzIjp7IjIwMCI6InN1Y2Nlc3MifX0=.vW9UOVdfGrcbgTkAO3Rs2JSi1Sm14J3LSJBZvEVpMcg=";
                for (String retval : data.split(".")) {
                    System.out.println(retval + "dot data");
                }
                String converted = Base64.encodeToString(data.toString().getBytes(), Base64.DEFAULT);
                System.out.println(converted);
                Intent intent = new Intent(GenerateTempPinActivity.this, PinActivity.class);
                startActivity(intent);
            }
        }
    }*/

    /*public void apiCall() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            *//*String secret = "2C9tYC";*//*
            jsonBody.put("activation_code", SharedPreferenceHelper.getPref("isvalid",getApplicationContext()));
            final String requestBody = jsonBody.toString();

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, TEMP_PIN_VALIDATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                          System.out.println(response.toString());
                           *//* if (response != null) {
                                for (String retval : response.split(".")) {
                                    System.out.println(retval + "dot data");
                                }
                                String converted = Base64.encodeToString(response.getBytes(), Base64.DEFAULT);
                                System.out.println(converted+"converted");
                              *//**//*  Log.i("VOLLEY", response);
                                Intent intent = new Intent(GenerateTempPinActivity.this, PinActivity.class);
                                startActivity(intent);*//**//*
                            }*//*
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

              *//*  @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }*//*

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString= "";
                    if (response != null) {
                        responseString = String.valueOf(response.data);
                        *//*responseString = String.valueOf(response.data);*//*
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));

                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
    public void ApiCall(final String param) {
        // FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "Click", "TempPinActivation"));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Activating...");
        pDialog.show();
        Mylogger.getInstance().Logit("BASEURL", AppConstants.TEMP_PIN_VALIDATION);
        StringRequest postReq = new StringRequest
                (Request.Method.POST, AppConstants.TEMP_PIN_VALIDATION, new Response.Listener<String>() {
                    JSONObject jsonObject;

                    @Override
                    public void onResponse(String response) {
                        Mylogger.getInstance().Logit("BASEURLRES", response);
                        Log.d(TAG, response);
                        pDialog.hide();
                        JSONObject jsonObject1 = null;
                        String userId = "";
                        try {
                            jsonObject = new JSONObject(response);

                            String name = jsonObject.getString("token");
                            System.out.println(name + "token");
                            Log.d(TAG, "onResponse: " + name);
                            StringTokenizer tokens = new StringTokenizer(name, ".");

                            String first = tokens.nextToken();
                            System.out.println(first + "first");
                            Log.d(TAG, "onResponse: " + first);
                            byte[] data = android.util.Base64.decode(first, android.util.Base64.DEFAULT);
                            try {
                                String text = new String(data, "UTF-8");
                                JSONObject jObject = new JSONObject(text); // json
                                JSONObject data1 = jObject.getJSONObject("data"); // get data object
                                userId = data1.getString("user_id"); // get the name from data.
                                if (userId != null) {
                                    new LongOperation().execute();
                                    sessionManager.updateIsFirstRun(false);
                                    preference.edit().putBoolean("isFirstRun", false).apply();
                                    Intent intent = new Intent(GenerateTempPinActivity.this, ActivityActivate.class);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("Bearer", name);
                                    startActivity(intent);
                                }
                                Log.d(TAG, "onResponse: " + userId + "userId");

                      /*  JSONObject errormessage = jsonObject.getJSONObject("status");
                        errorcode=errormessage.getString("404");*/

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            showAlert("Please enter valid PIN");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("activation_code", param);
                return params;
            }
        };
        postReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(postReq);
    }

    class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Mylogger.getInstance().Logit("BASEURL", AppConstants.UserACtion);
            putdetails(userId, userId, "android", androidId, "click", null, null, "TempPinActivation", reportDate, "Success", "RedirectingPinScreen");
            return "Executed";
        }
    }

    public void putdetails(final String cid, final String uid, final String platform, final String device_id, final String action, final String category_id,
                           final String product_id, final String page, final String date_time, final String status, final String message) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest putRequest = new StringRequest(Request.Method.PUT, AppConstants.UserACtion,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("PutDetails", response);
                        Mylogger.getInstance().Logit("BASEURLRES", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cid", cid);
                params.put("user_id", uid);
                params.put("Android", platform);
                params.put("device_id", device_id);
                params.put("Click", action);

                params.put("category_id", category_id);
                params.put("product_id", product_id);
                params.put("product_id", page);
                params.put("date_time", date_time);
                params.put("status", status);
                params.put("message", message);
                Mylogger.getInstance().Logit("params", "" + params.toString());
                return params;
            }
        };
        requestQueue.add(putRequest);
    }

    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                GenerateTempPinActivity.this).create();
        // alertDialog.setTitle("Alert");
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
