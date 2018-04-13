package com.oyob.controller.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.oyob.controller.Analytics.FirebaseAnalyticsClass;
import com.oyob.controller.R;
import com.oyob.controller.activity.ActivityDashboard;


/**
 * Created by 121 on 9/18/2017.
 */

public class FragmentContact extends Fragment implements View.OnClickListener {

    TextView textViewDial;
    TextView textViewEmail, textViewCallus, textViewEmailHint, textViewHours, textViewTime, textViewWeek;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, null, false);

        textViewDial = view.findViewById(R.id.textView_contact_no);
        textViewEmail = view.findViewById(R.id.email_address);
        textViewCallus = view.findViewById(R.id.textView_contact);
        textViewEmailHint = view.findViewById(R.id.email);
        textViewHours = view.findViewById(R.id.header);
        textViewTime = view.findViewById(R.id.time);
        textViewWeek = view.findViewById(R.id.days);
        textViewDial.setOnClickListener(this);
        textViewEmail.setOnClickListener(this);
        ActivityDashboard.setTitleToolbar(getString(R.string.contact_us));
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.textView_contact_no:
                Intent intentDial = new Intent(Intent.ACTION_DIAL);
                String number = getActivity().getString(R.string.number);
                intentDial.setData(Uri.parse("tel:" + number));
                startActivity(intentDial);
                FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle("Contact", "Click", "Dial"));
                break;
            case R.id.email_address:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("email"));
                String emailContact = getActivity().getString(R.string.mailaddress);
                String[] s = {emailContact, emailContact};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, s);
                emailIntent.setType("message/rfc822");
                Intent chooser = Intent.createChooser(emailIntent, "launch Email");
                startActivity(chooser);
                FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle("Contact", "Click", "email"));

                break;
        }
    }
}
