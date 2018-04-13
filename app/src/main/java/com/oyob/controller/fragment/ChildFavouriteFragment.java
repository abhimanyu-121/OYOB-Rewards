package com.oyob.controller.fragment;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oyob.controller.R;
import com.oyob.controller.adapter.ChildFavouriteFragmentAdapter;
import com.oyob.controller.interfaces.OnRecyclerViewItemClick;
import com.oyob.controller.model.ChildFavouriteModel;
import com.oyob.controller.model.ConnectModel;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper.getPref;
import static com.oyob.controller.utils.AppConstants.redeemAction;


/**
 * Created by 121 on 9/25/2017.
 */

public class ChildFavouriteFragment extends FragmentBase implements OnRecyclerViewItemClick {

    public static final String TAG = ChildFavouriteFragment.class.getSimpleName();
    private static final String SET_FAV = AppConstants.SERVER_ROOT + "set_favourites.php";
    public static String PRODUCTIMAGEURL = "";
    public static String MERCHANTIMAGEURL = "";
    String productUrl = null;
    List<ChildFavouriteModel> modelClassList;
    String cid = "";
    String ProductID = "";
    String uname = "";
    String imageId = "";
    String concatString = "";
    ChildFavouriteModel childFavouriteModel;
    String tagEx;
    boolean aBoolean;
    String userId = "";
    String apiClientId = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Team B");

        View rootView = LayoutInflater.from(_context).inflate(R.layout.child_favourite, container, false);

        apiClientId = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        userId = sessionManager.getParticularField(SessionManager.USER_ID);
        modelClassList = new ArrayList<>();
        Bundle bundle = getArguments();
        uname = sessionManager.getParticularField(SessionManager.USER_NAME);

        final String tag = bundle.getString("tag");
        final String name = bundle.getString("name");
        final String highlight = bundle.getString("highlight");
        final String url = bundle.getString("url");

        cid = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        ProductID = sessionManager.getParticularField(SessionManager.PRODUCT_ID);

        productUrl = AppConstants.SERVER_ROOT + "get_product.php?client_id=" + cid + "&id=" + tag;

        final RecyclerView rv = rootView.findViewById(R.id.card_recycler_view_child_favourite);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        final ChildFavouriteFragmentAdapter childFavouriteFragmentAdapter = new ChildFavouriteFragmentAdapter(getActivity(), modelClassList);
        childFavouriteFragmentAdapter.setClickListener(this);
        rv.setAdapter(childFavouriteFragmentAdapter);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, productUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        childFavouriteModel = new ChildFavouriteModel();
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jArray = jsonObject.getJSONArray("product");
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jsonObject1 = jArray.getJSONObject(i);
                              /*// json response data
                              String name = jsonObject1.getString("name");
                              String highlight = jsonObject1.getString("highlight");*/
                                String details = jsonObject1.getString("details");
                                String text = jsonObject1.getString("text");

                                String concat = details + text;
                                Log.d(TAG, "onResponse: details" + details);
                                childFavouriteModel.Details = concat;

                                // model class  for child  fragment
                                childFavouriteModel.name = name;
                                Log.d(TAG, "onResponse: name" + name);

                                childFavouriteModel.Discount = highlight;
                                Log.d(TAG, "onResponse: highlight" + highlight);

                                childFavouriteModel.text = text;
                                Log.d(TAG, "onResponse: text " + text);


                                String productImage = jsonObject1.getString("display_image");
                                String stripImage = jsonObject1.getString("strip_image");

                                Log.d(TAG, "onResponse: " + stripImage);
                                String extension = "";

                                if (productImage.equals("Merchant Logo")) {
                                    imageId = jsonObject1.getString("merchant_id");
                                    extension = jsonObject1.getString("logo_extension");
                                    imageId = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = imageId + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + concatString;
                                    childFavouriteModel.setChild_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: merchantLogo" + concatString);
                                } else if (productImage.equals("Product Image")) {
                                    imageId = jsonObject1.getString("id");
                                    extension = jsonObject1.getString("image_extension");
                                    imageId = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = imageId + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_image/" + concatString;
                                    childFavouriteModel.setChild_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: productImage" + concatString);
                                }
                            }
                            childFavouriteModel.setAndroid_image_url(url);
                            /*childFavouriteModel.setFav_image(fav);*/
                            modelClassList.add(childFavouriteModel);
                            childFavouriteFragmentAdapter.notifyDataSetChanged();

                            // get course_description
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
        queue.add(stringRequest);

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

                    FavouriteFragment mainHomeFragment = new FavouriteFragment();
                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(android.R.id.content, mainHomeFragment);
                    fragmentTransaction.commit();
                    Log.d(TAG, "onKey: " + "back key presses in child connect fragment");

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
        childFavouriteModel = modelClassList.get(position);

        if (getPref("apiResponse", getActivity()).equals(String.valueOf(0))) {

            SetFavApiCall(String.valueOf(uname), String.valueOf(tagEx), ConnectFragment.stringToBool(String.valueOf(1)));
            SharedPreferenceHelper.set(getActivity(), "apiResponse", String.valueOf(1));
            Log.d(TAG, "favoriteItemclick: " + String.valueOf(1));
        } else if (getPref("apiResponse", getActivity()).equals(String.valueOf(1))) {
            SetFavApiCall(String.valueOf(uname), String.valueOf(tagEx), ConnectFragment.stringToBool(String.valueOf(0)));
            SharedPreferenceHelper.set(getActivity(), "apiResponse", String.valueOf(0));
            Log.d(TAG, "favoriteItemclick: " + String.valueOf(0));
        }
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
                            ConnectModel connectModel = new ConnectModel();
                            //getting string values with keys- pageid and title
                            String status = obj4.getString("200");
                            if (status.equals("success")) {
                              /*if(favValue.equalsIgnoreCase("false")) {
                                   SharedPreferenceHelper.set(getActivity(), "SuccessState", favValue);
                               }
                               else  if(favValue.equalsIgnoreCase("true")) {
                                   SharedPreferenceHelper.set(getActivity(), "SuccessState", favValue);
                               }*/
                            } else {
                                SharedPreferenceHelper.set(getActivity(), "Failure", aBoolean);
                            }
                           /*aBoolean = !aBoolean;*/
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
                Log.d(TAG, "getParams: " + params);
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

    @Override
    public void productRedeemClick(View view, int position) {
        childFavouriteModel = (ChildFavouriteModel) modelClassList.get(position);
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
