package com.oyob.controller.activity;

import android.support.v7.app.AppCompatActivity;


/**
 * Created by Trush on 12/03/16.
 */
public class Redeem extends AppCompatActivity {
   /* private static final int REQUEST_REDEEM_POINT = 2;
    private static final int REQUEST_MANUAL_CHECK_IN_OUT = 3;
    private static final int REQUEST_REDEEM_NOTIFICATION_POST = 4;

    private GoogleMap gmap;
    Support support = new Support();
    RewardInfo item;
    SharedPreference sp;

    List<RewardInfo> items;
    private int index;
    private int points;

    private TextView mSubDescription, termCondition;
    private MapFragment mf;
    private TextView tcButton;
    private Constant constant = new Constant();

    private TextView mPromoCodeTextView, mTvCheckin_out;

    private Button mRedeemButton;
    private LinearLayout mLLCheckin;

    private int mRewardIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        sp = new SharedPreference(this);
        initToolbar();

        Bundle bundle = getIntent().getExtras();
        String d = bundle.getString(Constant.REWARD_LIST);
        points = bundle.getInt(Constant.POINTS);

        index = getIntent().getIntExtra(Constant.INDEX, 0);
        items = new Gson().fromJson(d, new TypeToken<List<RewardInfo>>() {
        }.getType());
        item = items.get(index);
        mRewardIndex = bundle.getInt(Constant.REWARD_INDEX);
        findViews();
        initializeMap(28, 80);
//        bindData();

    }

    private void bindData() { //1
        ((RewardRowView) findViewById(R.id.reward_row)).setDetails(mRewardIndex, mRewardIndex, getSupportFragmentManager(),
                items, new RewardRowView.RewardRowViewListener() {
                    @Override
                    public void onPageChanged(int position) {
                        item = items.get(position);
                        showHideTC();
                        if (!item.isWalkedIn()) {
                            mTvCheckin_out.setText("Check In");
                        } else {
                            mTvCheckin_out.setText("Check Out");
                        }

                        switch (item.getRewardType()) {
                            case Constant.RewardType.SHARED:
                                if (item.isWalkedIn()) { //item.isRedeemInStore() &&
                                    mRedeemButton.setVisibility(View.VISIBLE);
                                } else {
                                    mRedeemButton.setVisibility(View.GONE);
                                }
                                break;
                            case Constant.RewardType.BRAND:
                                String[] offerType = constant.OFFER_TYPE;
                                if (item.isInStoreReward() && item.isWalkedIn()) { //(item.getRewardSubType() != null && item.getRewardSubType().equals(offerType[1])) ||
                                    mRedeemButton.setVisibility(View.VISIBLE);
                                } else {
                                    mRedeemButton.setVisibility(View.GONE);
                                }
                                break;
                            case Constant.RewardType.SOCIAL:
                                mRedeemButton.setVisibility(View.GONE);
                                break;
                        }
                        updateMap();
                    }

                    @Override
                    public void onRewarddetailsClicked(List<RewardInfo> mItemRewardInfoList, int pos) {
                        //Toast.makeText(Redeem.this, "On Rewards", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void showHideTC() {
        Log.d("termCondition", item.getTermsAndConditions() + "");
        if (item.getTermsAndConditions() == null || item.getTermsAndConditions().isEmpty()) {
            tcButton.setVisibility(View.GONE);
            termCondition.setVisibility(View.GONE);
        } else {
            termCondition.setText(item.getTermsAndConditions());
            if (tcButton.getVisibility() != View.VISIBLE) {
                tcButton.setVisibility(View.VISIBLE);
            }
        }
        if (item.getSubDesc() != null) {
            mSubDescription.setVisibility(View.VISIBLE);
            mSubDescription.setText(item.getSubDesc());
        } else {
            mSubDescription.setText("");
            mSubDescription.setVisibility(View.INVISIBLE);
        }
        if (item.getPromoCode() != null) {
            mPromoCodeTextView.setText("CODE: " + item.getPromoCode());
            mPromoCodeTextView.setVisibility(View.VISIBLE);
        } else {
            mPromoCodeTextView.setVisibility(View.INVISIBLE);
        }

    }

    void updateMap() {
        if (item.getBrand().getLocation() != null) {
            LocationModel lm = item.getBrand().getLocation();
            if (gmap != null) {
                gmap.clear();
                if (lm.getAddress() != null) {
                    LatLng latLng = getLocationFromAddress(Redeem.this, lm.getAddress());
                    if (latLng != null) {
                        Log.d(Constant.TAG, "LATLNG:" + latLng.latitude + "," + latLng.longitude);

                        MarkerOptions mo = new MarkerOptions().position(latLng).title(lm.getAddress());
                        gmap.addMarker(mo);
                        gmap.getUiSettings().setZoomControlsEnabled(false);
                        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                        gmap.moveCamera(center);

                        CameraPosition cameraposition = new CameraPosition.Builder().target(latLng).tilt(30).zoom(15).build();
                        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraposition));
                    }
                } else {
                    gmap.clear();
                    LatLng latlng = new LatLng(28, 80);
                    CameraPosition cameraposition = new CameraPosition.Builder().target(latlng).tilt(30).zoom(15).build();
                    gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraposition));
                }
            }

        }
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return p1;
    }

    private void findViews() {
        mTvCheckin_out = (TextView) findViewById(R.id.activity_reddem_checkin_out_tv);
        mPromoCodeTextView = (TextView) findViewById(R.id.promo_code);
        mRedeemButton = (Button) findViewById(R.id.bRedeem);
        mLLCheckin = (LinearLayout) findViewById(R.id.activity_reddem_checkin_ll);
        tcButton = (TextView) findViewById(R.id.termc);
        mSubDescription = (TextView) findViewById(R.id.desc2);
        termCondition = (TextView) findViewById(R.id.termc_text);
        termCondition.setMovementMethod(new ScrollingMovementMethod());

        mRedeemButton.setOnClickListener(this);
        mLLCheckin.setOnClickListener(this);
        tcButton.setOnClickListener(this);

    }

    private void initializeMap(final double lat, final double lng) {
        mf = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));

        if (mf != null) {
            mf.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    gmap = googleMap;
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                   *//* if (ActivityCompat.checkSelfPermission(Redeem.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Redeem.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }*//*
                    //googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMapToolbarEnabled(false);

                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
                    googleMap.moveCamera(center);

                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);

                    LatLng latlng = new LatLng(lat, lng);
                    CameraPosition cameraposition = new CameraPosition.Builder().target(latlng).tilt(30).zoom(15).build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraposition));

//                    updateMap();
                    bindData();
                }
            });//.getMap();gmap =
           *//* gmap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            gmap.setMyLocationEnabled(true);
            gmap.getUiSettings().setMapToolbarEnabled(false);

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
            gmap.moveCamera(center);

            gmap.getUiSettings().setMyLocationButtonEnabled(false);

            LatLng latlng = new LatLng(lat, lng);
            CameraPosition cameraposition = new CameraPosition.Builder().target(latlng).tilt(30).zoom(15).build();

            gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraposition));*//*


        }
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tv = ((TextView) findViewById(R.id.tvToolbarTitle));
        setTitle("");

        tv.setText("Redeem");

        final Drawable menu = getResources().getDrawable(R.drawable.back);
        menu.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.termc:
                if (termCondition.getVisibility() == View.GONE) {
                    termCondition.setVisibility(View.VISIBLE);
                } else {
                    termCondition.setVisibility(View.GONE);
                }
                break;
            case R.id.activity_reddem_checkin_ll:
                manualCheckInOut();
                break;
           *//* case R.id.ibNext:
                index += 1;
                intent = new Intent(this, Redeem.class);
                intent.putExtra(Constant.INDEX, index);
                intent.putExtra(Constant.REWARD_LIST, getIntent().getStringExtra(Constant.REWARD_LIST));
                startActivity(intent);
                this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.ibPrevious:
                index -= 1;
                intent = new Intent(this, Redeem.class);
                intent.putExtra(Constant.INDEX, index);
                intent.putExtra(Constant.REWARD_LIST, getIntent().getStringExtra(Constant.REWARD_LIST));
                startActivity(intent);
                this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;*//*
            case R.id.bRedeem:
                if (item.getRewardType().equals(Constant.RewardType.BRAND) &&
                        item.getRewardSubType().equals(new Constant().OFFER_TYPE[0])) {
                    if (support.isNetworkOnline(Redeem.this)) {
                        SharedPreference sp = new SharedPreference(Redeem.this);
                        Properties header = new Properties();
                        header.put(Constant.AUTHORIZATION, sp.getValueString(Constant.AUTHORIZATION));
                        header.put(Constant.REWARD_ID, item.getId());
                        String url = Constant.SERVER_URL + Constant.REDEEM_NOTIFICATION + item.getId();
                        JSONObject obj = new JSONObject();
                        header.put(AccessApi.ID, obj.toString());

                        new AccessApi(url, "", Redeem.this, AccessApi.POST_METHOD, header,
                                support.getHeader(Redeem.this), Redeem.this, false,
                                REQUEST_REDEEM_NOTIFICATION_POST).execute();
                    } else {
                        support.showToast(Redeem.this, Constant.MSG_CHECK_INTERNET);
                    }
                }
                else if (item.getRedeemed() > 0 && item.getRedeemed() <= points) { //&&!item.getRewardType().equals(Constant.RewardType.BRAND)
                    support.showMessageOKCancel(this, "", "Would you like to redeem?", "Proceed", "Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    redeemPoint();
                                }
                            }, null);
                }else if(item.getRedeemed() > points) {
                    support.showMessageOKCancel(this, "", "You don't have sufficient\npoints to redeem.",
                            "Okay", null, null, null);
                }
                *//*else if (!item.getRewardType().equals(Constant.RewardType.BRAND)) { //if (!item.getRewardType().equals(Constant.RewardType.BRAND)) {
                    support.showMessageOKCancel(this, "", "You don't have sufficient\npoints to redeem.",
                            "Okay", null, null, null);
                }*//* else {

                   *//* support.showMessageOKCancel(this, "", "Redeem Offer\n\nBrand will manage your redemption.",
                            "Okay", null, null, null);*//*
                }
                break;
        }
    }

    private void manualCheckInOut() {
        try {
            String url = Constant.SERVER_URL + (item.isWalkedIn() ?
                    Constant.UPDATE_MANUAL_WALKIN_OUT : Constant.UPDATE_MANUAL_WALKIN_USER) + item.getBrand().getId();
            new AccessApi(url, "", this, AccessApi.POST_METHOD, null, support.getHeader(this), this, true,
                    REQUEST_MANUAL_CHECK_IN_OUT).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redeemPoint() {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Constant.USER_ID, sp.getValueString(Constant.ID));
            obj.put(Constant.BRAND_ID, item.getBrand().getId());
            obj.put(Constant.REWARD_ID, item.getId());
            obj.put(Constant.POINTS, item.getRedeemed());
            Log.d(Constant.TAG, "Inp:" + obj.toString());

            Properties p = new Properties();
            p.put(AccessApi.ID, obj.toString());

            String url = Constant.SERVER_URL + Constant.METHOD_REDEEM_POINTS;

            new AccessApi(url, "", this, AccessApi.POST_METHOD, p, support.getHeader(this), this,
                    true, REQUEST_REDEEM_POINT).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(String result, int statusCode, int reqCode) {
        switch (reqCode) {
            case REQUEST_REDEEM_POINT:
                try {
                    if (statusCode == 200) {
                        support.showToast(this, "Congrats! You have Redeemed.");
                    } else {
                        support.showToastMessageFromJSON(this, result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    support.showToast(this, Constant.MSG_CHECK_INTERNET);
                }
                break;
            case REQUEST_MANUAL_CHECK_IN_OUT:
                if (statusCode != 200) {
                    try {
                        if (statusCode == 458){
                            support.showToast(this, "Please contact the Brand to\nCheckin to the store.");
                        }else {
                            support.showToastMessageFromJSON(this, result);
                        }
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                boolean isManualCheck;

                if (item.isWalkedIn()) {
                    isManualCheck = false;
                } else {
                    isManualCheck = true;
                }

                for (int i = 0; i < items.size(); i++) {
                    items.get(i).setWalkedIn(isManualCheck);
                }

                *//*if (item.isRedeemInStore()) {
                    item.setRedeemInStore(false);
                } else {
                    item.setRedeemInStore(true);
                }*//*

                if (item.isWalkedIn()) {
                    mTvCheckin_out.setText("Check Out");
                } else {
                    mTvCheckin_out.setText("Check In");
                }

                if (!item.getRewardType().equals(Constant.RewardType.SOCIAL) &&
                        (item.getRewardType().equals(Constant.RewardType.BRAND) ?
                                item.isWalkedIn() && item.isInStoreReward() :
                                item.isWalkedIn())) {
                    mRedeemButton.setVisibility(View.VISIBLE);
                } else {
                    mRedeemButton.setVisibility(View.GONE);
                }
                break;
            case REQUEST_REDEEM_NOTIFICATION_POST:
                if (statusCode == 200) {
                    support.showMessageOKCancel(this, "", "Redeem Offer\n\nBrand will manage your redemption.",
                            "Okay", null, null, null);
                } else {
                    try {
                        support.showToastMessageFromJSON(this, result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;

        }
    }*/
}
