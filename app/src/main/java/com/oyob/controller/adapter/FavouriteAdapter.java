package com.oyob.controller.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.oyob.controller.Analytics.FirebaseAnalyticsClass;
import com.oyob.controller.R;
import com.oyob.controller.activity.OfferDetailActivity;
import com.oyob.controller.activity.VourcherWebviewActivity;
import com.oyob.controller.model.ModelClass;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 121 on 9/25/2017.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ConnectViewHolder> {

    static final String TAG = "ExclusiveRecycleAdapter";
    private static final String SET_FAV = AppConstants.SERVER_ROOT + "set_favourites.php";
    List<ModelClass> persons;
    private String uname;
    private Context context;
    private String androidId;
    private String reportDate;
    private String cid;
    private String userId;
    private SessionManager sessionManager;

    public FavouriteAdapter(Context context, List<ModelClass> persons) {
        this.persons = persons;
        this.context = context;
        sessionManager = new SessionManager(context);
        uname = SharedPreferenceHelper.getPref("uname", context);

       /* cid = SharedPreferenceHelper.getPref("client_id", context);
        userId = SharedPreferenceHelper.getPref("userId", context);*/

        cid = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        userId = sessionManager.getParticularField(SessionManager.USER_ID);

        androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();
        reportDate = df.format(currentTime);
    }

    @Override
    public FavouriteAdapter.ConnectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exclusive_adapter, parent, false);
        ConnectViewHolder pvh = new ConnectViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final FavouriteAdapter.ConnectViewHolder holder, final int position) {

        final ModelClass modelClass = persons.get(position);
        holder.personName.setText(persons.get(position).getName());
        //holder.personAge.setText(persons.get(position).getHighlight());

        if (modelClass.getId().equals("1040543")) {
            holder.personName.setVisibility(View.GONE);
            holder.personAge.setVisibility(View.GONE);
            holder.favimageView.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.personName.setVisibility(View.VISIBLE);
            holder.personAge.setVisibility(View.VISIBLE);
            holder.favimageView.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.relativeLayout.setVisibility(View.VISIBLE);
        }

        Integer product_quantity = null;
        if (!modelClass.getProduct_quantity().isEmpty()) {
            product_quantity = Integer.parseInt(modelClass.getProduct_quantity());
        }

        Integer out_of_order = null;
        if (!modelClass.getOut_of_order().isEmpty()) {
            out_of_order = Integer.parseInt(modelClass.getOut_of_order());
        }


        final String redeem_button_img = modelClass.getRedeem_button_img();
        Integer web_coupon = Integer.parseInt(modelClass.getWeb_coupon());
        Integer user_used_count = Integer.parseInt(modelClass.getUser_used_count());
        Integer used_count = Integer.parseInt(modelClass.getUsed_count());

        if (!TextUtils.isEmpty(redeem_button_img)) {
            holder.rea_tv_get_now.setVisibility(View.VISIBLE);
            holder.rea_tv_get_now.setText("Redeem Now");
            holder.rea_tv_get_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(redeem_button_img));
                    context.startActivity(browse);
                    redeemTracker(cid, userId, modelClass.getId());
                }
            });
        } else if (product_quantity != null && (product_quantity > 0 || product_quantity == -1)) {
            holder.rea_tv_get_now.setVisibility(View.VISIBLE);
            holder.rea_tv_get_now.setText("Buy Now");
            holder.rea_tv_get_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO  - API call to add item to cart
                    addTocart(userId, modelClass.getId());
                }
            });
        } else if ((web_coupon == 1) && (user_used_count > 0 || user_used_count == -1) && (used_count > 0 || used_count == -1)) {
            holder.rea_tv_get_now.setVisibility(View.VISIBLE);
            holder.rea_tv_get_now.setText("Get Now");
            holder.rea_tv_get_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO  - API call to voucher API
                    String voucherAPI = AppConstants.SERVER_ROOT + "get_voucher.php?uid=" + userId + "&cid=" + cid + "&mid=" + modelClass.getMerchant_id() + "&pid=" + modelClass.getId();

                    Intent intent = new Intent(context, VourcherWebviewActivity.class);
                    intent.putExtra("voucherAPI", voucherAPI);
                    intent.putExtra("cid", cid);
                    intent.putExtra("pid", modelClass.getId());
                    intent.putExtra("user_id", userId);
                    context.startActivity(intent);
                }
            });
        } else if (out_of_order != null && (out_of_order ==1)) {
            holder.rea_tv_get_now.setText("Out of Stock");
        }

        else {
            //holder.rea_tv_get_now.setText("Read More");
        }

        holder.rea_tv_merchant_offer.setText(modelClass.getHighlight());
        if (modelClass.getIs_favourite().equals(String.valueOf(1))) {
            holder.favimageView.setImageResource(R.drawable.fav_color);
        } else if (modelClass.getIs_favourite().equals(String.valueOf(0))) {

            holder.favimageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }

        holder.favimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modelClass.getIs_favourite().equals(String.valueOf(1))) {

                    SetFavApiCall(uname, modelClass.getId(), false, position);
                    putdetails(cid, userId, "android", androidId, null, modelClass.getId(), "Product List", reportDate, null, "Product favourite removed");

                } else if (modelClass.getIs_favourite().equals(String.valueOf(0))) {

                    SetFavApiCall(uname, modelClass.getId(), true, position);
                }

                FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(uname, "Favourite", "ConnectOffersScreen"));

            }
        });

        if (!modelClass.getLogo_extension().equals("null") && !modelClass.getLogo_extension().isEmpty()) {

            String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + modelClass.getMerchant_id() + "." + modelClass.getLogo_extension();

            Glide.with(context).load(imageUrl).into(holder.imageView);
        } else if (!modelClass.getStrip_image().equals("null") && !modelClass.getStrip_image().isEmpty()) {
            String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/strip_image/" + modelClass.getId() + "." + modelClass.getStrip_image();
            Glide.with(context).load(imageUrl).into(holder.imageView);
        } else {
            String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_image/" + modelClass.getId() + "." + modelClass.getImage_extension();
            Glide.with(context).load(imageUrl).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (persons != null) {
            return persons.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void SetFavApiCall(final String userId, final String productId, final boolean flag, final int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Log.i("TEST", " fav url " + SET_FAV);

        StringRequest postReq = new StringRequest(Request.Method.POST, SET_FAV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                Log.i("TEST", "save api call res  " + response.toString());
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject.getJSONObject("status");
                        Log.d(TAG, "onResponse:setFavorite " + jsonObject);
                        JSONObject status = jsonObject.getJSONObject("status");
                        JSONObject obj4 = null;
                        //Getting all the keys inside json object with key- pages
                        Iterator<String> keys = status.keys();
                        while (keys.hasNext()) {
                            String keyValue = (String) keys.next();

                            if (keyValue.equals("200")) {
                                if (flag) {
                                    persons.get(position).setIs_favourite("1");
                                    notifyDataSetChanged();
                                } else {
                                    persons.get(position).setIs_favourite("0");
                                    notifyDataSetChanged();
                                }
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

                Log.i("TEST", "params " + productId);
                return params;
            }

        };


        postReq.setShouldCache(false);
        requestQueue.add(postReq);
    }

    public void putdetails(final String cid, final String uid, final String platform, final String device_id, final String category_id,
                           final String product_id, final String page, final String date_time, final String status, final String message) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("cid", cid);
            jsonObject.put("uid", uid);
            jsonObject.put("platform", platform);
            jsonObject.put("device_id", device_id);


            JSONObject actionObj = new JSONObject();

            actionObj.put("category_id", category_id);
            actionObj.put("product_id", product_id);
            actionObj.put("page", page);
            actionObj.put("datetime", date_time);
            actionObj.put("status", status);
            actionObj.put("message", message);

            jsonObject.put("action", actionObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, AppConstants.UserACtion, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("PutDetails", response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("PutDetails", "");

                    }
                });

        jsObjRequest.setShouldCache(false);
        requestQueue.add(jsObjRequest);
    }

    private void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void redeemTracker(final String cid, final String uid, final String product_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("cid", cid);
            jsonObject.put("uid", uid);
            jsonObject.put("product_id", product_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, AppConstants.redeemAction, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("PutDetails", response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("PutDetails", "");

                    }
                });


        jsObjRequest.setShouldCache(false);
        requestQueue.add(jsObjRequest);
    }

    public class ConnectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView personName;
        TextView personAge, rea_tv_merchant_offer, rea_tv_get_now;
        ImageView imageView;
        ImageView favimageView;
        CardView relativeLayout;

        ConnectViewHolder(View itemView) {
            super(itemView);

            personName = itemView.findViewById(R.id.rea_tv_merchant_name);
            personAge = itemView.findViewById(R.id.rea_tv_get_now);
            rea_tv_merchant_offer = itemView.findViewById(R.id.rea_tv_merchant_offer);
            imageView = itemView.findViewById(R.id.rea_iv_product_image);
            rea_tv_get_now = itemView.findViewById(R.id.rea_tv_get_now);
            favimageView = itemView.findViewById(R.id.rea_iv_is_favorite);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (!persons.get(getAdapterPosition()).getId().equals("1040543")) {
                Intent intent = new Intent(context, OfferDetailActivity.class);
                intent.putExtra("type", "offers");
                intent.putExtra("ProductID", persons.get(getAdapterPosition()).getId());
                context.startActivity(intent);

                FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(persons.get(getAdapterPosition()).getId(), "Product", "ConnectOffersScreen"));
            }
        }
    }

    public void addTocart(String uid, String product_id) {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Adding product to cart");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", product_id);
            jsonObject.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, AppConstants.SERVER_ROOT + "add_cart.php", jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.i("PutDetails add to cart", response.toString());
                        Log.i("TEST", jsonObject.toString());
                        try {
                            if (response.has("cart_id")) {
                                String cart_id = response.getString("cart_id");
                                System.out.println("cart_id: " + cart_id);
                                sessionManager.updateCartId(cart_id);
                                //SharedPreferenceHelper.set(getApplicationContext(), "cart_id", cart_id);
                            }

                            JSONObject obj4 = null;
                            Iterator<String> keys = response.keys();

                        } catch (Exception iox) {
                            System.out.println("iox: " + iox);
                        }
                        showAlert("Product added to cart");

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("PutDetails", "");
                        pDialog.dismiss();

                    }
                });

        jsObjRequest.setShouldCache(false);
        requestQueue.add(jsObjRequest);
    }

}
