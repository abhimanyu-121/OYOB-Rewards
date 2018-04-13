package com.oyob.controller.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.oyob.controller.R;
import com.oyob.controller.activity.ActivityDashboard;
import com.oyob.controller.activity.ClickListener;
import com.oyob.controller.activity.PopularOfferWebViewActivity;
import com.oyob.controller.activity.RecyclerTouchListener;
import com.oyob.controller.adapter.CustomPagerAdapter;
import com.oyob.controller.adapter.PopularOffersAdapter;
import com.oyob.controller.model.BannerModel;
import com.oyob.controller.model.PopularOfferModal;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.Mylogger;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 121 on 9/18/2017.
 */
public class MainPageFragment extends FragmentBase implements ViewPager.OnPageChangeListener {

    String Url = null;
    List<PopularOfferModal> mainPageList = new ArrayList<>();
    ArrayList<PopularOfferModal> searchmainPageList = new ArrayList<>();
    String cid = "";
    PopularOffersAdapter mainPageAdapter;
    RecyclerView recyclerView;
    String country = "";
    private String clientId;
    private ViewPager mViewPager;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ProgressDialog pDialog;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private EditText edittext_cat_search;
    private TextView txt_category_cancel_search;
    String androidId;
    String userId = "";
    String reportDate;

    private String TAG = "MainPageFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(_context).inflate(R.layout.main_page_fragment, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Updating Offers...");

        ActivityDashboard.setTitleToolbar("");
        recyclerView = rootView.findViewById(R.id.main_page_recycler_view);
        mainPageAdapter = new PopularOffersAdapter(getActivity(), searchmainPageList);
        mViewPager = rootView.findViewById(R.id.pager);
        recyclerView.setHasFixedSize(true);
        clientId = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        pager_indicator = rootView.findViewById(R.id.viewPagerCountDots);

        androidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();
        reportDate = df.format(currentTime);
        userId = sessionManager.getParticularField(SessionManager.USER_ID);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(_context, 2);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(_context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                PopularOfferModal popularOfferModal = mainPageAdapter.getItemAtPosition(position);
                if (popularOfferModal != null) {
                    Intent intent = new Intent(getActivity(), PopularOfferWebViewActivity.class);
                    intent.putExtra("links", popularOfferModal.getLink());
                    startActivity(intent);
                }

                /*Bundle bundle = new Bundle();
                bundle.putString("client_id", cid);
                bundle.putString("cat_id", String.valueOf(searchmainPageList.get(position)));
                bundle.putString("name", searchmainPageList.get(position).getName());
                bundle.putString("q", "");
                Fragment offerListFragment = new OfferListFragment();
                offerListFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, offerListFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

                putdetails(clientId, userId, "android", androidId, null, null, searchmainPageList.get(position).getName(), reportDate, null, null);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.setAdapter(mainPageAdapter);

        edittext_cat_search = rootView.findViewById(R.id.edittext_cat_search);
        txt_category_cancel_search = rootView.findViewById(R.id.txt_category_cancel_search);
        edittext_cat_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(edittext_cat_search.getText().toString().trim());
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        edittext_cat_search.addTextChangedListener(new TextWatcher() {

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
        txt_category_cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext_cat_search.setText("");
                searchmainPageList.clear();
                searchmainPageList.addAll(mainPageList);
                mainPageAdapter.notifyDataSetChanged();
            }
        });
        getBanners();
        getCategories();

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        // inflater.inflate(R.menu.main_shop, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void performSearch(String searchCategory) {
        Mylogger.getInstance().Logit("searchdata", searchCategory);

        Bundle bundle = new Bundle();
        bundle.putString("client_id", cid);
        bundle.putString("cat_id", String.valueOf(0));
        bundle.putString("name", "Search Result");
        bundle.putString("q", searchCategory);
        Fragment offerListFragment = new OfferListFragment();
        offerListFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, offerListFragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    public void getBanners() {
        cid = sessionManager.getParticularField(SessionManager.CID);
        String client_id = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        country = sessionManager.getParticularField(SessionManager.COUNTRY);

        String url = AppConstants.SERVER_ROOT + "get_client_banner.php?" + "client_id=" + client_id;
        Mylogger.getInstance().Logit("BASEURL", url);
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Mylogger.getInstance().Logit("BASEURLRES", response.toString());
                pDialog.dismiss();
                Mylogger.getInstance().Logit(TAG, "onResponse: " + response);
                Mylogger.getInstance().Logit("BASEURLRES", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("images");

                    ArrayList<String> images = new ArrayList<>();
                    ArrayList<String> imageId = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String extension = jsonObject1.getString("big_img_extension");

                        BannerModel bannerModel = new BannerModel();
                        bannerModel.setId(id);
                        bannerModel.setBig_img_extension(extension);

                        ArrayList<BannerModel> bannerList = new ArrayList<>();
                        bannerList.add(bannerModel);

                        String imageUrl = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/big_image/" + id + "." + extension;
                        images.add(imageUrl);
                        imageId.add(id);
                    }

                    mCustomPagerAdapter = new CustomPagerAdapter(getActivity(), images, imageId);
                    mViewPager.setAdapter(mCustomPagerAdapter);
                    final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrollStateChanged(int arg0) {
                        }

                        @Override
                        public void onPageScrolled(int arg0, float arg1, int arg2) {
                        }

                        @Override
                        public void onPageSelected(int position) {

                            Log.d(TAG, "onPageSelected: ");
                            for (int i = 0; i < dotsCount; i++) {
                                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                            }

                            dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                        }
                    };
                    mViewPager.addOnPageChangeListener(pageChangeListener);
                    setUiPageViewController();
                    mViewPager.post(new Runnable() {
                        @Override
                        public void run() {
                            pageChangeListener.onPageSelected(mViewPager.getCurrentItem());
                        }
                    });

                    //getCategories();

                } catch (JSONException e) {
                    Mylogger.getInstance().Logit(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Mylogger.getInstance().Logit(TAG, error.toString());
                // hide the progress dialog
            }
        });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
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
        queue.add(jsonObjReq);
        queue.start();
    }

    public void getCategories() {
        pDialog.show();
        cid = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        country = sessionManager.getParticularField(SessionManager.COUNTRY);
        Url = AppConstants.SERVER_ROOT + "get_popular_images.php?client_id=" + cid;//+ "&country=" + country;
        Mylogger.getInstance().Logit("BASEURL", Url);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Mylogger.getInstance().Logit("BASEURLRES", response.toString());
                Log.d(TAG, response.toString());
                pDialog.dismiss();

                JSONObject preferencesJSON = null;
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Integer status = jsonObject.getInt("status");
                    if (status != null && status == 200) {
                        preferencesJSON = jsonObject.getJSONObject("Popular Offers");
                        Iterator keys = preferencesJSON.keys();
                        while (keys.hasNext()) {
                            String dynamicKey = (String) keys.next();
                            JSONObject line = preferencesJSON.getJSONObject(dynamicKey);
                            String img = line.getString("img");
                            String link = line.getString("link");

                            Mylogger.getInstance().Logit("dynamicKey", "" + dynamicKey);
                            Mylogger.getInstance().Logit("img", "" + img);
                            Mylogger.getInstance().Logit("link", "" + link);

                            PopularOfferModal offerModal = new PopularOfferModal();
                            offerModal.setName(dynamicKey);
                            offerModal.setImg(img);
                            offerModal.setLink(link);
                            mainPageList.add(offerModal);
                        }

                        searchmainPageList.clear();
                        searchmainPageList.addAll(mainPageList);
                        mainPageAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /*try {
                    JSONArray jsonarray = response.getJSONArray("cat");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String finalString = jsonobject.getString("name");
                        int id = jsonobject.getInt("id");

                        MainPageModel mainPageModel = new MainPageModel();
                        mainPageModel.setName(finalString);
                        mainPageModel.setId(id);
                        mainPageModel.setExt(jsonobject.getString("logo_extension"));
                        mainPageList.add(mainPageModel);
                        Log.d(TAG, "onResponse: " + finalString);
                    }

                    *//*Collections.sort(mainPageList, new Comparator<PopularOfferModal>() {
                        @Override
                        public int compare(PopularOfferModal o1, PopularOfferModal o2) {
                            if (o1.getId() > o2.getId()) {
                                return 1;
                            } else if (o1.getId() < o2.getId()) {
                                return -1;
                            }
                            return 0;
                        }
                    });*//*
                    searchmainPageList.clear();
                    searchmainPageList.addAll(mainPageList);
                    mainPageAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    Mylogger.getInstance().Logit(TAG, e.getMessage() + "");
                }*/
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                // hide the progress dialog
            }
        });

        queue.add(jsonObjReq);
    }


    private void setUiPageViewController() {

        dotsCount = mCustomPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        Log.d(TAG, "onPageScrolled: ");
    }

    @Override
    public void onPageSelected(int position) {

        Log.d(TAG, "onPageSelected: ");
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

        Log.d(TAG, "onPageScrollStateChanged: ");
    }

    public void putdetails(final String cid, final String uid, final String platform, final String device_id, final String category_id,
                           final String product_id, final String page, final String date_time, final String status, final String message) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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
}