package com.oyob.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.oyob.controller.R;


/**
 * Created by Ramasamy on 9/6/2017.
 */

public class GiftActivity extends AppCompatActivity {

    String giftBundle = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_activity);
        Bundle bundle=getIntent().getExtras();
        giftBundle= bundle.getString("client_id");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ActivityDashboard.class);
        intent.putExtra("client_id",giftBundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
