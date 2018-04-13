package com.oyob.controller.stripe.service;

import android.support.annotation.NonNull;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Piyush on 2/18/2018.
 */

public class CustomerIdProvider {

    private @NonNull
    CompositeSubscription mCompositeSubscription;
    private @NonNull StripeService mStripeService;
    private @NonNull CustomerIdListener mProgressListener;




    public interface CustomerIdListener {
        void onStringResponse(String string);
    }
}
