package com.oyob.controller.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.oyob.controller.R;
import com.oyob.controller.fragment.FragmentContact;


public class ContactActivity extends AppCompatActivity {

    String contactBundle = null;
    ImageView btn_back;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        contactBundle = bundle.getString("client_id");
        setContentView(R.layout.contact_activity);
        btn_back = findViewById(R.id.al_iv_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (savedInstanceState == null) {
            fragment = new FragmentContact();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.ca_fl_root, fragment, null).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
