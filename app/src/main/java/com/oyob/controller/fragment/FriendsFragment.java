package com.oyob.controller.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.oyob.controller.R;
import com.oyob.controller.activity.ClickListener;
import com.oyob.controller.activity.RecyclerTouchListener;
import com.oyob.controller.adapter.FriendsRecyclerAdapter;
import com.oyob.controller.model.ConnectModel;
import com.oyob.controller.model.FriendsModel;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.AppConstants;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 121 on 9/14/2017.
 */

public class FriendsFragment extends Fragment {
    String Url = "";
    List<FriendsModel> modelClassList;
    Context context;
    String cid = "";
    String uname = "", country = "";
    String concatString = "";
    public static String PRODUCTIMAGEURL = "";
    public static String MERCHANTIMAGEURL = "";

    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String imageId = "";
    String image = "";
    String merchantImageId = "";
    static final String TAG = FriendsFragment.class.getSimpleName();
    int PAGELIMIT = 30;

    private static final String SET_FAV = AppConstants.SERVER_ROOT + "set_favourites.php";
    boolean isPressed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.friends_fragment, container, false);
        modelClassList = new ArrayList<>();
        cid = SharedPreferenceHelper.getPref("client_id", getActivity());
        uname = SharedPreferenceHelper.getPref("uname", getActivity());
        country = SharedPreferenceHelper.getPref("country", getActivity());
        Url = AppConstants.SERVER_ROOT + "search_cat.php?cat_id=554&cid=" + cid + "&country=" + country + "&start=0&limit=" + PAGELIMIT;
        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.card_recycler_view_friends);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        final FriendsRecyclerAdapter friendsRecyclerAdapter = new FriendsRecyclerAdapter(getActivity(), modelClassList);
        rv.setAdapter(friendsRecyclerAdapter);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        Log.d(TAG, "onResponse: " + response);
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
                                obj4 = jsonObject.getJSONObject(keyValue);
                                FriendsModel friendsModel = new FriendsModel();
                                //getting string values with keys- pageid and title
                                final String pageid = obj4.getString("name");
                                friendsModel.name = pageid;
                                final String title = obj4.getString("highlight");
                                friendsModel.Discount = title;

                                /*String productImage = obj4.getString("display_image");*/
                                String stripImage = obj4.getString("strip_image");
                                final Bundle bundle = new Bundle();

                                final String tagInfo = obj4.getString("id");
                                friendsModel.Tag = tagInfo;
                                imageId = obj4.getString("id");

                                String id = "";
                                String extension = "";

/*connectModel.setTag(imageId);*//*
                                    SharedPreferenceHelper.set(getActivity(), "value1", imageId);
                                    extension = obj4.getString("image_extension");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_image/" + concatString;
                                    *//*STRIPIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_strip_images/" + concatString;*//*
                                    friendsModel.setAndroid_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: productImage" + concatString);
                                } else if (productImage.equals("Merchant Logo")) {
                                    imageId = obj4.getString("merchant_id");
                                    *//*connectModel.setTag(imageId);*//*
                                    bundle.putString("merchant_id", imageId);
                                    SharedPreferenceHelper.set(getActivity(), "value1", imageId);
                                    extension = obj4.getString("logo_extension");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    *//*MERCHANTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + concatString;*//*
                                    MERCHANTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/strip_image/" + concatString;
                                    friendsModel.setAndroid_image_url(MERCHANTIMAGEURL);
                                    Log.d(TAG, "onResponse: merchantLogo" + concatString);
                                }*/


                                if (stripImage.equals("jpg")) {
                                    imageId = obj4.getString("id");
                                    /*connectModel.setTag(imageId);*/
                                    bundle.putString("stripImage", imageId);
                                    SharedPreferenceHelper.set(getActivity(), "value1", imageId);
                                    extension = obj4.getString("strip_image");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    /*MERCHANTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + concatString;*/
                                    MERCHANTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/strip_image/" + concatString;
                                    friendsModel.setAndroid_image_url(MERCHANTIMAGEURL);
                                    Log.d(TAG, "onResponse: merchantLogo" + concatString);
                                }
                                modelClassList.add(friendsModel);
                                friendsRecyclerAdapter.notifyDataSetChanged();

                                // On click Listener
                                rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        FriendsModel atPosition = (FriendsModel) modelClassList.get(position);
                                        if (atPosition.isPresssed()) {
                                            SetFavApiCall(String.valueOf(uname), String.valueOf(atPosition.Tag), !isPressed);
                                        }
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {
                                        SharedPreferenceHelper.set(getActivity(), "id", imageId);
                                        SharedPreferenceHelper.set(getActivity(), "merchant_id", merchantImageId);
                                        FriendsModel atPosition =  modelClassList.get(position);

                                        bundle.putString("id", imageId);
                                        bundle.putString("image", image);
                                        bundle.putString("concatString", concatString);
                                        bundle.putString("name", atPosition.name);
                                        bundle.putString("highlight", atPosition.Discount);
                                        bundle.putString("url", atPosition.android_image_url);
                                        bundle.putString("tag", atPosition.getTag());

                                        fragment = new ChildFriendsFragment();
                                        fragment.setArguments(bundle);
                                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(android.R.id.content, fragment, null).commit();

                                    }
                                }));
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        pDialog.hide();
                    }
                });

// Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest);

        return rootView;
    }

    public void SetFavApiCall(final String userId, final String productId, final boolean flag) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Favourite item...");
        pDialog.show();
        StringRequest postReq = new StringRequest(Request.Method.POST, SET_FAV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject.getJSONObject("status");
                        Log.d(TAG, "onResponse:setFavorite " + jsonObject);
                        JSONObject obj4 = null;
                        //Getting all the keys inside json object with key- pages
                        Iterator<String> keys = jsonObject.keys();
                        while (keys.hasNext()) {
                            String keyValue =  keys.next();
                            obj4 = jsonObject.getJSONObject(keyValue);
                            ConnectModel connectModel = new ConnectModel();
                            //getting string values with keys- pageid and title
                            String pageid = obj4.getString("200");
                            if (pageid.equals("success")) {

                            } else {

                            }
                            Log.d(TAG, "onResponse: ");
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Retry...", Toast.LENGTH_LONG).show();
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

        requestQueue.add(postReq);
    }


}
