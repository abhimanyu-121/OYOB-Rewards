package com.oyob.controller.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;

import com.oyob.controller.R;
import com.oyob.controller.fragment.ConnectFragment;


/**
 * Created by Ramasamy on 9/2/2017.
 */
public class ConnectActivity extends AppCompatActivity {

    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    String bundleConnect =null;
    static  final String TAG=ConnectActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Bundle bundle= getIntent().getExtras();
        bundleConnect=bundle.getString("client_id");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        backArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.connect_toolbar);

        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;

        if (savedInstanceState == null) {
            fragment = new ConnectFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, fragment, null).commit();
        }
    }
    @Override
    public void onBackPressed() {

        FragmentManager mgr = getSupportFragmentManager();
        if (mgr.getBackStackEntryCount() == 1) {

        } else {
            Intent intent = new Intent(getApplicationContext(), ActivityDashboard.class);
            intent.putExtra("client_id", bundleConnect);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}
