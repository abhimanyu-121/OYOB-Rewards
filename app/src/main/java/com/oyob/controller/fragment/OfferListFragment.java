package com.oyob.controller.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.oyob.controller.R;
import com.oyob.controller.activity.ActivityDashboard;
import com.oyob.controller.activity.MyShopActivity;
import com.oyob.controller.adapter.ExclusiveRecycleAdapter;
import com.oyob.controller.interfaces.OnLoadMoreListener;
import com.oyob.controller.interfaces.RecyclerViewLoadMoreScroll;
import com.oyob.controller.model.ModelClass;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.Mylogger;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by 121 on 9/18/2017.
 */
public class OfferListFragment extends FragmentBase {

    static final String TAG = OfferListFragment.class.getSimpleName();
    private static int pageNumber = 0;
    private static int PAGELIMIT = 10;
    Fragment fragment;
    String bunbleCustId = null, title;
    TextView txtViewCount;
    private String flag;
    private EditText edittext_search;
    private TextView cancel_search;
    private String Url, uname, cat_id, query, user_id, country = "";
    private ExclusiveRecycleAdapter rvAdapter;
    private RecyclerView rv;
    private RecyclerViewLoadMoreScroll scrollListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(_context).inflate(R.layout.activity_product_list, container, false);

        bunbleCustId = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        cat_id = getArguments().getString("cat_id");
        title = getArguments().getString("name");
        uname = sessionManager.getParticularField(SessionManager.USER_NAME);
        user_id = sessionManager.getParticularField(SessionManager.USER_ID);
        query = getArguments().getString("q");
        flag = getArguments().getString("flag");
        country = sessionManager.getParticularField(SessionManager.COUNTRY);

        edittext_search = rootView.findViewById(R.id.edittext_product_search);
        cancel_search = rootView.findViewById(R.id.txt_category_product_cancel_search);

        ActivityDashboard.setTitleToolbar(title);

        AppConstants.getCardItemCount(getActivity());

        if (!TextUtils.isEmpty(query)) {
            Url = AppConstants.SERVER_ROOT + "search_cat.php?q=" + query + "&cid=" + bunbleCustId + "&country=" + country + "&start=" + pageNumber + "&limit=" + PAGELIMIT + "&uid=" + user_id;
        } else {
            Url = AppConstants.SERVER_ROOT + "search_cat.php?cat_id=" + cat_id + "&cid=" + bunbleCustId + "&country=" + country + "&start=" + pageNumber + "&limit=" + PAGELIMIT + "&uid=" + user_id;
        }

        Mylogger.getInstance().Logit("BASEURL", Url);

        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        backArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        rv = rootView.findViewById(R.id.card_recycler_view);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(_context);
        rv.setLayoutManager(llm);

        rvAdapter = new ExclusiveRecycleAdapter(_context, AppConstants.offerList, cat_id);
        rv.setAdapter(rvAdapter);
        AppConstants.offerList.clear();

        scrollListener = new RecyclerViewLoadMoreScroll(llm);
        scrollListener.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNumber = pageNumber + 10;
                if (!TextUtils.isEmpty(query)) {
                    Url = AppConstants.SERVER_ROOT + "search_cat.php?q=" + query + "&cid=" + bunbleCustId + "&country=" + country + "&start=" + pageNumber + "&limit=" + PAGELIMIT + "&uid=" + user_id;
                } else {
                    Url = AppConstants.SERVER_ROOT + "search_cat.php?cat_id=" + cat_id + "&cid=" + bunbleCustId + "&country=" + country + "&start=" + pageNumber + "&limit=" + PAGELIMIT + "&uid=" + user_id;
                }
                LoadMoreData();
            }
        });

        rv.addOnScrollListener(scrollListener);

        edittext_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!edittext_search.getText().toString().isEmpty()) {
                        performSearch(edittext_search.getText().toString().trim());
                        if (v != null) {
                            InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        edittext_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext_search.setText("");
            }
        });

        if (flag.equalsIgnoreCase("false")) {
            getOfferList();
        } else if ((flag.equalsIgnoreCase("true"))) {
            getOfferListSearch();
        }
        return rootView;
    }

    private void LoadMoreData() {
        // rvAdapter.addLoadingView();
        getOfferList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_shop, menu);
        super.onCreateOptionsMenu(menu, inflater);

        final View notificaitons = menu.findItem(R.id.action_myshop).getActionView();
        txtViewCount = notificaitons.findViewById(R.id.txtCount);
        final int count = Integer.parseInt(ActivityDashboard.cartCount);
        Log.i("TEST", "count   " + count);
        updateHotCount(count);
        txtViewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // updateHotCount(count);


            }
        });
        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    TODO
                if (count > 0) {
                    Fragment newFragment = new MyShopActivity();
                    android.support.v4.app.FragmentTransaction ft = ((AppCompatActivity) _context).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, newFragment, "").commit();
                } else {
                    Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateHotCount(final int count) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (count == 0)
                    txtViewCount.setVisibility(View.GONE);
                else {
                    txtViewCount.setVisibility(View.VISIBLE);
                    txtViewCount.setText(String.valueOf(count));
                    // supportInvalidateOptionsMenu();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_myshop:
                Fragment myshop = new MyShopActivity();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, myshop); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
                break;
        }
        return true;
    }

    public void getOfferList() {
        Mylogger.getInstance().Logit(TAG, "getOfferList");
        Mylogger.getInstance().Logit(TAG, Url);
        RequestQueue queue = Volley.newRequestQueue(_context);
        final ProgressDialog pDialog = new ProgressDialog(_context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.i("TEST", "url offer  " + Url + "&response_type=json");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Url + "&response_type=json", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        Mylogger.getInstance().Logit(TAG, "onResponse: " + response);

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
                                String keyValue = keys.next();
                                obj4 = jsonObject.getJSONObject(keyValue);

                                ModelClass modelClass = new ModelClass();
                                //getting string values with keys- pageid and title
                                String name = obj4.getString("name");
                                String highlight = obj4.getString("highlight");
                                String id = obj4.getString("id");
                                String favValue = obj4.getString("is_favourite");
                                String display_image = obj4.getString("display_image");
                                String logoExtension = obj4.getString("logo_extension");
                                String stripimage = obj4.getString("strip_image");
                                String merchant_id = obj4.getString("merchant_id");
                                String offer = obj4.getString("offer");
                                String image_extension = obj4.getString("image_extension");

                                modelClass.setName(name);
                                modelClass.setHighlight(highlight);
                                modelClass.setId(id);
                                modelClass.setIs_favourite(favValue);
                                modelClass.setOut_of_order(obj4.getString("out_of_stock"));
                                modelClass.setDisplay_image(display_image);
                                modelClass.setLogo_extension(logoExtension);
                                modelClass.setStrip_image(stripimage);
                                modelClass.setMerchant_id(merchant_id);
                                modelClass.setOffer(offer);
                                modelClass.setImage_extension(image_extension);
                                modelClass.setRedeem_button_img(obj4.getString("redeem_button_img"));
                                modelClass.setProduct_quantity(obj4.getString("product_quantity"));
                                modelClass.setWeb_coupon(obj4.getString("web_coupon"));
                                modelClass.setUser_used_count(obj4.getString("user_used_count"));
                                modelClass.setUsed_count(obj4.getString("used_count"));
                                AppConstants.offerList.add(modelClass);

                            }

                            rvAdapter.notifyDataSetChanged();


                            // get course_description
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        rvAdapter.notifyDataSetChanged();
                        scrollListener.setLoaded();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        pDialog.dismiss();
                        Toast.makeText(_context, "No such product is available.", Toast.LENGTH_LONG).show();

                    }
                });
        jsObjRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest);

    }

    private void performSearch(String searchCategory) {
        Mylogger.getInstance().Logit("searchdata", searchCategory);

        Bundle bundle = new Bundle();
        bundle.putString("cat_id", cat_id);
        bundle.putString("name", "Search Result");
        bundle.putString("q", edittext_search.getText().toString().trim());
        bundle.putString("flag", "true");
        Fragment offerListFragment = new OfferListFragment();
        offerListFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, offerListFragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    public void getOfferListSearch() {
        Log.i("TEST", "called");
        String Url_Search = AppConstants.SERVER_ROOT + "search_cat.php?q=" + query + "&cat_id=" + cat_id + "&cid=" + bunbleCustId + "&country=" + country + "&start=" + pageNumber + "&limit=" + PAGELIMIT + "&uid=" + user_id;

        Mylogger.getInstance().Logit(TAG, "getOfferList");
        Mylogger.getInstance().Logit(TAG, Url);
        RequestQueue queue = Volley.newRequestQueue(_context);
        final ProgressDialog pDialog = new ProgressDialog(_context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Url_Search + "&response_type=json", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("TEST", "on ser " + response);
                        pDialog.dismiss();
                        Mylogger.getInstance().Logit(TAG, "onResponse: " + response);
                        Log.i("TEST", " ser " + response.toString());
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
                                String name = obj4.getString("name");
                                String highlight = obj4.getString("highlight");
                                String id = obj4.getString("id");
                                String favValue = obj4.getString("is_favourite");
                                String display_image = obj4.getString("display_image");
                                String logoExtension = obj4.getString("logo_extension");
                                String stripimage = obj4.getString("strip_image");
                                String merchant_id = obj4.getString("merchant_id");
                                String offer = obj4.getString("offer");
                                String image_extension = obj4.getString("image_extension");

                                modelClass.setName(name);
                                modelClass.setHighlight(highlight);
                                modelClass.setId(id);
                                modelClass.setIs_favourite(favValue);
                                modelClass.setDisplay_image(display_image);
                                modelClass.setLogo_extension(logoExtension);
                                modelClass.setStrip_image(stripimage);
                                modelClass.setMerchant_id(merchant_id);
                                modelClass.setOffer(offer);
                                modelClass.setImage_extension(image_extension);

                                modelClass.setRedeem_button_img(obj4.getString("redeem_button_img"));
                                modelClass.setProduct_quantity(obj4.getString("product_quantity"));
                                modelClass.setWeb_coupon(obj4.getString("web_coupon"));
                                modelClass.setUser_used_count(obj4.getString("user_used_count"));
                                modelClass.setUsed_count(obj4.getString("used_count"));

                                AppConstants.offerList.add(modelClass);

                            }

                            rvAdapter.notifyDataSetChanged();


                            // get course_description
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        rvAdapter.notifyDataSetChanged();
                        scrollListener.setLoaded();
                        /*rvAdapter.removeLoadingView();
                        rvAdapter.addData(offerList);
                        rvAdapter.notifyDataSetChanged();
                        scrollListener.setLoaded();*/
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        pDialog.dismiss();
                        Toast.makeText(_context, "No such product is available.", Toast.LENGTH_LONG).show();

                    }
                });
        jsObjRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest);

    }


}
