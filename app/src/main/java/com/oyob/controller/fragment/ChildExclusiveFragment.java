package com.oyob.controller.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oyob.controller.R;
import com.oyob.controller.adapter.ChildExclusiveProductFragmentAdapter;
import com.oyob.controller.interfaces.OnRecyclerViewItemClick;
import com.oyob.controller.model.ModelClass;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.SessionManager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oyob.controller.utils.AppConstants.redeemAction;


/**
 * Created by 121 on 9/19/2017.
 */

public class ChildExclusiveFragment extends FragmentBase implements OnRecyclerViewItemClick {
    String productUrl = null;
    List<ModelClass> modelClassList;
    String cid = "";
    String ProductID = "";
    public static String PRODUCTIMAGEURL = "";
    public static String MERCHANTIMAGEURL = "";
    String imageId = "";
    public static final String TAG = ChildConnectFragment.class.getSimpleName();
    String uname = "";
    ModelClass childExclusiveModel;
    ModelClass modelClass;
    String favresponse;

    String tagEx;
    String apiClientId = "";
    String userId = "";
    String name = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = LayoutInflater.from(_context).inflate(R.layout.child_exclusive_layout, container, false);
        modelClassList = new ArrayList<>();
        modelClass = new ModelClass();
        apiClientId = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        userId = sessionManager.getParticularField(SessionManager.USER_ID);
        Bundle bundle = getArguments();
        String tagvalue = bundle.getString("tagValue");
        name = bundle.getString("name");
        tagEx = bundle.getString("tagExclusive");
        uname = sessionManager.getParticularField(SessionManager.USER_NAME);
        favresponse = bundle.getString("favData");
        cid = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        ProductID = sessionManager.getParticularField(SessionManager.PRODUCT_ID);

        productUrl = AppConstants.SERVER_ROOT + "get_product.php?client_id=" + cid + "&id=" + tagvalue + "&uid=" + userId;

        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.card_recycler_view_child_exclusive);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        final ChildExclusiveProductFragmentAdapter connectRecyclerAdapter = new ChildExclusiveProductFragmentAdapter(getActivity(), modelClassList);
        connectRecyclerAdapter.setClickListener(this);
        rv.setAdapter(connectRecyclerAdapter);


       /* RequestQueue queue = Volley.newRequestQueue(getActivity());
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, productUrl,
                new Response.Listener<String>()  {
                    @Override
                    public void onResponse(String response) {
                        childExclusiveModel = new ModelClass();
                        pDialog.hide();
                        Log.d(TAG, "onResponse: " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jArray = jsonObject.getJSONArray("product");
                            String redeem_button_img = null;
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jsonObject1 = jArray.getJSONObject(i);

                               *//* // json response data
                                String name = jsonObject1.getString("name");
                                String highlight = jsonObject1.getString("highlight");*//*
                                String details = jsonObject1.getString("details");
                                String text = jsonObject1.getString("text");
                                redeem_button_img = jsonObject1.getString("redeem_button_img");

                                childExclusiveModel.android_image_url = redeem_button_img;
                                String concat = details + text;
                                childExclusiveModel.setAndroid_image_url(redeem_button_img);
                                Log.d(TAG, "onResponse: details" + details);
                                Log.d(TAG, "onResponse: details" + concat);
                                childExclusiveModel.Details = concat;

                                // model class  for child  fragment

                                childExclusiveModel.name = name;
                                Log.d(TAG, "onResponse: " + name);
                                childExclusiveModel.setName(name);

                                childExclusiveModel.favPress = favresponse;
                                childExclusiveModel.Discount = highlight;
                                Log.d(TAG, "onResponse: " + highlight);

                                Log.d(TAG, "onResponse: " + details);
                                childExclusiveModel.Details = details;
                                childExclusiveModel.text = text;
                                Log.d(TAG, "onResponse: " + text);

                                String productImage = jsonObject1.getString("display_image");
                                String stripImage = jsonObject1.getString("strip_image");
                                String extension = "";

                                if (productImage.equals("Product Image")) {
                                    imageId = jsonObject1.getString("id");
                                    extension = jsonObject1.getString("image_extension");
                                    imageId = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = imageId + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_image/" + concatString;
                                    childExclusiveModel.setChild_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: productImage" + concatString);
                                } else if (productImage.equals("Merchant Logo")) {
                                    imageId = jsonObject1.getString("merchant_id");
                                    extension = jsonObject1.getString("logo_extension");
                                    imageId = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = imageId + extension;
                                    MERCHANTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + concatString;
                                    childExclusiveModel.setChild_image_url(MERCHANTIMAGEURL);
                                    Log.d(TAG, "onResponse: merchantLogo" + concatString);
                                }
                            }
                            childExclusiveModel.setAndroid_image_url(redeem_button_img);
                            childExclusiveModel.setFavPress(favresponse);
                            SetFavApiCall(String.valueOf(uname), String.valueOf(tagEx), stringToBool(favresponse));
                            childExclusiveModel.setAndroid_image_url(url);
                            modelClassList.add(childExclusiveModel);
                            connectRecyclerAdapter.notifyDataSetChanged();
                            // get course_description
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            pDialog.hide();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });

// Access the RequestQueue through your singleton class.
        queue.add(stringRequest);*/

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    ExclusiveFragment mainHomeFragment = new ExclusiveFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(android.R.id.content, mainHomeFragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void listItemClick(View view, int position) {

    }

    @Override
    public void favoriteItemclick(View view, int position) {
        childExclusiveModel =  modelClassList.get(position);
    }

    @Override
    public void productRedeemClick(View view, int position) {
        childExclusiveModel =  modelClassList.get(position);
        putRedeemdetails(userId, apiClientId, String.valueOf(tagEx));

    }

    public void putRedeemdetails(final String userid, final String cid, final String product_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest putRequest = new StringRequest(Request.Method.PUT, redeemAction,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d(TAG + "RedeemResponse", response);
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
                params.put("user_id", userid);
                params.put("cid", cid);
                params.put("product_id", product_id);
                return params;
            }

        };
        requestQueue.add(putRequest);
    }
}
