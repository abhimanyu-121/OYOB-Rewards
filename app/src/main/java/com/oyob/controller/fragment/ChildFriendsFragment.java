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
import com.oyob.controller.adapter.ChildFriendsProductAdapter;
import com.oyob.controller.model.ChildFriendsModel;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.AppConstants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 121 on 9/19/2017.
 */

public class ChildFriendsFragment extends Fragment {
    String productUrl = null;
    List<ChildFriendsModel> modelClassList;
    Context context;
    String cid = "";
    String ProductID = "";
    public static String PRODUCTIMAGEURL = "";
    public static String MERCHANTIMAGEURL = "";
    String imageId = "";
    String concatString = "";

    public static final String TAG = ChildFriendsFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.child_friends_layout, container, false);
        modelClassList = new ArrayList<>();

        Bundle bundle = getArguments();
        String id = bundle.getString("id");
        String actual = bundle.getString("image");
        final String test = bundle.getString("test");
        final String tag = bundle.getString("tag");
        final String name = bundle.getString("name");
        final String highlight = bundle.getString("highlight");
        final String url = bundle.getString("url");


        /*id = SharedPreferenceHelper.getPref("value1",getActivity());*/
        cid = SharedPreferenceHelper.getPref("client_id", getActivity());
        ProductID = SharedPreferenceHelper.getPref("value1", getActivity());

        productUrl = AppConstants.SERVER_ROOT + "get_product.php?client_id=" + cid + "&id=" + tag;


        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.card_recycler_view_child_friends);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);


        final ChildFriendsProductAdapter childFriendsProductAdapter = new ChildFriendsProductAdapter(getActivity(), modelClassList);
        rv.setAdapter(childFriendsProductAdapter);


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, productUrl,
                new Response.Listener<String>() {
                    ChildFriendsModel childFriendsModel = new ChildFriendsModel();

                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();

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

                                // model class  for child  fragment

                                childFriendsModel.name = name;
                                Log.d(TAG, "onResponse: name" + name);

                                childFriendsModel.Discount = highlight;
                                Log.d(TAG, "onResponse: highlight" + highlight);

                                Log.d(TAG, "onResponse: details" + details);
                                childFriendsModel.Details = details;

                                childFriendsModel.text = text;
                                Log.d(TAG, "onResponse: text " + text);


                                String productImage = jsonObject1.getString("display_image");
                                String extension = "";


                                if (productImage.equals("Product Image")) {
                                    imageId = jsonObject1.getString("id");
                                    extension = jsonObject1.getString("image_extension");
                                    imageId = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = imageId + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_image/" + concatString;
                                    childFriendsModel.setChild_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: productImage" + concatString);
                                } else if (productImage.equals("Merchant Logo")) {
                                    imageId = jsonObject1.getString("merchant_id");
                                    extension = jsonObject1.getString("logo_extension");
                                    imageId = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = imageId + extension;
                                    MERCHANTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + concatString;
                                    childFriendsModel.setChild_image_url(MERCHANTIMAGEURL);

                                    Log.d(TAG, "onResponse: merchantLogo" + concatString);
                                }
                            }
                            childFriendsModel.setAndroid_image_url(url);
                            modelClassList.add(childFriendsModel);
                            childFriendsProductAdapter.notifyDataSetChanged();

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

                    FriendsFragment mainHomeFragment = new FriendsFragment();
                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(android.R.id.content, mainHomeFragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
    }
}