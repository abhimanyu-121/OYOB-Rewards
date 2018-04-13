package com.oyob.controller.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.oyob.controller.R;
import com.oyob.controller.adapter.FavouriteAdapter;
import com.oyob.controller.model.ModelClass;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Ramasamy on 9/8/2017.
 */
public class FavouriteActivity extends ActivityBase {

    Fragment fragment;
    SessionManager sessionManager;
    String bunbleFriends = null, Url, cid, uname;

    private FavouriteAdapter favouriteAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        sessionManager = new SessionManager(this);
        bunbleFriends = bundle.getString("client_id");

        cid = bunbleFriends;
        uname = bundle.getString("uname");

        String uid = sessionManager.getParticularField(SessionManager.USER_ID);
        Url = AppConstants.SERVER_ROOT + "get_favourites.php?uid=" + uid + "&start=0&limit=150";

        setContentView(R.layout.friends_fragment);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        backArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.favourite_toolbar);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
        ImageView img_client_logo = findViewById(R.id.image);
        String client_logo = AppConstants.SERVER_ROOT + "get_client_banner.php?cid=" + cid;
        Picasso.with(getApplicationContext()).load(client_logo).into(img_client_logo);

        final RecyclerView rv = findViewById(R.id.card_recycler_view_friends);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(FavouriteActivity.this);
        rv.setLayoutManager(llm);

        AppConstants.favList.clear();
        favouriteAdapter = new FavouriteAdapter(FavouriteActivity.this, AppConstants.favList);
        rv.setAdapter(favouriteAdapter);
        getFaviorites();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (favouriteAdapter != null)
            favouriteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    TextView txtViewCount;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_shop, menu);

        final View notificaitons = menu.findItem(R.id.action_myshop).getActionView();

        txtViewCount = notificaitons.findViewById(R.id.txtCount);
        final int count = Integer.parseInt(ActivityDashboard.cartCount);
        //final int count = Integer.valueOf(sessionManager.getParticularField(SessionManager.CART_COUNT));
        updateHotCount(count);
        txtViewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateHotCount(count);
            }
        });
        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    TODO
                if (count > 0) {
                    Intent intent = new Intent(FavouriteActivity.this, ActivityDashboard.class);
                    intent.putExtra("iscatd", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(FavouriteActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppConstants.favList.clear();
    }

    public void getFaviorites() {
        RequestQueue queue = Volley.newRequestQueue(FavouriteActivity.this);
        dialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        Log.d("onResponse", "onResponse: " + response);
                        try {
                            JSONObject status = response.getJSONObject("status");

                            Iterator<String> iter = status.keys();
                            ArrayList<String> keys = new ArrayList<>();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                keys.add(key);
                            }

                            if (keys.get(0).equals("200")) {

                                JSONArray fav = response.getJSONArray("favourites");

                                for (int i = 0; i < fav.length(); i++) {

                                    JSONObject jsonObject = fav.getJSONObject(i);
                                    ModelClass modelClass = new ModelClass();
                                    //getting string values with keys- pageid and title
                                    String name = jsonObject.getString("name");
                                    String highlight = jsonObject.getString("highlight");
                                    String id = jsonObject.getString("id");
                                    String display_image = jsonObject.getString("display_image");
                                    String logoExtension = jsonObject.getString("logo_extension");
                                    String stripimage = jsonObject.getString("strip_image");
                                    String merchant_id = jsonObject.getString("merchant_id");
                                    String offer = jsonObject.getString("offer");
                                    String image_extension = jsonObject.getString("image_extension");

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

                                    modelClass.setProduct_quantity(jsonObject.getString("product_quantity"));
                                    modelClass.setOut_of_order(jsonObject.getString("out_of_stock"));
                                    modelClass.setWeb_coupon(jsonObject.getString("web_coupon"));
                                    modelClass.setUsed_count(jsonObject.getString("used_count"));
                                    modelClass.setUser_used_count(jsonObject.getString("user_used_count"));
                                    modelClass.setRedeem_button_img(jsonObject.getString("redeem_button_img"));


                                    AppConstants.favList.add(modelClass);

                                }
                            } else {
                                Toast.makeText(FavouriteActivity.this, "No Favourites Found", Toast.LENGTH_SHORT).show();
                            }
                            // get course_description
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        Log.d("", "onResponse: ");
                        favouriteAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

// Access the RequestQueue through your singleton class.
        jsObjRequest.setShouldCache(false);
        queue.add(jsObjRequest);

    }
}
