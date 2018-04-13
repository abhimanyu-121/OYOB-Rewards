package com.oyob.controller.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.oyob.controller.Analytics.FirebaseAnalyticsClass;
import com.oyob.controller.R;
import com.oyob.controller.adapter.ProductAdapter;
import com.oyob.controller.fragment.CardFragment;
import com.oyob.controller.fragment.CategoriesFragment;
import com.oyob.controller.fragment.FragmentAbout;
import com.oyob.controller.fragment.FragmentContact;
import com.oyob.controller.fragment.FragmentPopularOffers;
import com.oyob.controller.fragment.MainPageFragment;
import com.oyob.controller.fragment.MapsActivity;
import com.oyob.controller.networkCall.ProcessAsynctack;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityDashboard extends ActivityBase
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    static final String TAG = ActivityDashboard.class.getSimpleName();
    public static LinearLayout linearLayout;
    public static String cartCount = "0";
    public static ImageView headerUserName;
    public static ImageView img_client_logo;
    static TextView mDotsText[];
    static TextView txt_toolbar_title;
    protected DrawerLayout drawer;
    TextView cart_counter;
    SessionManager sessionManager;
    ProcessAsynctack process;
    String stringResponse;
    String URL = null;
    String clientId = null;
    String country = null;
    String uname = null;
    ProductAdapter customAdapter1;
    ImageView ad_iv_banner;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView mTitle;
    String androidId;
    Date currentTime;
    String userId = "";
    String errorcode = "";
    DateFormat df;
    String reportDate;
    Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    private int mDotCount;
    private LinearLayout mDotsLayout;
    private boolean isSuccessApiCall = false;

    private WebView testweb;

    public static void setTitleToolbar(String ttl) {
        txt_toolbar_title.setText(ttl);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sessionManager = new SessionManager(getApplicationContext());

        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currentTime = Calendar.getInstance().getTime();
        reportDate = df.format(currentTime);
        linearLayout = findViewById(R.id.container);
        clientId = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        country = sessionManager.getParticularField(SessionManager.COUNTRY);
        uname = sessionManager.getParticularField(SessionManager.USER_NAME);
        userId = sessionManager.getParticularField(SessionManager.USER_ID);
        URL = AppConstants.SERVER_ROOT + "get_cat.php?cid=" + clientId + "&country=" + country + "&response_type=json&images=new_cat";

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txt_toolbar_title = toolbar.findViewById(R.id.txt_toolbar_title);

        testweb = findViewById(R.id.testweb);
        img_client_logo = findViewById(R.id.img_client_logo);

        String client_logo = AppConstants.SERVER_ROOT + "get_client_banner.php?cid=" + clientId;
        // Log.i("TEST","url "+client_logo);
        //Picasso.with(context).load("http://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/clients/newsletter_banner_image_1/716_03feffeda08ece0993837fac7df0f70a.jpg").into(img_client_logo);
        //Picasso.with(context).load(client_logo).into(img_client_logo);
        AppConstants.getCardItemCount(getApplicationContext());

        if (savedInstanceState == null) {
            if (getIntent().getBooleanExtra("iscatd", false)) {
                addFragmentINQueue(new MyShopActivity());
            } else {
                addFragmentINQueue(new FragmentPopularOffers());
            }
        }

        getBanners();

        drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        headerUserName = navHeaderView.findViewById(R.id.client_logo);
        headerUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentINQueue(new MainPageFragment());
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        cart_counter = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_myshop));

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.


            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
                initializeCountDrawer();
            }
        });

        //initializeCountDrawer();
    }

    public void addFragmentINQueue(Fragment fragment1) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction().addToBackStack("");
        fragmentTransaction.replace(R.id.frame, fragment1, null).commit();
    }

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            } else {
                addFragmentINQueue(new MainPageFragment());
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(getApplicationContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("NewMenuActivity.this", "Permission is granted");
                return true;
            } else {
                Log.d("NewMenuActivity.this", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.d("NewMenuActivity.this", "Permission is granted");
            return true;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_about) {
            addFragmentINQueue(new FragmentAbout());
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "AboutScreen"));
            AppConstants.getCardItemCount(this);
            putdetails(clientId, userId, "android", androidId, null, null, "ADOUT", reportDate, "", "From Side Menu");
        } else if (id == R.id.nav_exclusives) {
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "ExclusiveScreen"));
            Intent intent = new Intent(ActivityDashboard.this, OfferListActivity.class);
            intent.putExtra("client_id", clientId);
            intent.putExtra("cat_id", "552");
            intent.putExtra("name", "Exclusive Offers");
            startActivity(intent);
            AppConstants.getCardItemCount(this);
            putdetails(clientId, userId, "android", androidId, null, null, "Exclusive Offers", reportDate, null, "From Side Menu");

        } else if (id == R.id.nav_connect) {
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "ConnectScreen"));
            Intent intent = new Intent(ActivityDashboard.this, OfferListActivity.class);
            intent.putExtra("client_id", clientId);
            intent.putExtra("cat_id", "553");
            intent.putExtra("name", "Connect Offers");
            startActivity(intent);

            putdetails(clientId, userId, "android", androidId, null, null, "Exclusive Offers", reportDate, null, "From Side Menu");

        } else if (id == R.id.nav_friends) {
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "FriendsScreen"));

            Intent intent = new Intent(ActivityDashboard.this, OfferListActivity.class);
            intent.putExtra("client_id", clientId);
            intent.putExtra("cat_id", "554");
            intent.putExtra("name", "Friends");
            startActivity(intent);
            putdetails(clientId, userId, "android", androidId, null, null, "Friends", reportDate, null, "From Side Menu");
        } else if (id == R.id.nav_favourite) {
            Intent intent = new Intent(getApplicationContext(), FavouriteActivity.class);
            intent.putExtra("client_id", clientId);
            intent.putExtra("uname", uname);
            startActivity(intent);
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "FavouriteScreen"));
            putdetails(clientId, userId, "android", androidId, null, null, "Favourites", reportDate, null, "From Side Menu");
        } else if (id == R.id.nav_contact) {
            addFragmentINQueue(new FragmentContact());
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "ContactScreen"));
            putdetails(clientId, userId, "android", androidId, null, null, "Contact", reportDate, null, "From Side Menu");

        } else if (id == R.id.nav_myshop) {
            addFragmentINQueue(new MyShopActivity());
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "MyShopScreen"));
            AppConstants.getCardItemCount(this);
            putdetails(clientId, userId, "android", androidId, null, null, "MyShop", reportDate, null, "From Side Menu");
        } else if (id == R.id.nav_mycard) {
            addFragmentINQueue(new CardFragment());
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "MyCardScreen"));
            putdetails(clientId, userId, "android", androidId, null, null, "MyCard", reportDate, null, "From Side Menu");
        } else if (id == R.id.nav_explore) {
            addFragmentINQueue(new MapsActivity());
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "Arround me"));
            putdetails(clientId, userId, "android", androidId, null, null, "Arround me", reportDate, null, "From Side Menu");
            AppConstants.getCardItemCount(this);
        } else if (id == R.id.nav_popular) {
            addFragmentINQueue(new FragmentPopularOffers());
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "Popular offers"));
            putdetails(clientId, userId, "android", androidId, null, null, "Popular offers", reportDate, null, "From Side Menu");
            AppConstants.getCardItemCount(this);
        } else if (id == R.id.nav_categories) {
            addFragmentINQueue(new CategoriesFragment());
            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "Categories"));
            AppConstants.getCardItemCount(this);
            putdetails(clientId, userId, "android", androidId, null, null, "Categories", reportDate, null, "From Side Menu");
        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sure you want to log out?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sessionManager.updateIsFirstRun(true);
                            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "NavButton", "Categories"));
                            putdetails(clientId, userId, "android", androidId, null, null, "Categories", reportDate, null, "From Side Menu");
                            startActivity(new Intent(context, ActivityLogin.class));
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    public void putdetails(final String cid, final String uid, final String platform, final String device_id, final String category_id,
                           final String product_id, final String page, final String date_time, final String status, final String message) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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

    private void initializeCountDrawer() {

        cart_counter.setGravity(Gravity.CENTER_VERTICAL);
        cart_counter.setTypeface(null, Typeface.BOLD);
        cart_counter.setTextColor(getResources().getColor(R.color.colorAccent));
        getCardItemCount(ActivityDashboard.this);

    }

    public void getCardItemCount(Context context) {
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
                            if (!jsonObject.getString("cart_items").equalsIgnoreCase("0"))
                                cart_counter.setText((jsonObject.getString("cart_items")));
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

/*
      public void getBanners() {

        String url = AppConstants.SERVER_ROOT + "get_client_banner.php?cid=" + clientId;

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("images");

                    ArrayList<String> images = new ArrayList<>();
                    ArrayList<String> imageId = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String extension = jsonObject1.getString("big_img_extension");

                        BannerModel bannerModel = new BannerModel();
                        bannerModel.setId(id);
                        bannerModel.setBig_img_extension(extension);

                        ArrayList<BannerModel> bannerList = new ArrayList<>();
                        bannerList.add(bannerModel);

                        String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/big_image/" + id + "." + extension;
                        images.add(imageUrl);
                        imageId.add(id);
                    }

                    //getCategories();

                } catch (JSONException e) {
                    Mylogger.getInstance().Logit(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Mylogger.getInstance().Logit(TAG, error.toString());
                // hide the progress dialog
            }
        });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
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
        queue.add(jsonObjReq);
        queue.start();
    }
*/

    public void getBanners() {

        String url = AppConstants.SERVER_ROOT + "get_client_banner.php?cid=" + clientId;

        testweb.setWebViewClient(new com.oyob.controller.activity.MyBrowser());
        testweb.getSettings().setJavaScriptEnabled(false);
        testweb.getSettings().setLoadsImagesAutomatically(true);
        testweb.getSettings().setLoadWithOverviewMode(true);
        testweb.getSettings().setUseWideViewPort(true);
        testweb.loadUrl(url);
    }

    class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            view.setVisibility(View.VISIBLE);
            return true;
        }
    }

}