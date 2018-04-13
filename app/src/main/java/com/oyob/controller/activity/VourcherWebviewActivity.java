package com.oyob.controller.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oyob.controller.R;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.Mylogger;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VourcherWebviewActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private Button btn_merchant_reedem_get;
    private String cid, pid, user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vourcher_webview);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView =  findViewById(R.id.webView);
        progressBar =  findViewById(R.id.progressBar);
        btn_merchant_reedem_get =  toolbar.findViewById(R.id.btn_merchant_reedem_get);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cid = getIntent().getStringExtra("cid");
        pid = getIntent().getStringExtra("pid");
        user_id = getIntent().getStringExtra("user_id");

        //String url = "http://myrewards.tk/newapp/get_voucher.php?uid=6799719&cid=1963&mid=1026340&pid=1031232";
        getOfferDetail(getIntent().getStringExtra("voucherAPI"));
        //  webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);

        btn_merchant_reedem_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redeemed();
            }
        });
    }

    public void Redeemed() {
        String url = AppConstants.SERVER_ROOT + "redeemed.php";
        Mylogger.getInstance().Logit("getOfferDetail", url);
        RequestQueue queue = Volley.newRequestQueue(VourcherWebviewActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Mylogger.getInstance().Logit("vourvherdata", response);
                        showAlert(response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Mylogger.getInstance().Logit("getOfferDetail", error.toString());

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cid", user_id);
                params.put("lat", "0.00");
                params.put("lon", "0.00");
                params.put("pid", pid);
                params.put("user_id", user_id);
                params.put("response_type","json");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        // Access the RequestQueue through your singleton class.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }


    public void getOfferDetail(String url) {

//        Mylogger.getInstance().Logit("getOfferDetail", url);
        RequestQueue queue = Volley.newRequestQueue(VourcherWebviewActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Mylogger.getInstance().Logit("vourvherdata", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject status = obj.getJSONObject("status");
                            if (status.getString("200").equalsIgnoreCase("success")) {
                                progressBar.setVisibility(View.GONE);
                                Mylogger.getInstance().Logit("vourvherdata", obj.getString("voucher"));
                                webView.loadDataWithBaseURL(null, obj.getString("voucher"), "text/html", "utf-8", null);
                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Mylogger.getInstance().Logit("getOfferDetail", error.toString());

            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        // Access the RequestQueue through your singleton class.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    private void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                VourcherWebviewActivity.this).create();
//        alertDialog.setTitle("Alert");
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
