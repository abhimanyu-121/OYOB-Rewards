package com.oyob.controller.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.oyob.controller.R;
import com.oyob.controller.adapter.ProductAdapter;
import com.oyob.controller.fragment.FragmentBase;
import com.oyob.controller.fragment.MycartDeliveryFragment;
import com.oyob.controller.fragment.MycartFragment;
import com.oyob.controller.fragment.MycartPaypalFragment;
import com.oyob.controller.networkCall.ProcessAsynctack;
import com.oyob.controller.utils.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyShopActivity extends FragmentBase {


    private RadioGroup rg;
    static TextView mDotsText[];
    private int mDotCount;
    private LinearLayout mDotsLayout;
    Button cardButton1;
    Button cardButton2;
    ListView listView;
    private boolean isSuccessApiCall = false;
    ProcessAsynctack process;
    String stringResponse;
    String URL = null;
    static final String TAG = ActivityDashboard.class.getSimpleName();
    String clientId = null;
    String country = null;
    String uname = null;
    ProductAdapter customAdapter1;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    protected DrawerLayout drawer;
    TextView mTitle;
    public static LinearLayout linearLayout;
    String androidId;
    Date currentTime;
    String userId = "";
    String errorcode = "";
    DateFormat df;
    String reportDate;
    Toolbar toolbar;
    static TextView txt_toolbar_title;
    View rootView;
    static RadioButton rb_payment;
    static RadioButton rb_ex;

    public static double totalMainCost = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(_context).inflate(R.layout.activity_my_shop, container, false);

        ActivityDashboard.img_client_logo.setVisibility(View.GONE);
        
        androidId = Settings.Secure.getString(_context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currentTime = Calendar.getInstance().getTime();
        reportDate = df.format(currentTime);
        ActivityDashboard.setTitleToolbar("My Cart");
        linearLayout = rootView.findViewById(R.id.container);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            clientId = bundle.getString("client_id");
            country = bundle.getString("country");
            uname = bundle.getString("uname");
        } else {
            clientId = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        }
        userId = sessionManager.getParticularField(SessionManager.USER_ID);
        linearLayout = rootView.findViewById(R.id.container);
        drawer = rootView.findViewById(R.id.drawer_layout);
        rb_payment = rootView.findViewById(R.id.rb_payment);
        rb_ex = rootView.findViewById(R.id.rb_ex);

        if (savedInstanceState == null) {
            fragment = new MycartFragment();
            addFragmentINQueue(fragment);
        }
        setHasOptionsMenu(false);
        rg = rootView.findViewById(R.id.activity_course_rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getId() == R.id.rb_in) {
                    fragment = new MycartFragment();
                    addFragmentINQueue(fragment);
                } else if (rb.getId() == R.id.rb_ex) {
                    if (MycartFragment.shopModels.size() > 0) {
                        fragment = new MycartDeliveryFragment();
                        addFragmentINQueue(fragment);
                    } else {
                        Toast.makeText(_context, "Cart is empty", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (MycartFragment.shopModels.size() > 0) {
                        fragment = new MycartPaypalFragment();
                        addFragmentINQueue(fragment);
                    } else {
                        Toast.makeText(_context, "Cart is empty", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return rootView;
    }

    public static void ClickPayment() {
        rb_payment.performClick();
    }

    public static void ClickDelivery() {
        rb_ex.performClick();
    }


    private void addFragmentINQueue(Fragment fragment1) {
        fragmentTransaction = getChildFragmentManager().beginTransaction().addToBackStack("");
        fragmentTransaction.replace(R.id.frame, fragment1, null).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ActivityDashboard.img_client_logo.setVisibility(View.VISIBLE);
    }
}