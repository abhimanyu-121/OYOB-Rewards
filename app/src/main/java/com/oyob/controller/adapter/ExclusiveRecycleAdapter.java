package com.oyob.controller.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.oyob.controller.Analytics.FirebaseAnalyticsClass;
import com.oyob.controller.R;
import com.oyob.controller.activity.OfferDetailActivity;
import com.oyob.controller.activity.VourcherWebviewActivity;
import com.oyob.controller.model.ModelClass;
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
 * Created by 121 on 9/14/2017.
 */

public class ExclusiveRecycleAdapter extends RecyclerView.Adapter<ExclusiveRecycleAdapter.PersonViewHolder> {

    static final String TAG = "ExclusiveRecycleAdapter";
    private static final String SET_FAV = AppConstants.SERVER_ROOT + "set_favourites.php";
    SessionManager sessionManager;
    List<ModelClass> persons;
    private String uname;
    private Context context;
    private String androidId;
    private String reportDate;
    private String cid;
    private String userId;
    private String cat_id;

    public ExclusiveRecycleAdapter(Context context, List<ModelClass> persons, String cat_id) {
        this.persons = persons;
        this.context = context;
        sessionManager = new SessionManager(context);
        this.cat_id = cat_id;
        uname = sessionManager.getParticularField(SessionManager.USER_NAME);
        cid = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        userId = sessionManager.getParticularField(SessionManager.USER_ID);

        androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();
        reportDate = df.format(currentTime);
    }

    public void addData(List<ModelClass> dataViews) {
        this.persons.addAll(dataViews);
        notifyDataSetChanged();
    }

    public ModelClass getItemAtPosition(int position) {
        return persons.get(position);
    }

    public void addLoadingView() {
        //add loading item
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                persons.add(null);
                notifyItemInserted(persons.size() - 1);
            }
        });
    }

    public void removeLoadingView() {
        //Remove loading item
        persons.remove(persons.size() - 1);
        notifyItemRemoved(persons.size());
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exclusive_adapter, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int position) {
        final ModelClass modelClass = persons.get(position);
        holder.rea_tv_merchant_name.setText(persons.get(position).getName());
        holder.rea_tv_get_now.setText(persons.get(position).getHighlight());

        if (modelClass.getId().equals("1040543")) {
            holder.rea_tv_merchant_name.setVisibility(View.GONE);
            holder.rea_tv_get_now.setVisibility(View.GONE);
            holder.rea_iv_is_favorite.setVisibility(View.GONE);
            holder.card_button.setVisibility(View.GONE);
            holder.rea_iv_product_image.setVisibility(View.VISIBLE);
        } else {
            holder.rea_tv_merchant_offer.setVisibility(View.VISIBLE);
            holder.rea_tv_merchant_name.setVisibility(View.VISIBLE);
            holder.rea_tv_get_now.setVisibility(View.VISIBLE);
            holder.rea_iv_is_favorite.setVisibility(View.VISIBLE);
            holder.rea_iv_product_image.setVisibility(View.VISIBLE);
            holder.card_button.setVisibility(View.VISIBLE);
        }

        if (modelClass.getIs_favourite().equals("1")) {
            holder.rea_iv_is_favorite.setImageResource(R.drawable.fav_color);
        } else if (modelClass.getIs_favourite().equals("0")) {
            holder.rea_iv_is_favorite.setImageResource(R.drawable.fav_3x_gray);
        }
        holder.rea_tv_merchant_offer.setText(modelClass.getHighlight());


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
            holder.rea_tv_get_now.setText("Read More");
        }

        holder.rea_iv_is_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(uname, "Favourite", "ConnectOffersScreen"));

                if (modelClass.getIs_favourite().equals(String.valueOf(1))) {

                    SetFavApiCall(userId, modelClass.getId(), false, position);
                    putdetails(cid, userId, "android", androidId, cat_id, modelClass.getId(), "Product List", reportDate, null, "Product favourite removed");

                } else if (modelClass.getIs_favourite().equals(String.valueOf(0))) {

                    SetFavApiCall(userId, modelClass.getId(), true, position);
                    putdetails(cid, userId, "android", androidId, cat_id, modelClass.getId(), "Product List", reportDate, null, "Product set to favourite");
                }
            }
        });

        if (!modelClass.getLogo_extension().equals("null") && !modelClass.getLogo_extension().isEmpty()) {

            String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/" + modelClass.getMerchant_id() + "." + modelClass.getLogo_extension();
/*
            Glide.with(context).load("https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/merchant_logo/1027993.png").centerCrop()
                    .crossFade().placeholder(R.drawable.final_logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.rea_iv_product_image);
*/
            Glide.with(context).load(imageUrl).centerCrop()
                    .crossFade().placeholder(R.drawable.final_logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.rea_iv_product_image);


        } else if (!modelClass.getStrip_image().equals("null") && !modelClass.getStrip_image().isEmpty()) {
            String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/strip_image/" + modelClass.getId() + "." + modelClass.getStrip_image();
            Glide.with(context).load(imageUrl).centerCrop()
                    .crossFade().placeholder(R.drawable.final_logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.rea_iv_product_image);
        } else {
            String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/product_image/" + modelClass.getId() + "." + modelClass.getImage_extension();
            Glide.with(context).load(imageUrl).centerCrop()
                    .crossFade().placeholder(R.drawable.final_logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.rea_iv_product_image);
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

        alertDialog.show();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rea_tv_merchant_name, rea_tv_merchant_offer;
        TextView rea_tv_get_now;
        ImageView rea_iv_product_image;
        ImageView rea_iv_is_favorite;
        CardView card_button;

        PersonViewHolder(View itemView) {
            super(itemView);
            rea_tv_merchant_name = itemView.findViewById(R.id.rea_tv_merchant_name);
            rea_tv_merchant_offer = itemView.findViewById(R.id.rea_tv_merchant_offer);
            rea_tv_get_now = itemView.findViewById(R.id.rea_tv_get_now);
            rea_iv_product_image = itemView.findViewById(R.id.rea_iv_product_image);
            rea_iv_is_favorite = itemView.findViewById(R.id.rea_iv_is_favorite);
            card_button = itemView.findViewById(R.id.relativeLayout);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if (!persons.get(getAdapterPosition()).getId().equals("1040543")) {
                Intent intent = new Intent(context, OfferDetailActivity.class);
                intent.putExtra("type", "offers");
                intent.putExtra("cat_id", cat_id);
                intent.putExtra("ProductID", persons.get(getAdapterPosition()).getId());
                context.startActivity(intent);

                FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(persons.get(getAdapterPosition()).getId(), "Product", "ConnectOffersScreen"));

            }
        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }


}
