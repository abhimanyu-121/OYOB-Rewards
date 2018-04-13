package com.oyob.controller.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.oyob.controller.R;

import com.oyob.controller.activity.ActivityDashboard;
import com.oyob.controller.adapter.MyShopeAdapter;
import com.oyob.controller.stripe.RetrofitFactory;
import com.oyob.controller.stripe.service.SampleStoreEphemeralKeyProvider;
import com.oyob.controller.stripe.service.StripeService;
import com.oyob.controller.utils.Mylogger;
import com.oyob.controller.utils.SessionManager;
import com.oyob.controller.utils.Support;
import com.oyob.controller.utils.Utility;
import com.stripe.android.CustomerSession;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentSession;
import com.stripe.android.PaymentSessionConfig;
import com.stripe.android.PaymentSessionData;
import com.stripe.android.model.Customer;
import com.stripe.android.model.CustomerSource;
import com.stripe.android.model.ShippingInformation;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceCardData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.oyob.controller.activity.ActivityDashboard.headerUserName;

/**
 * Created by 121 on 9/18/2017.
 */

public class MycartPaypalFragment extends FragmentBase {

    private static final String PUBLISHABLE_KEY = "pk_live_1Sx0DwDFuLMYO5Gc8NusmU2p";
    //private static final String PUBLISHABLE_KEY = "pk_test_O25zyXXj74gl3XRbVoXjaHaE";
    private static String cus_id, balance_transaction;
    private Button btn_place_order;
    private View rootView;
    private EditText edt_email;
    private Button btn_select_payment;
    private LinearLayout linear_paymentinfo;
    private TextView payment_source;
    private @NonNull
    CompositeSubscription mCompositeSubscription;
    private @NonNull
    StripeService mStripeService;
    private PaymentSession mPaymentSession;
    private SessionManager sessionManager;
    private double price;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_cart__paypal_fragment, container, false);

        PaymentConfiguration.init(PUBLISHABLE_KEY);
        sessionManager=new SessionManager(getActivity());

        Retrofit retrofit = RetrofitFactory.getInstance(RetrofitFactory.BASE_URL);
        mStripeService = retrofit.create(StripeService.class);
        mCompositeSubscription = new CompositeSubscription();

        edt_email = rootView.findViewById(R.id.edt_email);
        btn_select_payment = rootView.findViewById(R.id.btn_select_payment);
        linear_paymentinfo = rootView.findViewById(R.id.linear_paymentinfo);
        payment_source = rootView.findViewById(R.id.payment_source);

        btn_select_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();
                if (TextUtils.isEmpty(email) || Utility.isEmailValid(email) == false) {
                    showAlert("Invalid email");
                    return;
                } else {
                    Support.hideKeyboard(getActivity(), edt_email);
                    CreateStripeCustomer(email);
                   /* if(((sessionManager.getParticularField(SessionManager.CUSTOMER_ID))!=null ))
                    {
                        cus_id=sessionManager.getParticularField(SessionManager.CUSTOMER_ID);
                        Log.i("TEST"," seess id "+cus_id);
                        setupCustomerSession(cus_id);
                    }
                    else
                    {
                        Support.hideKeyboard(getActivity(), edt_email);
                        CreateStripeCustomer(email);
                    }*/

                }
            }
        });

        //completePurchase();


        btn_place_order = rootView.findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptPurchase();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        setupPaymentSession();
    }

    private void CreateStripeCustomer(String email) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Map<String, String> apiParamMap = new HashMap<>();
        apiParamMap.put("email", email);

        mCompositeSubscription.add(
                mStripeService.CreateStripeCustomer(apiParamMap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ResponseBody>() {
                            @Override
                            public void call(ResponseBody response) {
                                pDialog.dismiss();
                                try {
                                    String rawKey = response.string();
                                    Mylogger.getInstance().Logit("rawKey", "rawKey: " + rawKey);
                                    if (!Utility.isEmptyOrNot(rawKey)) {
                                        JSONObject jsonObject = new JSONObject(rawKey);
                                        cus_id = jsonObject.getString("id");
                                        Log.i("TEST","customer id "+cus_id);
                                        sessionManager.updateCustomerId(cus_id);
                                        if (!Utility.isEmptyOrNot(cus_id)) {
                                            setupCustomerSession(cus_id);
                                        }
                                    }
                                } catch (Exception iox) {
                                    System.out.println("iox: " + iox);
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                pDialog.dismiss();
                            }
                        }));
    }

    private void setupCustomerSession(String cus_id) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        // CustomerSession only needs to be initialized once per app.
        CustomerSession.initCustomerSession(
                new SampleStoreEphemeralKeyProvider(
                        new SampleStoreEphemeralKeyProvider.ProgressListener() {
                            @Override
                            public void onStringResponse(String string) {
                                pDialog.dismiss();
                                if (string.startsWith("Error: ")) {
                                    new AlertDialog.Builder(getContext()).setMessage(string).show();
                                } else {
                                    setupPaymentSession();

                                    if (!mPaymentSession.getPaymentSessionData().isPaymentReadyToCharge()) {
                                        btn_place_order.setEnabled(false);
                                    }
                                }
                            }
                        }, cus_id));
    }

    private void showAlert(String msg) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                getContext()).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    private void attemptPurchase() {
        CustomerSession.getInstance().retrieveCurrentCustomer(new CustomerSession.CustomerRetrievalListener() {
            @Override
            public void onCustomerRetrieved(@NonNull Customer customer) {
                String sourceId = customer.getDefaultSource();
                if (sourceId == null) {
                    displayError("No payment method selected");
                    return;
                }
                CustomerSource source = customer.getSourceById(sourceId);
                proceedWithPurchaseIf3DSCheckIsNotNecessary(source.asSource(), customer.getId());
            }

            @Override
            public void onError(int errorCode, @Nullable String errorMessage) {
                displayError("Error getting payment method");
            }
        });

    }

    private void proceedWithPurchaseIf3DSCheckIsNotNecessary(Source source, String customerId) {
        if (source == null || !Source.CARD.equals(source.getType())) {
            displayError("Something went wrong - this should be rare");
            return;
        }

        SourceCardData cardData = (SourceCardData) source.getSourceTypeModel();
        if (SourceCardData.REQUIRED.equals(cardData.getThreeDSecureStatus())) {
            // In this case, you would need to ask the user to verify the purchase.
            // You can see an example of how to do this in the 3DS example application.
            // In stripe-android/example.
        } else {
            // If 3DS is not required, you can charge the source.
            //completePurchase(source.getId(), customerId);
            completePurchase();
        }
    }



    private Map<String, Object> createParams(double price, String sourceId, String customerId, ShippingInformation shippingInformation) {
        Map<String, Object> params = new HashMap<>();

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("CartId",sessionManager.getParticularField(SessionManager.CART_ID));
            jsonObject.put("UserId",sessionManager.getParticularField(SessionManager.USER_ID));
            jsonObject.put("ClientId",sessionManager.getParticularField(SessionManager.CLIENT_ID));
           // jsonObject.put("AddressId",sessionManager.getParticularField(SessionManager.ADDRESS_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        float amount = Float.valueOf(MycartFragment.tt);
        float amount1=amount*100;
        String aa = String.valueOf(amount1);
        int pp= aa.indexOf('.');
        String uu =aa.substring(0,pp);
        params.put("amount", uu);
        //params.put("amount", "100");
        //   params.put("source", sourceId);
        params.put("customer", customerId);
        params.put("currency", "aud");
        params.put("description",jsonObject.toString());
        // params.put("shipping", shippingInformation.toMap());
        Mylogger.getInstance().Logit("Params", "Params: " + params.toString());
        Log.i("TEST",params.toString());
        return params;
    }

   /* public void ShowDialog() {
        if (mProgressDialogFragment != null &&
                !mProgressDialogFragment.isAdded())
            mProgressDialogFragment.show(getActivity().getSupportFragmentManager(), "progress");
    }

    public void DismissDialog() {
        if (mProgressDialogFragment != null
                && mProgressDialogFragment.isVisible()) {
            mProgressDialogFragment.dismiss();
        }
    }*/


    private void completePurchase() {

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("CartId",sessionManager.getParticularField(SessionManager.CART_ID));
            jsonObject.put("UserId",sessionManager.getParticularField(SessionManager.USER_NAME));
            jsonObject.put("ClientId",sessionManager.getParticularField(SessionManager.CLIENT_NAME)+"\n"+ MyShopeAdapter.extra);
            // jsonObject.put("AddressId",sessionManager.getParticularField(SessionManager.ADDRESS_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String ee = "CartId:"+sessionManager.getParticularField(SessionManager.CART_ID)+"\nUserId:"+sessionManager.getParticularField(SessionManager.USER_ID)+"\nClientId:"+sessionManager.getParticularField(SessionManager.CLIENT_ID)+"\n"+ MyShopeAdapter.extra+"\n Shipping Cost:"+MycartFragment.pp;

        float amount = Float.valueOf(MycartFragment.tt);
        float amount1=amount*100;
        String aa = String.valueOf(amount1);
        int pp= aa.indexOf('.');
        String uu =aa.substring(0,pp);



        String url = "https://api.stripe.com/v1/charges?amount=" + uu + "&currency=" + "aud" + "&customer=" + sessionManager.getParticularField(SessionManager.CUSTOMER_ID) + "&description=" + jsonObject.toString()+ "&receipt_email="+MycartDeliveryFragment.a_email+    "&shipping[name]="+sessionManager.getParticularField(SessionManager.FIRST_NAME)+"&shipping[phone]="+MycartDeliveryFragment.a_mobile+"&shipping[address][country]="+MycartDeliveryFragment.a_country+"&shipping[address][state]="+MycartDeliveryFragment.a_state+"&shipping[address][city]="+MycartDeliveryFragment.a1+"&shipping[address][postal_code]="+MycartDeliveryFragment.a2+"&shipping[address][line1]="+MycartDeliveryFragment.a3+"&shipping[address][line2]="+MycartDeliveryFragment.a4;
        Log.i("TEST","new complete purchase "+url);

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Retrofit retrofit = RetrofitFactory.getInstance(RetrofitFactory.BASE_URL_STRIPE);
        StripeService stripeService = retrofit.create(StripeService.class);

        Map<String, String> apiParamMap = new HashMap<>();
        apiParamMap.put("amount", uu);
        apiParamMap.put("currency", "aud");
        apiParamMap.put("customer", sessionManager.getParticularField(SessionManager.CUSTOMER_ID));
        apiParamMap.put("description", ee);
        apiParamMap.put("receipt_email", MycartDeliveryFragment.a_email);
        apiParamMap.put("shipping[name]", sessionManager.getParticularField(SessionManager.FIRST_NAME));
        apiParamMap.put("shipping[phone]", MycartDeliveryFragment.a_mobile);
        apiParamMap.put("shipping[address][country]", MycartDeliveryFragment.a_country);
        apiParamMap.put("shipping[address][state]", MycartDeliveryFragment.a_state);
        apiParamMap.put("shipping[address][city]", MycartDeliveryFragment.a1);
        apiParamMap.put("shipping[address][postal_code]", MycartDeliveryFragment.a2);
        apiParamMap.put("shipping[address][line1]", MycartDeliveryFragment.a3);
        apiParamMap.put("shipping[address][line2]", MycartDeliveryFragment.a4);

        ShippingInformation shippingInformation = mPaymentSession.getPaymentSessionData().getShippingInformation();

       // Observable<ResponseBody> stripeResponse = stripeService.createQueryCharge(uu,"aud",sessionManager.getParticularField(SessionManager.CUSTOMER_ID),jsonObject.toString(),MycartDeliveryFragment.a_email,sessionManager.getParticularField(SessionManager.FIRST_NAME),MycartDeliveryFragment.a_mobile,MycartDeliveryFragment.a_country,MycartDeliveryFragment.a_state,MycartDeliveryFragment.a1,MycartDeliveryFragment.a2,MycartDeliveryFragment.a3,MycartDeliveryFragment.a4);

        Observable<ResponseBody> stripeResponse = stripeService.createQueryCharge(apiParamMap);

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        mCompositeSubscription.add(stripeResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                                if (!pDialog.isShowing()) {
                                    pDialog.show();
                                }
                            }
                        })
                .doOnUnsubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                                pDialog.dismiss();
                            }
                        })
                .subscribe(
                        new Action1<ResponseBody>() {
                            @Override
                            public void call(ResponseBody response) {
                                pDialog.dismiss();
                                try {
                                    String rawKey = response.string();

                                    Mylogger.getInstance().Logit("Stripe Final", "Stripe: " + rawKey);
                                    if (!Utility.isEmptyOrNot(rawKey)) {
                                        JSONObject jsonObject = new JSONObject(rawKey);
                                        balance_transaction = jsonObject.getString("balance_transaction");
                                        String amount = jsonObject.getString("amount");
                                        String currency = jsonObject.getString("currency");
                                        addInvoice(amount, balance_transaction, "aud");
                                    }
                                } catch (Exception iox) {
                                    System.out.println("iox: " + iox);
                                }


                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                displayError(throwable.getLocalizedMessage());
                            }
                        }));
    }


    private void displayError(String errorMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void finishCharge() {

    }

    private void setupPaymentSession() {
        mPaymentSession = new PaymentSession(getActivity());
        mPaymentSession.init(new PaymentSession.PaymentSessionListener() {
            @Override
            public void onCommunicatingStateChanged(boolean isCommunicating) {

            }

            @Override
            public void onError(int errorCode, @Nullable String errorMessage) {
                displayError(errorMessage);
            }

            @Override
            public void onPaymentSessionDataChanged(@NonNull PaymentSessionData data) {
                if (data.getShippingMethod() != null) {
                   /* mEnterShippingInfo.setText(data.getShippingMethod().getLabel());
                    mShippingCosts = data.getShippingMethod().getAmount();
                    addCartItems();
                    updateConfirmPaymentButton();*/
                }

                if (data.getSelectedPaymentMethodId() != null) {
                    CustomerSession.getInstance().retrieveCurrentCustomer(new CustomerSession.CustomerRetrievalListener() {
                        @Override
                        public void onCustomerRetrieved(@NonNull Customer customer) {
                            String sourceId = customer.getDefaultSource();
                            if (sourceId == null) {
                                //displayError("No payment method selected");
                                mPaymentSession.presentPaymentMethodSelection();
                                return;
                            }
                            btn_place_order.setEnabled(true);
                            linear_paymentinfo.setVisibility(View.VISIBLE);
                            CustomerSource source = customer.getSourceById(sourceId);
                            payment_source.setText(formatSourceDescription(source.asSource()));
                        }

                        @Override
                        public void onError(int errorCode, @Nullable String errorMessage) {
                            displayError(errorMessage);
                        }
                    });
                } else {
                    mPaymentSession.presentPaymentMethodSelection();
                }

                if (data.isPaymentReadyToCharge()) {
                    btn_place_order.setEnabled(true);
                }

            }
        }, new PaymentSessionConfig.Builder().build());
    }

    private String formatSourceDescription(Source source) {
        if (Source.CARD.equals(source.getType())) {
            SourceCardData sourceCardData = (SourceCardData) source.getSourceTypeModel();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sourceCardData.getBrand()).append(getString(R.string.ending_in)).append(sourceCardData.getLast4());
            return stringBuilder.toString();
        }
        return source.getType();
    }

    /*
     * Cleaning up all Rx subscriptions in onDestroy.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }

        mPaymentSession.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPaymentSession.handlePaymentData(requestCode, resultCode, data);
    }


    public void addInvoice(String amount, String txnId, String currency) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Retrofit retrofit = RetrofitFactory.getInstance(RetrofitFactory.BASE_URL);
        mStripeService = retrofit.create(StripeService.class);

        String cart_id = sessionManager.getParticularField(SessionManager.CART_ID);
        String client_id = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        String uid = sessionManager.getParticularField(SessionManager.USER_ID);

        int pp= MycartFragment.tt.indexOf('.');
        String uu =MycartFragment.tt.substring(0,pp);

        Map<String, String> apiParamMap = new HashMap<>();
        //apiParamMap.put("amount", String.valueOf(price));
        apiParamMap.put("amount",uu);
        apiParamMap.put("cart_id", cart_id);
        apiParamMap.put("client_id", client_id);
        apiParamMap.put("currency", "AUD");
        apiParamMap.put("payment_status", "Completed");
        apiParamMap.put("txn_id", txnId);
        apiParamMap.put("address_id",sessionManager.getParticularField(SessionManager.ADDRESS_ID));
        apiParamMap.put("uid", uid);

        String jsonReq = RetrofitFactory.gson.toJson(apiParamMap);

        System.out.println("jsonReq: " + jsonReq);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonReq);

        mCompositeSubscription.add(
                mStripeService.addInvoice(body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ResponseBody>() {
                            @Override
                            public void call(ResponseBody response) {
                                pDialog.dismiss();
                                try {
                                    String rawKey = response.string();

                                    Mylogger.getInstance().Logit("rawKey", "rawKey: " + rawKey);
                                    if (!Utility.isEmptyOrNot(rawKey)) {
                                        JSONObject jsonObject = new JSONObject(rawKey);

                                        JSONObject jsonObject1 = jsonObject.getJSONObject("status");
                                        JSONObject obj4 =null;
                                        Iterator<String> keys = jsonObject.keys();
                                        while (keys.hasNext()) {
                                            String keyValue = (String) keys.next();
                                            obj4 = jsonObject.getJSONObject(keyValue);

                                            //getting string values with keys- pageid and title
                                            String status = jsonObject1.getString("200");

                                            if (status.equals("success")) {
                                                UpdateCartStatus();
                                            }
                                        }
                                    }
                                } catch (Exception iox) {
                                    System.out.println("iox: " + iox);
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                pDialog.dismiss();
                            }
                        }));
    }

    public void UpdateCartStatus() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Retrofit retrofit = RetrofitFactory.getInstance(RetrofitFactory.BASE_URL);
        mStripeService = retrofit.create(StripeService.class);

        String cart_id = sessionManager.getParticularField(SessionManager.CART_ID);
        String address_id = sessionManager.getParticularField(SessionManager.ADDRESS_ID);
        String uid = sessionManager.getParticularField(SessionManager.USER_ID);

        Map<String, String> apiParamMap = new HashMap<>();
        apiParamMap.put("cart_id", cart_id);
        apiParamMap.put("address_id", address_id);
        apiParamMap.put("uid", uid);

        mCompositeSubscription.add(
                mStripeService.UpdateCardStatus(apiParamMap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ResponseBody>() {
                            @Override
                            public void call(ResponseBody response) {
                                pDialog.dismiss();
                                try {
                                    String rawKey = response.string();
                                    Mylogger.getInstance().Logit("rawKey", "rawKey: " + rawKey);
                                    if (!Utility.isEmptyOrNot(rawKey)) {
                                        JSONObject jsonObject = new JSONObject(rawKey);
                                        JSONObject obj4 = null;
                                        Iterator<String> keys = jsonObject.keys();
                                        while (keys.hasNext()) {
                                            String keyValue = (String) keys.next();
                                            obj4 = jsonObject.getJSONObject(keyValue);

                                            //getting string values with keys- pageid and title
                                            String status = obj4.getString("200");
                                            if (status.equals("success")) {
                                                // GOTO HOME
                                                showAlertPayment("Success");
                                                getActivity().startActivity(new Intent(getActivity(),ActivityDashboard.class));
                                            } else {
                                                displayError(status);
                                            }
                                        }
                                    }
                                } catch (Exception iox) {
                                    System.out.println("iox: " + iox);
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                pDialog.dismiss();
                            }
                        }));
    }

    private void showAlertPayment(String msg) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                _context).create();
        alertDialog.setTitle("Payment Successful");
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                headerUserName.performClick();
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

/*
    private void completePurchase() {
        {

            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("CartId",sessionManager.getParticularField(SessionManager.CART_ID));
                jsonObject.put("UserId",sessionManager.getParticularField(SessionManager.USER_ID));
                jsonObject.put("ClientId",sessionManager.getParticularField(SessionManager.CLIENT_ID)+"\n"+ MyShopeAdapter.extra);
                // jsonObject.put("AddressId",sessionManager.getParticularField(SessionManager.ADDRESS_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }



            float amount = Float.valueOf(MycartFragment.tt);
            float amount1=amount*100;
            String aa = String.valueOf(amount1);
            int pp= aa.indexOf('.');
            String uu =aa.substring(0,pp);



            String url = "https://api.stripe.com/v1/charges?amount=" + uu + "&currency=" + "aud" + "&customer=" + sessionManager.getParticularField(SessionManager.CUSTOMER_ID) + "&description=" + jsonObject.toString()+ "&receipt_email="+MycartDeliveryFragment.a_email+    "&shipping[name]="+sessionManager.getParticularField(SessionManager.FIRST_NAME)+"&shipping[phone]="+MycartDeliveryFragment.a_mobile+"&shipping[address][country]="+MycartDeliveryFragment.a_country+"&shipping[address][state]="+MycartDeliveryFragment.a_state+"&shipping[address][city]="+MycartDeliveryFragment.a1+"&shipping[address][postal_code]="+MycartDeliveryFragment.a2+"&shipping[address][line1]="+MycartDeliveryFragment.a3+"&shipping[address][line2]="+MycartDeliveryFragment.a4;
Log.i("TEST","new complete purchase "+url);
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
Log.i("TEST","charges res "+response);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.i("TEST","charges err "+error.getMessage().toString());
                }
            }

            )

            {@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer sk_test_yb1n5THFv5CncjmECieJmzPa");
                return params;
            }
                public String getBodyContentType()
                {
                    return "application/json; charset=utf-8";
                }
            };


            jsonObjReq.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

            // Access the RequestQueue through your singleton class.
            queue.add(jsonObjReq);
            queue.start();
        }
    }
*/
}