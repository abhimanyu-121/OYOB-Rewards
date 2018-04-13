package com.oyob.controller.fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oyob.controller.R;
import com.oyob.controller.activity.ActivityDashboard;
import com.oyob.controller.utils.SessionManager;


/**
 * Created by Ramasamy on 9/2/2017.
 */
public class FragmentAbout extends FragmentBase {
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    private TextView textView, terms;
    private SessionManager sessionManager;
    private String client_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(getContext());
        textView = view.findViewById(R.id.about_us);
        terms = view.findViewById(R.id.myshop_terms_conditions);
        client_name = sessionManager.getParticularField(SessionManager.CLIENT_NAME);

        String aboutme_new = "<p><b><font size=\"26\" color=\"black\">Welcome to the </font></b>" + client_name + "</p>" + "\n" +
                "\n" +
                "<p><strong><font style = \"bold\" size=\"6\" color=\"black\">how it works...</font>\n</strong></p>\n" +
                "\n" +
                client_name + " has 1000's of offers from all around Australia and even more overseas. Because there are so many suppliers there are different ways to access / redeem each benefit. It's easy to follow by reading the notes that are included with each offer, but if you get stuck here are a few pointers to assist you.\n" +


                "\n\n" +
                "<p><b><font size=\"6\" color=\"black\">tips on searching...</font></b></p>\n" +

                "<p>searching is easy... use the key words of what you are searching for to get rolling. If unsuccessful on that term, try some variations on the item you are seeking. You will then be able to refine your search by category, country, state and suburb. Look for the filters on the left of the page. If you still cannot find what you’re looking for pop us an email or give us a call on: 1300 368 846.</p>\n" +
                "\n\n";

        Spanned sp = Html.fromHtml(aboutme_new);
        textView.setText(sp);
        ActivityDashboard.setTitleToolbar(getString(R.string.about));

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/Product/Terms+and+conditions.pdf"));
                startActivity(browserIntent);
            }
        });
    }
}
