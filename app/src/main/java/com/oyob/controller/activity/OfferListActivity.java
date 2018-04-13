package com.oyob.controller.activity;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.oyob.controller.R;
import com.oyob.controller.adapter.ExclusiveRecycleAdapter;
import com.oyob.controller.interfaces.OnLoadMoreListener;
import com.oyob.controller.interfaces.RecyclerViewLoadMoreScroll;
import com.oyob.controller.model.ModelClass;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.Mylogger;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ramasamy on 9/2/2017.
 */

public class OfferListActivity extends ActivityBase {

    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String bunbleCustId = null, title;
    static final String TAG = OfferListActivity.class.getSimpleName();
    TextView title_cat;
    private String Url, uname, cat_id, country = "";
    private ExclusiveRecycleAdapter rvAdapter;
    private RecyclerView rv;
    private RecyclerViewLoadMoreScroll scrollListener;
    private static int pageNumber = 0;
    private static int PAGELIMIT = 20;
    List<ModelClass> offerList = new ArrayList<>();

    public static TextView yy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Bundle bundle = getIntent().getExtras();
        bunbleCustId = bundle.getString("client_id");
        cat_id = getIntent().getStringExtra("cat_id");
        uname = sessionManager.getParticularField(SessionManager.USER_NAME);
        title = bundle.getString("name");
        country = sessionManager.getParticularField(SessionManager.COUNTRY);

        pageNumber = 0;
        PAGELIMIT = 20;
        Url = AppConstants.SERVER_ROOT + "search_cat.php?cat_id=" + cat_id + "&cid=" + bunbleCustId + "&country=" + country + "&start=" + pageNumber + "&limit=" + PAGELIMIT + "&uid=" + uname;

        //  Url = "https://vicchambers.benefits.plus/newapp/search_cat.php?cat_id="+cat_id+"&cid="+bunbleCustId+"&country="+country+"&start=0&limit="+PAGELIMIT+"&uid="+uname;

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        backArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // perform whatever you want on back arrow click
            }
        });

        title_cat = findViewById(R.id.txv_title);
        title_cat.setText(title);

        rv = findViewById(R.id.card_recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(OfferListActivity.this);
        rv.setLayoutManager(llm);
        rvAdapter = new ExclusiveRecycleAdapter(OfferListActivity.this, offerList, cat_id);
        rv.setAdapter(rvAdapter);

        scrollListener = new RecyclerViewLoadMoreScroll(llm);
        scrollListener.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                rvAdapter.addLoadingView();
                pageNumber = pageNumber + 1;
                Url = AppConstants.SERVER_ROOT + "search_cat.php?cat_id=" + cat_id + "&cid=" + bunbleCustId + "&country=" + country + "&start=" + pageNumber + "&limit=" + PAGELIMIT + "&uid=" + uname;
                getOfferList();
            }
        });
        rv.addOnScrollListener(scrollListener);
        rvAdapter.addLoadingView();
        getOfferList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (rvAdapter != null)
            rvAdapter.notifyDataSetChanged();
    }

    public void getOfferList() {
        Mylogger.getInstance().Logit(TAG, "getOfferList");
        Mylogger.getInstance().Logit(TAG, Url);
        RequestQueue queue = Volley.newRequestQueue(OfferListActivity.this);
        final ProgressDialog pDialog = new ProgressDialog(OfferListActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Mylogger.getInstance().Logit(TAG, "onResponse: " + response);
                        offerList = new ArrayList<>();
                        try {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject("cat");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            JSONObject obj4 = null;
                            //Getting all the keys inside json object with key- pages
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String keyValue = (String) keys.next();
                                if (!keyValue.equalsIgnoreCase("link_next") || !keyValue.equalsIgnoreCase("link_prev")) {
                                    obj4 = jsonObject.getJSONObject(keyValue);

                                    ModelClass modelClass = new ModelClass();
                                    //getting string values with keys- pageid and title
                                    String name = obj4.getString("name");
                                    String highlight = obj4.getString("highlight");
                                    String id = obj4.getString("id");
                                    String favValue = obj4.getString("is_favourite");
                                    String display_image = obj4.getString("display_image");
                                    String logoExtension = obj4.getString("logo_extension");
                                    String stripimage = obj4.getString("strip_image");
                                    String merchant_id = obj4.getString("merchant_id");
                                    String offer = obj4.getString("offer");
                                    String image_extension = obj4.getString("image_extension");


                                    modelClass.setName(name);
                                    modelClass.setHighlight(highlight);
                                    modelClass.setId(id);
                                    modelClass.setIs_favourite(favValue);
                                    modelClass.setDisplay_image(display_image);
                                    modelClass.setLogo_extension(logoExtension);
                                    modelClass.setStrip_image(stripimage);
                                    modelClass.setMerchant_id(merchant_id);
                                    modelClass.setOffer(offer);
                                    modelClass.setImage_extension(image_extension);
                                    offerList.add(modelClass);
                                }
                            }

                            // get course_description
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        Mylogger.getInstance().Logit("offerList activity", "" + offerList);
                        rvAdapter.removeLoadingView();
                        rvAdapter.addData(offerList);
                        rvAdapter.notifyDataSetChanged();
                        scrollListener.setLoaded();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        pDialog.dismiss();
                        rvAdapter.removeLoadingView();
                        rvAdapter.addData(offerList);
                        rvAdapter.notifyDataSetChanged();
                        scrollListener.setLoaded();
                    }
                });
        jsObjRequest.setRetryPolicy(new RetryPolicy() {
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
        queue.add(jsObjRequest);

    }


}


