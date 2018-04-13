package com.oyob.controller.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oyob.controller.R;
import com.oyob.controller.activity.ActivityDashboard;
import com.oyob.controller.sharePreferenceHelper.SharedPreference;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.SessionManager;


/**
 * Created by Narender Kumar on 2/23/2018.
 * Prominent Developer
 * narender.kumar.nishad@gmail.com
 */

public class CardFragment extends FragmentBase {

    private String clientId = "", uname = "";
    private ImageView card_iv_image;
    private TextView card_tv_member_name, card_member_ship_number, card_tv_membership_id;
    private SharedPreference preference;
    private String card_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clientId = SharedPreferenceHelper.getPref("client_id", _context);
        uname = SharedPreferenceHelper.getPref("userId", _context);
        preference = new SharedPreference(_context);
        card_iv_image = view.findViewById(R.id.card_iv_image);
        card_tv_member_name = view.findViewById(R.id.card_tv_member_name);
        card_member_ship_number = view.findViewById(R.id.member_ship_number);
        card_tv_membership_id = view.findViewById(R.id.card_tv_membership_id);

        ActivityDashboard.setTitleToolbar(getString(R.string.my_card));

        String full_name = sessionManager.getParticularField(SessionManager.FIRST_NAME)
                + " " + sessionManager.getParticularField(SessionManager.LAST_NAME);

        card_image = sessionManager.getParticularField(SessionManager.CLIENT_ID)+"."+sessionManager.getParticularField(SessionManager.CARD_EXT);

        getDetails(full_name);
    }

    private void getDetails(String full_name) {
        Glide.with(_context)
                .load("https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/files/mobile_membership_card/"+card_image)
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .into(card_iv_image);

        card_tv_membership_id.setText(sessionManager.getParticularField(SessionManager.USER_NAME));
        //card_member_ship_number.setText(sessionManager.getParticularField(SessionManager.M));
        card_tv_member_name.setText(full_name);
    }
}