package com.oyob.controller.activity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oyob.controller.R;
import com.oyob.controller.interfaces.TaskCompleted;
import com.oyob.controller.model.Filter;
import com.oyob.controller.model.PointsModel;
import com.oyob.controller.model.RecentRedeemModel;
import com.oyob.controller.model.RewardInfo;
import com.oyob.controller.model.RewardListGroup;
import com.oyob.controller.model.UserDetailModel;
import com.oyob.controller.model.Value;
import com.oyob.controller.networkCall.AccessApi;
import com.oyob.controller.sharePreferenceHelper.SharedPreference;
import com.oyob.controller.utils.Constant;
import com.oyob.controller.utils.Support;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



/**
 * Created by Trush on 28/02/16.
 */

/** This activity will displays all the rewards(i.e Brands,offer,Social rewards) which all are created by the
 * brand from the Brand app and rewards display grouped by the brand store*/

public class Rewards extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener,
        TaskCompleted {

    private static final int REQUEST_GET_POINTS = 1;
    private static final int REQUEST_REWARD_LIST = 2;
    private static final int REQUEST_GET_BRAND = 3;
    private static final String TAG = "Rewards";
    private DrawerLayout dLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    Support support = new Support();
    private SharedPreference sp;
    String authToken;
    private PointsModel points;
    TextView tvPointsBalance, tvPointsBalanceLabel; //tvRewardRedeeemHistory,
    private List<RewardInfo> rewardList;
    LinearLayout llBrands, llPoint;
    private TextView tvName;
    private String name;
    private List<RewardListGroup> rewardListGroupList;
    private boolean mNameVisible = true, mDescVisble = true;
    private View mRedimLayout, mYouHaveTv, mNameTv; //mDescTv;
    private int tempX, x, tempY, y;
    ProgressBar mProgressBar;
    NestedScrollView nestedScrollView;
    private int mSelPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        initToolbar();
        findViews();
        sp = new SharedPreference(this);
        authToken = sp.getValueString(Constant.AUTHORIZATION);
        String d = sp.getValueString(Constant.USER_DATA);
        UserDetailModel um = new Gson().fromJson(d, UserDetailModel.class);
        if (um != null) {
            name = um.getName().getFirstName();
        }else {
            this.finish();
            return;
        }

        mProgressBar = (ProgressBar) findViewById(R.id.brands_progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        getDataFromServer();
        findViewById(R.id.redim_layout).setTag(getResources().getDimensionPixelSize(R.dimen.dp_195));
        ((AppBarLayout) findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(this);
        mNameTv = findViewById(R.id.name_layout);
        //  mDescTv = findViewById(R.id.tvRewardRedeeemHistory);
        mNameTv.setTag(1);
        //final int size = getResources().getDimensionPixelSize(R.dimen.dp_170);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (87 - scrollY > 70) {
                    tvPointsBalance.setTextSize(57);
                    tvName.setAlpha((float) 0.9);
                } else if (87 - scrollY > 65) {
                    tvPointsBalance.setTextSize(55);
                    tvName.setAlpha((float) 0.9);
                } else if (87 - scrollY > 60) {
                    tvPointsBalance.setTextSize(53);
                    tvName.setAlpha((float) 0.9);
                } else if (87 - scrollY > 55) {
                    tvPointsBalance.setTextSize(51);
                    tvName.setAlpha((float) 0.9);
                } else if (87 - scrollY > 50) {
                    tvPointsBalance.setTextSize(49);
                    tvName.setAlpha((float) 0.9);
                } else if (87 - scrollY > 45) {
                    tvPointsBalance.setTextSize(47);
                    tvName.setAlpha((float) 0.7);
                } else if (87 - scrollY > 40) {
                    tvPointsBalance.setTextSize(45);
                    tvName.setAlpha((float) 0.5);
                } else if (87 - scrollY > 35) {
                    tvPointsBalance.setTextSize(43);
                    tvName.setAlpha((float) 0.5);
                } else if (87 - scrollY > 30) {
                    tvPointsBalance.setTextSize(40);
                    tvName.setAlpha((float) 0.5);
                } else if (87 - scrollY > 25) {
                    tvPointsBalance.setTextSize(38);
                    tvName.setAlpha((float) 0.5);
                } else if (87 - scrollY > 20) {
                    tvPointsBalance.setTextSize(36);
                    tvName.setAlpha((float) 0.4);
                } else if (87 - scrollY > 10) {
                    tvPointsBalance.setTextSize(36);
                    tvName.setAlpha((float) 0);
                } else if (scrollY > 82) {
                    tvName.setAlpha((float) 0);
                    tvPointsBalance.setTextSize(36);
                }
                if (scrollY >= 140) {
                    findViewById(R.id.redim_layout).setVisibility(View.INVISIBLE);
                    findViewById(R.id.point_layout2).setVisibility(View.VISIBLE);

                } else {
                    findViewById(R.id.redim_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.point_layout2).setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }

    private void handleAlphaOnName(float percentage, View view) {
        if (percentage >= 1) {
            if (mNameVisible) {
                startAlphaAnimation(view, 200, View.INVISIBLE);
                mNameVisible = false;
            }

        } else {

            if (!mNameVisible) {
                startAlphaAnimation(view, 200, View.VISIBLE);
                mNameVisible = true;
            }
        }
    }

    private void handleAlphaOnDesc(float percentage, View view) {
        if (percentage >= 1) {
            if (mDescVisble) {
                startAlphaAnimation(view, 200, View.INVISIBLE);
                mDescVisble = false;
            }

        } else {

            if (!mDescVisble) {
                startAlphaAnimation(view, 200, View.VISIBLE);
                mDescVisble = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private void getBrandFromServer() {
        String url = Constant.SERVER_URL + Constant.METHOD_GET_REWARD_LIST_GROUP;
        new AccessApi(url, "", this, AccessApi.POST_METHOD, null, support.getHeader(this), this, false, REQUEST_GET_BRAND).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void findViews() {
        //tvRewardRedeeemHistory = (TextView) findViewById(R.id.tvRewardRedeeemHistory);
        tvPointsBalance = (TextView) findViewById(R.id.tvPointsBalance);
        tvPointsBalanceLabel = (TextView) findViewById(R.id.tvPointsBalanceLabel);
        llBrands = (LinearLayout) findViewById(R.id.llBrands);
        llPoint = (LinearLayout) findViewById(R.id.point_layout);
        tvName = (TextView) findViewById(R.id.tvName);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedscroll_view);


        findViewById(R.id.bRedeem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokeRedeem(view);
            }
        });

        /*Animation translatebu= AnimationUtils.loadAnimation(this, R.anim.textscroll);

        tvPointsBalance.startAnimation(translatebu);*/

        // animateLayoutChanges((LinearLayout)findViewById(R.id.points_expanded_view));

    }

    public static void animateLayoutChanges(ViewGroup container) {
        final LayoutTransition transition = new LayoutTransition();
        transition.setDuration(300);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            transition.enableTransitionType(LayoutTransition.CHANGING);
            transition.enableTransitionType(LayoutTransition.APPEARING);
            transition.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
            transition.enableTransitionType(LayoutTransition.DISAPPEARING);
            transition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
        }

        container.setLayoutTransition(transition);
    }

    private void getDataFromServer() {
        if (support.isNetworkOnline(this)) {
            Properties header = new Properties();
            header.put(Constant.AUTHORIZATION, authToken);
            String url = Constant.SERVER_URL + Constant.METHOD_GET_POINTS;
            new AccessApi(url, "", this, AccessApi.GET_METHOD, null, header, this, true, REQUEST_GET_POINTS).execute();
        } else {
            support.showToast(this, Constant.MSG_CHECK_INTERNET);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
       /* dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, dLayout, R.string.app_name, R.string.app_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tv = ((TextView) findViewById(R.id.tvToolbarTitle));

        setTitle("");
        tv.setText("Rewards");
        dLayout.setDrawerListener(mDrawerToggle);
        new SetupDrawer(this, R.id.llRewards);
        final Drawable menu = getResources().getDrawable(R.drawable.menu);
        menu.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(menu);*/
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    public void invokeRedeem(View v) {
        //getRewardListFromServer();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResult(String result, int statusCode, int reqCode) {
        switch (reqCode) {
            case REQUEST_GET_POINTS:
                try {
                    getBrandFromServer();
                    if (statusCode == 200) {

                        JSONObject obj = new JSONObject(result);
                        String data = obj.getString(Constant.POINTS);
                        points = new Gson().fromJson(data, PointsModel.class);
                        if (points != null) {
                            if (points.getRecentRedeem() != null) {
                                RecentRedeemModel rm = points.getRecentRedeem();
                                String msg = rm.getPoints() + " points last redeemed at " +
                                        rm.getBrandName() + "on " + support.getDate(rm.getRedeemedOn(), Constant.SERVER_DATE_FORMAT, Constant.REWARD_DATE_FORMAT);
                                //tvRewardRedeeemHistory.setText(msg);
                            } else {
                                //tvRewardRedeeemHistory.setText("No Redeem History");
                            }

                            if (points.getPointsBalance() == 0) {
                                tvPointsBalanceLabel.setVisibility(View.VISIBLE);
                                tvName.setText("Hi " + name + "");
                                tvName.setVisibility(View.VISIBLE);
                                tvPointsBalanceLabel.setText("Points");

                            } else {
                                tvName.setVisibility(View.VISIBLE);
                                tvName.setText("Hi " + name);
                                tvPointsBalance.setText(points.getPointsBalance() + "");
                                tvPointsBalance.setVisibility(View.VISIBLE);
                                tvPointsBalanceLabel.setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.tvPointsBalance_small)).setText(points.getPointsBalance() + "");

                            }

                        }

                    } else {
                        support.showToastMessageFromJSON(this, result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    support.showToast(this, Constant.MSG_CHECK_INTERNET);
                    mProgressBar.setVisibility(View.GONE);
                }
                break;
            case REQUEST_GET_BRAND:
                try {
                    JSONObject obj = new JSONObject(result);
                    String d = obj.getString(Constant.REWARD_LIST);
                    rewardListGroupList = new Gson().fromJson(d, new TypeToken<List<RewardListGroup>>() {
                    }.getType());
                    if (rewardListGroupList.size() > 0) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        Rewards.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bindBrands();
                            }
                        });

                    }else {
                        mProgressBar.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_REWARD_LIST:
                Log.d(Constant.TAG, "Res:" + result);
                try {
                    if (statusCode == 200) {
                        JSONObject obj = new JSONObject(result);
                        String d = obj.getString(Constant.REWARD_LIST);
                        rewardList = new Gson().fromJson(d, new TypeToken<List<RewardInfo>>() {
                        }.getType());
                        if (rewardList.size() > 0) {
                            Intent i = new Intent(this, Redeem.class);
                            i.putExtra(Constant.REWARD_LIST, d);
                            i.putExtra(Constant.INDEX, 0);
                            i.putExtra(Constant.REWARD_INDEX, mSelPosition);
                            if (points != null)
                                i.putExtra(Constant.POINTS, points.getPointsBalance());
                            startActivity(i);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void bindBrands() {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < rewardListGroupList.size(); i++) {
            View v = inflater.inflate(R.layout.inflater_reward_list, null);

            llBrands.addView(v);
            View view = new View(this);
            Resources resources = getResources();
            view.setBackgroundColor(resources.getColor(R.color.white));
            llBrands.addView(view, new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.dp_10)));
            mProgressBar.setVisibility(View.GONE);
        }
        mProgressBar.setVisibility(View.GONE);
    }

    public void getRewardListFromServer(String brand_id) {
        try {
            String url = Constant.SERVER_URL + Constant.METHOD_GET_REWARD_LIST;
            List<Filter> filterList = new ArrayList<>();
            Filter filter = new Filter();
            filter.setType("FIXED");
            filter.setKey("brandId");
            List<Value> values = filter.getValue();
            Value value = new Value();
            value.setType("IN");
            value.setValue(brand_id);
            values.add(value);
            filterList.add(filter);
            JSONObject obj = new JSONObject();
            obj.put(Constant.FILTERS, new Gson().toJson(filterList));
            obj.put("index", 0);
            obj.put("limit", 200);
            Properties p = new Properties();
            p.put(AccessApi.ID, obj.toString());
            new AccessApi(url, "", this, AccessApi.POST_METHOD, p, support.getHeader(this), this, true,
                    REQUEST_REWARD_LIST).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}