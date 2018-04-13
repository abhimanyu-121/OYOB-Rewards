package com.oyob.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oyob.controller.R;
import com.oyob.controller.adapter.ExclusiveRecycleAdapter;
import com.oyob.controller.interfaces.OnRecyclerViewItemClick;
import com.oyob.controller.model.ModelClass;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.AppConstants;


import java.util.ArrayList;
import java.util.List;

import static com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper.getPref;


/**
 * Created by Ramasamy on 9/11/2017.
 */

public class ExclusiveFragment extends Fragment implements OnRecyclerViewItemClick {


    private Context context;
    String Url = null;
    List<ModelClass> modelClassList;
    String cid = "";

    public static String PRODUCTIMAGEURL = "";
    public static String MERCHANTIMAGEURL = "";

    ModelClass atPosition;
    String string = "";

    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    String imageId = "";
    String image = "";
    String uname = "", country = "";
    Bundle bundle;
    static final String TAG = ExclusiveFragment.class.getSimpleName();
    int PAGELIMIT = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.exclusive_fragment, container, false);
        cid = getPref("client_id", getActivity());
        uname = getPref("uname", getActivity());
        country = getPref("country", getActivity());
        Url = AppConstants.SERVER_ROOT + "search_cat.php?cat_id=552&cid=" + cid + "&country=" + country + "&start=0&limit=" + PAGELIMIT + "&uid=" + uname;
        modelClassList = new ArrayList<>();

        final RecyclerView rv =  rootView.findViewById(R.id.card_recycler_view);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        final ExclusiveRecycleAdapter rvAdapter = new ExclusiveRecycleAdapter(getActivity(), modelClassList, "");
        // rvAdapter.setClickListener(this);
        rv.setAdapter(rvAdapter);

        /*RequestQueue queue = Volley.newRequestQueue(getActivity());
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

                                ModelClass modelClass = new ModelClass();
                                //getting string values with keys- pageid and title
                                String pageid = obj4.getString("name");
                                modelClass.name = pageid;
                                String title = obj4.getString("highlight");
                                modelClass.Discount = title;

                                final String tagInfo = obj4.getString("id");
                                modelClass.Tag = tagInfo;
                                modelClass.setTag(tagInfo);

                                imageId = obj4.getString("id");

                                favValue = obj4.getString("is_favourite");

                                Log.d(TAG, "onResponse: boolean" + aBoolean);
                               *//* SharedPreferenceHelper.set(getActivity(), "apiResponse", favValue);
                                SharedPreferenceHelper.set(getActivity(), "favValue", favValue);*//*
                                modelClass.setFavPress(favValue);

                                String productImage = obj4.getString("display_image");
                                String logoExtension = obj4.getString("logo_extension");
                                 bundle = new Bundle();
                                String stripimage = obj4.getString("strip_image");

                                String extension = "";

                                if (!logoExtension.isEmpty()) {
                                    imageId = obj4.getString("merchant_id");
                                    set(getActivity(),"value1",imageId);
                                    extension = obj4.getString("logo_extension");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + concatString;
                                    modelClass.setAndroid_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: merchantLogo" + concatString);
                                }
                                else if (!stripimage.equals("null") && !logoExtension.equals("null")) {
                                    imageId = obj4.getString("id");

                                    *//*connectModel.setTag(imageId);*//*
                                    set(getActivity(), "value1", imageId);
                                     logoExtension = obj4.getString("logo_extension");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + logoExtension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/strip_image/" + concatString;
                                     modelClass.setAndroid_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: productImage" + concatString);
                                } else if (productImage.equals("Product Image")) {
                                    imageId = obj4.getString("id");
                                    set(getActivity(),"value1",imageId);
                                    extension = obj4.getString("image_extension");
                                    image = new StringBuilder(imageId).insert(imageId.length(), ".").toString();
                                    concatString = image + extension;
                                    PRODUCTIMAGEURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_image/" + concatString;
                                    modelClass.setAndroid_image_url(PRODUCTIMAGEURL);
                                    Log.d(TAG, "onResponse: productImage" + concatString);
                                }

                                modelClassList.add(modelClass);
                                rvAdapter.notifyDataSetChanged();
                                Log.d(TAG, "Page id : " + pageid + " Title : " + title);

                            }
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
        queue.add(jsObjRequest);*/

        return rootView;
    }

/*
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
                                atPosition.setFavPress(atPosition.getFavPress());
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
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", userId);
                params.put("pid", productId);
                params.put("flag", String.valueOf(flag));
                Log.d(TAG, "getParams: "+params);
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
*/

    @Override
    public void listItemClick(View view, int position) {
/*
        atPosition = (ModelClass) modelClassList.get(position);
        imageId = getPref("value1",getActivity());
        Log.i("atPosition", "Persons name: " + atPosition.name);
        Log.i("atPosition", "Persons discount : " + atPosition.Discount);
        Log.i("atPosition", "Persons url : " + atPosition.android_image_url);
        bundle.putString("id", imageId);
        bundle.putString("image",image);
        bundle.putString("tagValue",atPosition.getTag());
        bundle.putString("concatString",concatString);
        bundle.putString("name", atPosition.name);
        SharedPreferenceHelper.set(getContext(),"pagername",atPosition.getName());
        */
/*bundle.putString("favValue", String.valueOf(atPosition.isPresssed));*//*

        bundle.putString("highlight", atPosition.Discount);

        bundle.putString("url", atPosition.android_image_url);
        bundle.putString("tagExclusive", atPosition.getTag());
        bundle.putString("favData",atPosition.getFavPress());
        */
/*bundle.putString("position",atPosition);*//*

        fragment = new ChildExclusiveFragment();
        fragment.setArguments(bundle);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, null).commit();
        Log.d(TAG, "onClick: " + position);
        Log.d(TAG, "onClick: " + imageId);
*/
    }

    @Override
    public void favoriteItemclick(View view, int position) {
        atPosition = (ModelClass) modelClassList.get(position);

/*
        if(atPosition.getFavPress().equals(String.valueOf(0))){
            */
/*changeaBoolean= favSuccessResult;*//*

            SetFavApiCall(String.valueOf(uname), String.valueOf(atPosition.Tag), stringToBool(String.valueOf(1)));
            SharedPreferenceHelper.set(getActivity(),"apiResponse",String.valueOf(1));
            atPosition.setFavPress(String.valueOf(1));
            Log.d(TAG, "favoriteItemclick: "+String.valueOf(1));
        }
        else if(atPosition.getFavPress().equals(String.valueOf(1))) {
            SetFavApiCall(String.valueOf(uname), String.valueOf(atPosition.Tag), stringToBool(String.valueOf(0)));
            SharedPreferenceHelper.set(getActivity(),"apiResponse",String.valueOf(0));
            Log.d(TAG, "favoriteItemclick: "+String.valueOf(0));
            atPosition.setFavPress(String.valueOf(0));
        }
        Log.d(TAG, "onClick: getfav" + favValue);
*/
    }

    @Override
    public void productRedeemClick(View view, int position) {

    }

}
