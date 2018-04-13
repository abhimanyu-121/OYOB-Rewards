package com.oyob.controller.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.oyob.controller.Analytics.FirebaseAnalyticsClass;
import com.oyob.controller.R;
import com.oyob.controller.model.ModelClass;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.Mylogger;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 10/11/2017.
 */

public class OfferDetailActivity extends ActivityBase {

    private static final String SET_FAV = AppConstants.SERVER_ROOT + "set_favourites.php";
    TextView aod_tv_merchant_name;
    TextView aod_tv_get_now;
    TextView Details;
    TextView Text;
    ImageView imageView;
    ImageView favImageView;
    String androidId;
    String reportDate;
    TextView txtViewCount;
    private WebView webView, testweb;
    private String productUrl, cid, userId, ProductID, type;
    private LinearLayout baseLayout, webLayout;
    private Button redeem;
    private String favValue;
    private String uname;
    private ModelClass modelClass = new ModelClass();
    private String cat_id;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        AppConstants.getCardItemCount(this);
        cid = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        userId = sessionManager.getParticularField(SessionManager.USER_ID);
        ProductID = getIntent().getStringExtra("ProductID");
        sessionManager.updateProductId(ProductID);
        type = getIntent().getStringExtra("type");
        cat_id = getIntent().getStringExtra("cat_id");
        uname = sessionManager.getParticularField(SessionManager.USER_NAME);
        productUrl = AppConstants.SERVER_ROOT + "get_product.php?client_id=" + cid + "&id=" + ProductID + "&uid=" + userId + "&response_type=json";

        Mylogger.getInstance().Logit("producturl", productUrl);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();
        reportDate = df.format(currentTime);

        baseLayout = findViewById(R.id.base_layout);
        webLayout = findViewById(R.id.relativeLayout1);
        redeem = findViewById(R.id.redeem);

        aod_tv_merchant_name = findViewById(R.id.aod_tv_merchant_name);
        aod_tv_get_now = findViewById(R.id.aod_tv_get_now);
        imageView = findViewById(R.id.image);
        favImageView = findViewById(R.id.child_connect_image);
        webView = findViewById(R.id.web_image);
        testweb = findViewById(R.id.testweb);

        //ImageView img_client_logo =  findViewById(R.id.img_client_logo);
        String client_logo = AppConstants.SERVER_ROOT + "get_client_banner.php?cid=" + cid;
        //Picasso.with(getApplicationContext()).load(client_logo).into(img_client_logo);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setDefaultFontSize(40);

        baseLayout.setVisibility(View.GONE);
        webLayout.setVisibility(View.GONE);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        backArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.offerdetail_toolbar);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;

       /* Toolbar mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        backArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

       /* mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // perform whatever you want on back arrow click
            }
        });*/
        getBanners();
        getOfferDetail();

    }

    public void getOfferDetail() {
        RequestQueue queue = Volley.newRequestQueue(OfferDetailActivity.this);
        dialog.show();

        Log.i("TEST","url  "+productUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, productUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.i("TEST", "tt " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jArray = jsonObject.getJSONArray("product");

                            JSONObject jsonObject1 = jArray.getJSONObject(0);

                            String name = jsonObject1.getString("name");
                            String highlight = jsonObject1.getString("highlight");
                            final String id = jsonObject1.getString("id");
                            favValue = jsonObject1.getString("is_favourite");
                            String display_image = jsonObject1.getString("display_image");
                            String logoExtension = jsonObject1.getString("logo_extension");
                            String stripimage = jsonObject1.getString("strip_image");
                            final String merchant_id = jsonObject1.getString("merchant_id");
                            String offer = jsonObject1.getString("offer");
                            String image_extension = jsonObject1.getString("image_extension");
                            String details = jsonObject1.getString("details");
                            String text = jsonObject1.getString("text");
                            Integer product_quantity = null;
                            if (!TextUtils.isEmpty(jsonObject1.getString("product_quantity"))) {
                                product_quantity = jsonObject1.getInt("product_quantity");
                            }

                            final String redeem_button_img = jsonObject1.getString("redeem_button_img");
                            Integer web_coupon = jsonObject1.getInt("web_coupon");
                            Integer user_used_count = jsonObject1.getInt("user_used_count");
                            Integer used_count = jsonObject1.getInt("used_count");

                            modelClass.setName(name);
                            modelClass.setHighlight(highlight);
                            modelClass.setId(id);
                            modelClass.setIs_favourite("1");
                            modelClass.setDisplay_image(display_image);
                            modelClass.setLogo_extension(logoExtension);
                            modelClass.setStrip_image(stripimage);
                            modelClass.setMerchant_id(merchant_id);
                            modelClass.setOffer(offer);
                            modelClass.setImage_extension(image_extension);


                            aod_tv_merchant_name.setText(name);
                            aod_tv_get_now.setText(highlight);


                            if (favValue.equals(String.valueOf(true))) {
                                favImageView.setImageResource(R.drawable.fav_color);
                            } else if (favValue.equals(String.valueOf(false))) {

                                favImageView.setImageResource(R.drawable.fav_icon);
                            }

                            favImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (favValue.equals("true")) {

                                        SetFavApiCall(userId, id, false);

                                        putdetails(cid, userId, "android", androidId, cat_id, id, "Product Detail", reportDate, null, "Product set to favourite");
                                    } else if (favValue.equals("false")) {

                                        SetFavApiCall(userId, id, true);
                                        putdetails(cid, userId, "android", androidId, cat_id, id, "Product Detail", reportDate, null, "Product favourite removed");
                                    }
                                }
                            });

                            if (!logoExtension.equals("null") && !logoExtension.isEmpty()) {
                                String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + merchant_id + "." + logoExtension;
                                Glide.with(FirebaseAnalyticsClass.getInstance()).load(imageUrl).into(imageView);
                            } else if (!stripimage.equals("null") && !stripimage.isEmpty()) {
                                String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/strip_image/" + id + "." + stripimage;
                                Glide.with(FirebaseAnalyticsClass.getInstance()).load(imageUrl).into(imageView);
                            } else {
                                String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_image/" + id + "." + image_extension;
                                Glide.with(FirebaseAnalyticsClass.getInstance()).load(imageUrl).into(imageView);
                            }
                            String html = details + text;
                            //webView.loadData(html, "text/html", "UTF-8");
                            //webView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + html, "text/html", "UTF-8", null);
                            setWebViewWithImageFit(html);
                            if (!TextUtils.isEmpty(redeem_button_img)) {
                                redeem.setVisibility(View.VISIBLE);
                                redeem.setText("Redeem Now");
                                redeem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(redeem_button_img));
                                        startActivity(browse);
                                        redeemTracker(cid, userId, id);
                                    }
                                });
                            } else if (product_quantity != null && (product_quantity > 0 || product_quantity == -1)) {
                                redeem.setVisibility(View.VISIBLE);
                                redeem.setText("Buy Now");
                                redeem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //TODO  - API call to add item to cart
                                        addTocart(userId, ProductID);
                                    }
                                });
                            } else if ((web_coupon == 1) && (user_used_count > 0 || user_used_count == -1) && (used_count > 0 || used_count == -1)) {
                                redeem.setVisibility(View.VISIBLE);
                                redeem.setText("Get Now");
                                redeem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //TODO  - API call to voucher API
                                        String voucherAPI = AppConstants.SERVER_ROOT + "get_voucher.php?uid=" + userId + "&cid=" + cid + "&mid=" + merchant_id + "&pid=" + ProductID;

                                        Intent intent = new Intent(OfferDetailActivity.this, VourcherWebviewActivity.class);
                                        intent.putExtra("voucherAPI", voucherAPI);
                                        intent.putExtra("cid", cid);
                                        intent.putExtra("pid", ProductID);
                                        intent.putExtra("user_id", userId);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                redeem.setVisibility(View.GONE);
                            }
                            // get course_description
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        baseLayout.setVisibility(View.VISIBLE);
                        webLayout.setVisibility(View.VISIBLE);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        // Access the RequestQueue through your singleton class.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void SetFavApiCall(final String userId, final String productId, final boolean flag) {
        RequestQueue requestQueue = Volley.newRequestQueue(OfferDetailActivity.this);
        final ProgressDialog pDialog = new ProgressDialog(OfferDetailActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest postReq = new StringRequest(Request.Method.POST, SET_FAV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject.getJSONObject("status");

                        JSONObject status = jsonObject.getJSONObject("status");
                        JSONObject obj4 = null;
                        //Getting all the keys inside json object with key- pages
                        Iterator<String> keys = status.keys();
                        while (keys.hasNext()) {
                            String keyValue = (String) keys.next();

                            if (keyValue.equals("200")) {
                                if (flag) {
                                    favValue = "true";

                                } else {
                                    favValue = "false";
                                }

                                if (favValue.equals("true")) {
                                    favImageView.setImageResource(R.drawable.fav_color);
                                } else if (favValue.equals(String.valueOf(false))) {

                                    favImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                                }

                                if (type.equals("offers")) {
                                    for (ModelClass modelClass : AppConstants.offerList) {


                                        if (modelClass.getId().equals(productId)) {
                                            if (flag) {

                                                modelClass.setIs_favourite("1");
                                            } else {
                                                modelClass.setIs_favourite("0");
                                            }
                                        }

                                    }
                                }

                                if (flag) {

                                    modelClass.setIs_favourite("1");
                                    int isInFavList = getFavListPos(productId);
                                    if (isInFavList == -1) {
                                        AppConstants.favList.add(modelClass);
                                    }
                                } else {
                                    int isInFavList = getFavListPos(productId);
                                    if (isInFavList != -1) {
                                        AppConstants.favList.remove(isInFavList);
                                    }
                                    modelClass.setIs_favourite("0");
                                }
                            }

                            Log.d("response", response.toString());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", userId);
                params.put("pid", productId);
                params.put("flag", String.valueOf(flag));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        postReq.setShouldCache(false);
        requestQueue.add(postReq);
    }

    public int getFavListPos(String productId) {
        int isAvailable = -1;
        for (ModelClass modelClass : AppConstants.favList) {

            if (modelClass.getId().equals(productId)) {

                isAvailable = AppConstants.favList.indexOf(modelClass);
                return isAvailable;
            }

        }

        return isAvailable;

    }

    public void redeemTracker(final String cid, final String uid, final String product_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("cid", cid);
            jsonObject.put("uid", uid);
            jsonObject.put("product_id", product_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, AppConstants.redeemAction, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("PutDetails", response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("PutDetails", "");

                    }
                });


        jsObjRequest.setShouldCache(false);
        requestQueue.add(jsObjRequest);
    }

    public void addTocart(String uid, String product_id) {
        final ProgressDialog pDialog = new ProgressDialog(OfferDetailActivity.this);
        pDialog.setMessage("Adding product to cart");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(OfferDetailActivity.this);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", product_id);
            jsonObject.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, AppConstants.SERVER_ROOT + "add_cart.php", jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.i("PutDetails add to cart", response.toString());
                        Log.i("TEST", jsonObject.toString());
                        try {
                            if (response.has("cart_id")) {
                                String cart_id = response.getString("cart_id");
                                System.out.println("cart_id: " + cart_id);
                                sessionManager.updateCartId(cart_id);
                                //SharedPreferenceHelper.set(getApplicationContext(), "cart_id", cart_id);
                            }

                            JSONObject obj4 = null;
                            Iterator<String> keys = response.keys();

/*
                            while (keys.hasNext()) {
                                String keyValue = (String) keys.next();
                                //obj4 = response.getJSONObject(keyValue);

                                //getting string values with keys- pageid and title
                                String status = obj4.getString("200");
                                if (status.equals("success")) {

                                }
                            }
*/

                        } catch (Exception iox) {
                            System.out.println("iox: " + iox);
                        }
                        showAlert("Product added to cart");

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("PutDetails", "");
                        pDialog.dismiss();

                    }
                });

        jsObjRequest.setShouldCache(false);
        requestQueue.add(jsObjRequest);
    }

    private void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                OfferDetailActivity.this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void putdetails(final String cid, final String uid, final String platform, final String device_id, final String category_id,
                           final String product_id, final String page, final String date_time, final String status, final String message) {
        RequestQueue requestQueue = Volley.newRequestQueue(OfferDetailActivity.this);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("cid", cid);
            jsonObject.put("uid", uid);
            jsonObject.put("platform", platform);
            jsonObject.put("device_id", device_id);


            JSONObject actionObj = new JSONObject();

            actionObj.put("category_id", category_id);
            actionObj.put("product_id", product_id);
            actionObj.put("page", page);
            actionObj.put("datetime", date_time);
            actionObj.put("status", status);
            actionObj.put("message", message);

            jsonObject.put("action", actionObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, AppConstants.UserACtion, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("PutDetails", response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("PutDetails", "");

                    }
                });

        jsObjRequest.setShouldCache(false);
        requestQueue.add(jsObjRequest);
    }

    public void setWebViewWithImageFit(String content) {
        Log.i("TEST", "hhhhhh   " + content);

       /* // content is the content of the HTML or XML.
        String stringToAdd = "width=\"100%\" ";

        // Create a StringBuilder to insert string in the middle of content.
        StringBuilder sb = new StringBuilder(content);

        int i = 0;
        int cont = 0;

        // Check for the "src" substring, if it exists, take the index where
        // it appears and insert the stringToAdd there, then increment a counter
        // because the string gets altered and you should sum the length of the inserted substring
        while (i != -1) {
            i = content.indexOf("src", i + 1);
            if (i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd);
            ++cont;
        }

        // Set the webView with the StringBuilder: sb.toString()
        webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);*/


        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/avenir_roman.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
        String pas = "</body></html>";

        String myHtmlString = pish + content + pas;
        webView.loadDataWithBaseURL(null, myHtmlString, "text/html", "UTF-8", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_shop, menu);

        final View notificaitons = menu.findItem(R.id.action_myshop).getActionView();
        txtViewCount = (TextView) notificaitons.findViewById(R.id.txtCount);
        final int count = Integer.parseInt(ActivityDashboard.cartCount);
        Log.i("TEST", "count " + count);
        updateHotCount(count);
        txtViewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // updateHotCount(count);
            }
        });
        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    TODO
                if (count > 0) {
                    Intent intent = new Intent(OfferDetailActivity.this, ActivityDashboard.class);
                    intent.putExtra("iscatd", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(OfferDetailActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return true;
    }

    public void updateHotCount(final int count) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (count == 0)
                    txtViewCount.setVisibility(View.GONE);
                else {
                    txtViewCount.setVisibility(View.VISIBLE);
                    txtViewCount.setText(String.valueOf(count));
                    // supportInvalidateOptionsMenu();
                }
            }
        });
    }

    public void getBanners() {

        String url = AppConstants.SERVER_ROOT + "get_client_banner.php?cid=" + cid;

        testweb.setWebViewClient(new MyBrowser());
        testweb.getSettings().setJavaScriptEnabled(false);
        testweb.getSettings().setLoadsImagesAutomatically(true);
        testweb.getSettings().setLoadWithOverviewMode(true);
        testweb.getSettings().setUseWideViewPort(true);
        testweb.loadUrl(url);
    }


}


class MyBrowser extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        view.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {

    }

    private String getHtmlData(Context context, String data) {
        String head = "<head><style>@font-face {font-family: 'Avenir Roman';src: url('file://" + context.getFilesDir().getAbsolutePath() + "/Avenir Roman.ttf');}body {font-family: 'Avenir Roman';}</style></head>";
        String htmlData = "<html>" + head + "<body>" + data + "</body></html>";
        return htmlData;
    }
}

