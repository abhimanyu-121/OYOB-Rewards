package com.oyob.controller.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.oyob.controller.R;
import com.oyob.controller.utils.SessionManager;


public class ActivityTermsandConditions extends ActivityBase {

    private AppCompatImageView aa_aciv_back;
    private WebView webView;

    private TextView textView;
    private SessionManager sessionManager;
    private String client_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsand_conditions);

        aa_aciv_back = findViewById(R.id.aa_aciv_back);
        webView = findViewById(R.id.webViewTerms);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new Callback());

        String pdfURL = "https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/Product/Terms+and+conditions.pdf";
        webView.loadUrl(
                "http://docs.google.com/gview?embedded=true&url=" + pdfURL);

        sessionManager = new SessionManager(ActivityTermsandConditions.this);
        textView = findViewById(R.id.terms_us_one);
        client_name= sessionManager.getParticularField(SessionManager.CLIENT_NAME);

        String aboutme_new = "By activating your "+client_name+ " membership you agree to the Ongoing Membership and Website Use Terms and Conditions and Fair Use Policy below."+"\n";
        Spanned sp = Html.fromHtml(aboutme_new);
        textView.setText(sp);

        SpannableString ss = new SpannableString("Read more about the Ongoing Membership and Website Use Terms and Conditions");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/Website/PGA.pdf%3E")));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 55, 75, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.txt_darker_blue)), 55, 75, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView =  findViewById(R.id.terms_us_two);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.BLUE);

        SpannableString ss1 = new SpannableString("Read more about the Fair use policy");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/Website/Pegasus%20Group%20Australia%20Pty%20Ltd%20Fair%20Use%20Policy.pdf%3E")));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss1.setSpan(clickableSpan1, 20, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.txt_darker_blue)), 20, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView1 =  findViewById(R.id.terms_us_three);
        textView1.setText(ss1);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());
        textView1.setHighlightColor(Color.BLUE);

        SpannableString ss2 = new SpannableString("Pegasus’ privacy policy explains how Pegasus handles personal information.");
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://s3-ap-southeast-2.amazonaws.com/myrewards-media/webroot/Website/Pegasus%20Group%20Australia%20Pty%20Ltd%20Privacy%20Policy_WLN.pdf%3E")));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss2.setSpan(clickableSpan2, 0, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.txt_darker_blue)), 0, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView2 =  findViewById(R.id.terms_us_six);
        textView2.setText(ss2);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        textView2.setHighlightColor(Color.BLUE);

        SpannableString ss3 = new SpannableString("We will send you information relating to "+ client_name +" by email to your nominated email address. If you wish to communicate with "+ client_name +" support, please send emails to info@work.com.au");


        ClickableSpan clickableSpan3 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","info@atwork.com.au", null));
                //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                //emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
               // intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));             }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss3.setSpan(clickableSpan3, 158, 174, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.txt_darker_blue)), 158, 174, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView3 =  findViewById(R.id.terms_us_eight);
        textView3.setText(ss3);
        textView3.setMovementMethod(LinkMovementMethod.getInstance());

        String aboutme_new1 = "\n<p>Using and sharing your information\n" +
                "Pegasus Group Australia Pty Ltd (PGA) and PGA’s service providers involved in providing your benefits program need to use and share your information for the purposes of:"+"</p>";

        Spanned sp1 = Html.fromHtml(aboutme_new1);
        TextView textVie=findViewById(R.id.terms_us_four);
        textVie.setText(sp1);

        String aboutme_new2 = "<p>1. administering the "+ client_name +" program;</p>" +
                "<p>2. providing you with "+client_name+" offers; and</p>" +
                "<p>3. providing you with products and services if you take up any "+client_name+" offers.</p>" +
                "4. By logging on to "+client_name+","+" you agree to this use of your information.";

        Spanned sp2 = Html.fromHtml(aboutme_new2);
        TextView textVie2=findViewById(R.id.terms_us_five);
        textVie2.setText(sp2);

        String aboutme_new3 = "Opting out or unsubscribing from "+ client_name +" emails\n" +
                "You can opt out or unsubscribe from getting "+ client_name +" emails in the way indicated on the relevant emails or through the "+ client_name+" website.";

        Spanned sp3 = Html.fromHtml(aboutme_new3);
        TextView textVie3=findViewById(R.id.terms_us_nine);
        textVie3.setText(sp3);


        aa_aciv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
            super.onBackPressed();
            return;
        }
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }


}
