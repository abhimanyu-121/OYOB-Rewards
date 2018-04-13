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
import com.oyob.controller.adapter.ConnectRecyclerAdapter;
import com.oyob.controller.interfaces.OnRecyclerViewItemClick;
import com.oyob.controller.model.ConnectModel;
import com.oyob.controller.utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper.getPref;
import static com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper.set;


/**
 * Created by Ramasamy on 9/11/2017.
 */

public class ConnectFragment extends Fragment implements OnRecyclerViewItemClick {

    static final String TAG = ConnectFragment.class.getSimpleName();
    private static final String SET_FAV = AppConstants.SERVER_ROOT + "set_favourites.php";
    public static String PRODUCTIMAGEURL = "";
    public static String MERCHANTIMAGEURL = "";
    public static String favValueConnect;
    String Url = "";
    List<ConnectModel> connectModels;
    String cid = "";
    String uname = "", country = "";
    String concatString = "";
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    String imageId = "";
    String image = "";
    String merchantImageId = "";
    int PAGELIMIT = 100;
    boolean isValidClick;
    boolean aBooleanConnect;
    ConnectModel connectModel;
    String string = "";
    Bundle bundle;
    String response;
    String UserId;
    private Context context;

    public static boolean stringToBool(String s) {
        if (s.equals("1"))
            return true;
        if (s.equals("0"))
            return false;
        throw new IllegalArgumentException(s + " is not a bool. Only 1 and 0 are.");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.connect_fragment, container, false);
        cid = getPref("client_id", getActivity());
        uname = getPref("uname", getActivity());
        country = getPref("country", getActivity());
        Log.d(TAG, "userId: " + uname);
        Log.d(TAG, "cid: " + cid);

        Url = AppConstants.SERVER_ROOT + "search_cat.php?cat_id=553&cid=" + cid + "&country=" + country + "&start=0&limit=" + PAGELIMIT + "&uid=" + uname;
        connectModels = new ArrayList<>();

        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.card_recycler_view_connect);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        final ConnectRecyclerAdapter connectRecyclerAdapter = new ConnectRecyclerAdapter(getActivity(), connectModels);
        connectRecyclerAdapter.setClickListener(this);
        rv.setAdapter(connectRecyclerAdapter);

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
                                ConnectModel connectModel = new ConnectModel();
                                //getting string values with keys- pageid and title
                                final String pageid = obj4.getString("name");
                                connectModel.name = pageid;
                                final String title = obj4.getString("highlight");
                                connectModel.Discount = title;

                                String productImage = obj4.getString("display_image");
                                bundle = new Bundle();

                                final String tagInfo = obj4.getString("id");
                                connectModel.Tag = tagInfo;
                                imageId = obj4.getString("id");

                                favValueConnect = obj4.getString("is_favourite");
                                connectModel.setFav_bg(favValueConnect);

                                String logoExtension = obj4.getString("logo_extension");
                                Log.d(TAG, "onResponse: boolean" + aBooleanConnect);
                                String id = "";
                                String extension = "";

                                String stripimage = obj4.getString("strip_image");

                                if (!logoExtension.isEmpty() && logoExtension.equalsIgnoreCase("jpg")) {
                                    imageId = obj4.getString("merchant_id");
                                    bundle.putString("merchant_id", imageId);
                                    set(getActivity(), "value1", imageId);
                                    extension = obj4.getString("logo_extension");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + concatString;
                                    connectModel.setAndroid_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: merchantLogo" + concatString);
                                } else if (!stripimage.equals("null") && !logoExtension.equals("null")) {
                                    imageId = obj4.getString("id");
                                    set(getActivity(), "value1", imageId);
                                    extension = obj4.getString("strip_image");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/strip_image/" + concatString;
                                    Log.d(TAG, "onResponse: productImage" + concatString);
                                } else if (productImage.equals("Product Image")) {
                                    imageId = obj4.getString("id");
                                    set(getActivity(), "value1", imageId);
                                    extension = obj4.getString("image_extension");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_image/" + concatString;
                                    connectModel.setAndroid_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: productImage" + concatString);
                                }
                                connectModel.setProductId(imageId);
                                connectModels.add(connectModel);
                                connectRecyclerAdapter.notifyDataSetChanged();
                                Log.d(TAG, "onCreateView: " + connectRecyclerAdapter.getItemCount());

                                Log.d(TAG, "Page id : " + pageid + " Title : " + title);


                            } // get course_description
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
                            String keyValue = (String) keys.next();
                            obj4 = jsonObject.getJSONObject(keyValue);

                            //getting string values with keys- pageid and title
                            String status = obj4.getString("200");
                            if (status.equals("success")) {
                                connectModel.setFav_bg(connectModel.getFav_bg());
                            }

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
    public void listItemClick(View view, int position) {
        set(getActivity(), "id", imageId);
        set(getActivity(), "merchant_id", merchantImageId);
        connectModel = (ConnectModel) connectModels.get(position);

        Log.i("atPosition", "Persons name: " + connectModel.name);
        Log.i("atPosition", "Persons discount : " + connectModel.Discount);
        Log.i("atPosition", "Persons url : " + connectModel.android_image_url);
        Log.d(TAG, "onClick: " + connectModel);

        bundle.putString("id", imageId);
        bundle.putString("image", image);
        bundle.putString("concatString", concatString);
        bundle.putString("name", connectModel.name);
        bundle.putString("highlight", connectModel.Discount);
        bundle.putString("url", connectModel.android_image_url);
        bundle.putString("tag", connectModel.getTag());
        bundle.putString("tagValue", connectModel.getTag());
        bundle.putString("favResponse", connectModel.getFav_bg());
        Log.d(TAG, "listItemClick: log" + connectModel.getTag());


        fragment = new ChildConnectFragment();
        fragment.setArguments(bundle);
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, null).commit();
        Log.d(TAG, "onClick: " + position);
        Log.d(TAG, "onClick: " + imageId);
        Log.d(TAG, "onClick: getTag" + connectModel.getTag());
        FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(imageId, "Product", "ConnectOffersScreen"));
    }

    @Override
    public void favoriteItemclick(View view, int position) {
        connectModel = (ConnectModel) connectModels.get(position);
        Log.d(TAG, "favoriteItemclick: " + position);
        FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(uname, "Favourite", "ConnectOffersScreen"));

        if (connectModel.getFav_bg().equals(String.valueOf(0))) {
            SetFavApiCall(String.valueOf(uname), String.valueOf(connectModel.Tag), stringToBool(String.valueOf(1)));
            set(getActivity(), "apiResponseConnect", String.valueOf(1));
            connectModel.setFav_bg(String.valueOf(1));
            Log.d(TAG, "favoriteItemclick: " + String.valueOf(1));
        } else if (connectModel.getFav_bg().equals(String.valueOf(1))) {
            SetFavApiCall(String.valueOf(uname), String.valueOf(connectModel.Tag), stringToBool(String.valueOf(0)));
            set(getActivity(), "apiResponseConnect", String.valueOf(0));
            connectModel.setFav_bg(String.valueOf(0));
            Log.d(TAG, "favoriteItemclick: " + String.valueOf(0));
        }
    }

    @Override
    public void productRedeemClick(View view, int position) {

    }
}
