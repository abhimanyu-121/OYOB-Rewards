package com.oyob.controller.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.oyob.controller.Analytics.FirebaseAnalyticsClass;
import com.oyob.controller.R;
import com.oyob.controller.activity.ActivityDashboard;
import com.oyob.controller.activity.ClickListener;
import com.oyob.controller.activity.RecyclerTouchListener;
import com.oyob.controller.interfaces.OnRecyclerViewItemClick;
import com.oyob.controller.model.ConnectModel;
import com.oyob.controller.model.FavouriteModel;
import com.oyob.controller.model.ModelClass;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.AppConstants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by 121 on 9/24/2017.
 */

public class FavouriteFragment extends Fragment implements OnRecyclerViewItemClick {

    private Context context;
    String GetFavouriteUrl = null;
    List<FavouriteModel> modelClassList;
    String cid = "";
    String concatString = "";
    public static String PRODUCTIMAGEURL = "";
    public static String MERCHANTIMAGEURL = "";
    FavouriteModel favouriteModel;
    ConnectModel connectModel;
    ModelClass modelClass;


    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    String imageId = "";
    String image = "";
    String merchantImageId = "";
    String uname = "";
    Bundle bundle;
    boolean aBooleanFAV;

    static final String TAG = ExclusiveFragment.class.getSimpleName();

    private static final String SET_FAV = AppConstants.SERVER_ROOT + "set_favourites.php";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.favourite_fragment, container, false);
        cid = SharedPreferenceHelper.getPref("client_id", getActivity());
        uname = SharedPreferenceHelper.getPref("uname", getActivity());
        connectModel = new ConnectModel();
        modelClass = new ModelClass();

        ActivityDashboard.setTitleToolbar(getString(R.string.favorite));

        GetFavouriteUrl = AppConstants.SERVER_ROOT + "get_favourites.php?uid=" + uname + "&start=0&limit=100";
        modelClassList = new ArrayList<>();

        final RecyclerView rv =  rootView.findViewById(R.id.card_recycler_view_favourite);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Adding favourite item...");
        pDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, GetFavouriteUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        Log.d(TAG, "onResponse: " + response);
                        JSONArray jsonarray;

                        try {
                            jsonarray = response.getJSONArray("favourites");

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                favouriteModel = new FavouriteModel();
                                final String pageid = jsonobject.getString("name");
                                favouriteModel.name = pageid;
                                final String title = jsonobject.getString("highlight");
                                favouriteModel.Discount = title;
                                String productImage = jsonobject.getString("display_image");
                                bundle = new Bundle();

                                final String tagInfo = jsonobject.getString("id");
                                favouriteModel.Tag = tagInfo;
                                imageId = jsonobject.getString("id");
                                String stripimage = jsonobject.getString("strip_image");
                                String logoExtension = jsonobject.getString("logo_extension");

                                String extension = "";
                                if (!logoExtension.isEmpty()) {
                                    imageId = jsonobject.getString("merchant_id");
                                    /*connectModel.setTag(imageId);*/
                                    bundle.putString("merchant_id", imageId);
                                    SharedPreferenceHelper.set(getActivity(), "value1", imageId);
                                    extension = jsonobject.getString("logo_extension");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + concatString;
                                    favouriteModel.setAndroid_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: merchantLogo" + concatString);
                                } else if (!stripimage.equals("null") && !logoExtension.equals("null")) {
                                    imageId = jsonobject.getString("id");
                                /*connectModel.setTag(imageId);*/
                                    bundle.putString("id", imageId);
                                    SharedPreferenceHelper.set(getActivity(), "value1", imageId);
                                    extension = jsonobject.getString("logo_extension");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + concatString;
                                    favouriteModel.setAndroid_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: merchantLogo" + concatString);
                                } else if (productImage.equals("Product Image")) {
                                    imageId = jsonobject.getString("id");
                                /*connectModel.setTag(imageId);*/
                                    SharedPreferenceHelper.set(getActivity(), "value1", imageId);
                                    extension = jsonobject.getString("image_extension");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_image/" + concatString;
                                /*STRIPIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_strip_images/" + concatString;*/
                                    favouriteModel.setAndroid_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: productImage" + concatString);
                                }
                                modelClassList.add(favouriteModel);
                                //favouriteAdapter.notifyDataSetChanged();
                                //Log.d(TAG, "onCreateView: " + favouriteAdapter.getItemCount());

                                Log.d(TAG, "Page id : " + pageid + " Title : " + title);

                                // On click Listener
                                rv.addOnItemTouchListener(new RecyclerTouchListener(context, rv, new ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {

                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }
                                }));
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        //getting string values with keys- pageid and title
                        // get course_description
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        pDialog.hide();
                    }
                });

// Access the RequestQueue through your singleton class.
        requestQueue.add(jsObjRequest);
        return rootView;
    }

    @Override
    public void listItemClick(View view, int position) {
        FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(uname, "Favourite", "FavouriteScreen"));

        SharedPreferenceHelper.set(getActivity(), "id", imageId);
        SharedPreferenceHelper.set(getActivity(), "merchant_id", merchantImageId);
        favouriteModel =  modelClassList.get(position);

        bundle.putString("id", imageId);
        bundle.putString("image", image);
        bundle.putString("concatString", concatString);
        bundle.putString("name", favouriteModel.name);
        bundle.putString("highlight", favouriteModel.Discount);
        bundle.putString("url", favouriteModel.android_image_url);
        bundle.putString("tag", favouriteModel.getTag());


        fragment = new ChildFavouriteFragment();
        fragment.setArguments(bundle);
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, null).commit();
    }

    @Override
    public void favoriteItemclick(View view, int position) {
        FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(uname, "Favourite", "FavouriteScreen"));

        favouriteModel =  modelClassList.get(position);
        if (SharedPreferenceHelper.getPref("apiResponseConnect", getActivity()).equals("1") || SharedPreferenceHelper.getPref("apiResponse", getActivity()).equals("1")) {
            SetFavApiCall(String.valueOf(uname), String.valueOf(favouriteModel.Tag), ConnectFragment.stringToBool(String.valueOf(1)));
            SharedPreferenceHelper.set(getActivity(), "apiResponse", String.valueOf(1));
            Log.d(TAG, "favoriteItemclick: " + String.valueOf(1));
        } else if (SharedPreferenceHelper.getPref("apiResponseConnect", getActivity()).equals("0") || SharedPreferenceHelper.getPref("apiResponse", getActivity()).equals("0")) {
            SetFavApiCall(String.valueOf(uname), String.valueOf(favouriteModel.Tag), ConnectFragment.stringToBool(String.valueOf(0)));
            SharedPreferenceHelper.set(getActivity(), "apiResponse", String.valueOf(0));
            Log.d(TAG, "favoriteItemclick: " + String.valueOf(0));
        }
    }

    @Override
    public void productRedeemClick(View view, int position) {

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
                            String keyValue = (String) keys.next();
                            obj4 = jsonObject.getJSONObject(keyValue);
                            //getting string values with keys- pageid and title
                            String status = obj4.getString("200");
                            if (status.equals("success")) {
                                /*if(favSuccessStringConnect.equalsIgnoreCase("false")) {
                                    *//*SharedPreferenceHelper.set(getActivity(), "SuccessStateConnect", !aBooleanConnect);*//*
                                }
                                else  if(favSuccessStringConnect.equalsIgnoreCase("true")) {
                                    set(getActivity(), "SuccessStateFav", changeaBoolean);
                                }*/

                            } else {
                                SharedPreferenceHelper.set(getActivity(), "Failure", aBooleanFAV);
                            }
                            /*aBooleanConnect = !aBooleanConnect;*/

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