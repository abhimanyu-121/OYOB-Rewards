package com.oyob.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.oyob.controller.R;
import com.oyob.controller.activity.ActivityDashboard;
import com.oyob.controller.activity.OfferDetailActivity;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Narender Kumar on 2/23/2018.
 * Prominent Developer
 * narender.kumar.nishad@gmail.com
 */

public class FragmentPopularOffers extends FragmentBase {

    ViewPager fpo_vp_pager;
    String[] images_link, web_link;
    TextView fpo_tv_error_text;
    ProgressBar fpo_pb_progress;
    private SessionManager sessionManager;
    private String cat_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_offer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(getActivity());

        ActivityDashboard.setTitleToolbar(getString(R.string.popular_offers));

        fpo_vp_pager = view.findViewById(R.id.fpo_vp_pager);
        fpo_vp_pager.setClipToPadding(false);
        fpo_vp_pager.setPadding(0, 0, 40, 0);

        cat_id = sessionManager.getParticularField(SessionManager.CART_ID);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        fpo_vp_pager.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        fpo_tv_error_text = view.findViewById(R.id.fpo_tv_error_text);
        fpo_pb_progress = view.findViewById(R.id.fpo_pb_progress);
        if (isNetworkConnected(_context)) {
            getPopularOffers();
        } else {
            Toast.makeText(_context, getString(R.string.check_internet_connectivity), Toast.LENGTH_SHORT).show();
        }
    }

    private void getPopularOffers() {

        RequestQueue requestQueue = Volley.newRequestQueue(_context);
        //dialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, AppConstants.POPULAR_OFFERS + "?cid=" + sessionManager.getParticularField(SessionManager.CLIENT_ID), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        Log.d("popular", "onResponse: " + response);
                        JSONArray jsonarray;
                        fpo_pb_progress.setVisibility(View.GONE);
                        try {
                            if (response.getJSONObject("status").has("200")) {
                                jsonarray = response.getJSONArray("popular_offers");
                                if (jsonarray.length() > 0) {
                                    images_link = new String[response.getJSONArray("popular_offers").length()];
                                    web_link = new String[response.getJSONArray("popular_offers").length()];
                                    for (int i = 0; i < images_link.length; i++) {
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        images_link[i] = jsonobject.getString("image");
                                        web_link[i] = jsonobject.getString("image_link");
                                    }
                                    fpo_vp_pager.setAdapter(new ImagePagerAdapter());
                                    fpo_vp_pager.setOffscreenPageLimit(images_link.length);
                                } else {
                                    fpo_tv_error_text.setVisibility(View.VISIBLE);
                                    fpo_tv_error_text.setText(R.string.no_offers);
                                    fpo_vp_pager.setVisibility(View.GONE);
                                }
                            } else {
                                fpo_tv_error_text.setVisibility(View.VISIBLE);
                                fpo_vp_pager.setVisibility(View.GONE);
                                fpo_tv_error_text.setText(R.string.offers_not_found);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //getting string values with keys- pageid and title
                    // get course_description
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i("Popular Offers", "Error :" + error.toString());
                        dialog.dismiss();
                    }
                });
        // Access the RequestQueue through your singleton class.
        requestQueue.add(jsObjRequest);
    }

    private class ImagePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return images_link.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public View instantiateItem(@NonNull ViewGroup container, final int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View view = inflater.inflate(R.layout.row_popular_offers, null);
            final ImageView imageView = view.findViewById(R.id.rpo_tiv_offer_image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (web_link[position] != null) {

                        /*Intent intent = new Intent(getActivity(), PopularOfferWebViewActivity.class);
                        intent.putExtra("links", web_link[position]);
                        startActivity(intent);*/

                        Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
                        intent.putExtra("type", "offers");
                        intent.putExtra("cat_id", cat_id);
                        intent.putExtra("ProductID", web_link[position]);
                        getActivity().startActivity(intent);
                    }
                }
            });
            Glide.with(_context).load(AppConstants.POPULAR_OFFERS_IMAGE_PREFIX + images_link[position])
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            fpo_pb_progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            fpo_pb_progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}