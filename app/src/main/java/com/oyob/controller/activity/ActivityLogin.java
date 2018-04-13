package com.oyob.controller.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.oyob.controller.Analytics.FirebaseAnalyticsClass;
import com.oyob.controller.R;
import com.oyob.controller.dataBaseHelper.PasswordHelper;
import com.oyob.controller.utils.SessionManager;
import com.oyob.controller.utils.TinyDbKeys;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.oyob.controller.utils.AppConstants.GET_USER;


/**
 * Created by 121 on 9/3/2017.
 */
public class ActivityLogin extends ActivityBase implements TextWatcher, View.OnClickListener {
    public static final String TAG = ActivityLogin.class.getSimpleName();
    static boolean isLoginOtp;
    static String client = null;
    static String GET_USER_URL = "";
    EditText editText_one, editText_two, editText_three, editText_four;
    String one, two, three, four, five, six;
    TextView loginTextView, insertPin, as_tv_forget_pin, as_tv_create_pin;
    Button button;
    String checkLoginPin = null;
    PasswordHelper passwordHelper;
    String password;
    String androidId;
    Date currentTime;
    DateFormat df;
    String reportDate;
    //Activation code: Hy2CYH key

    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currentTime = Calendar.getInstance().getTime();
        reportDate = df.format(currentTime);

        loginTextView = findViewById(R.id.al_tv_login);
        insertPin = findViewById(R.id.al_tv_insertpin);

        as_tv_forget_pin = findViewById(R.id.as_tv_forget_pin);
        as_tv_forget_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ActivityCreatePIN.class));
                finish();
            }
        });
        as_tv_create_pin = findViewById(R.id.as_tv_create_pin);
        as_tv_create_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ActivityCreatePIN.class));
                finish();
            }
        });
        button = findViewById(R.id.loginButton);
        button.setOnClickListener(this);

        editText_one = findViewById(R.id.editText1);
        editText_two = findViewById(R.id.editText2);
        editText_three = findViewById(R.id.editText3);
        editText_four = findViewById(R.id.editText4);

        //myEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        /*editText_one.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_one.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_two.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_two.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_three.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_three.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_four.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_four.setTransformationMethod(new MyPasswordTransformationMethod());*/

        ImageView btn_back = findViewById(R.id.al_iv_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        editText_four.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    /* handle action here */
                    handled = true;
                }
                return handled;
            }
        });

        editText_one.addTextChangedListener(this);
        editText_two.addTextChangedListener(this);
        editText_three.addTextChangedListener(this);
        editText_four.addTextChangedListener(this);

        one = editText_one.getText().toString();
        two = editText_two.getText().toString();
        three = editText_three.getText().toString();
        four = editText_four.getText().toString();

        editText_one.requestFocus();

        editText_two.setOnKeyListener(new View.OnKeyListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String two = editText_two.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && two.trim().length() == 0) {
                    editText_one.setText("");
                    editText_one.requestFocus();
                    System.out.println("text delete in  editText2" + editText_two);
                }
                return false;
            }
        });

        editText_three.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String three = editText_three.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && three.trim().length() == 0) {
                    editText_two.setText("");
                    editText_two.requestFocus();
                    System.out.println("text delete in  editText3" + editText_three);
                }
                return false;
            }
        });

        editText_four.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String four = editText_four.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && four.trim().length() == 0) {
                    editText_three.setText("");
                    editText_three.requestFocus();
                    System.out.println("text delete in  editText3" + editText_four);
                }
                return false;
            }
        });

        editText_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text = editable.toString();

                boolean isAllFilled = isAllFieldsFilled();
                if (isAllFilled) {
                    hideKeyboardwithoutPopulate(ActivityLogin.this);
                    button.performClick();
                }
                Log.d(TAG, "" + "afterTextChanged: ");
            }
        });
        /*if (!TextUtils.isEmpty(sessionManager.getParticularField(SessionManager.CLIENT_ID)) &&
                !TextUtils.isEmpty(sessionManager.getParticularField(SessionManager.USER_ID)))
            saveData();*/
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        if (charSequence == editText_one.getEditableText() && after != 0) {
            editText_two.requestFocus();
        } else if (charSequence == editText_two.getEditableText() && after != 0) {
            editText_three.requestFocus();
        } else if (charSequence == editText_three.getEditableText() && after != 0) {
            editText_four.requestFocus();
        } else if (charSequence == editText_four.getEditableText() && after != 0) {

        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    private boolean isAllFieldsFilled() {

        boolean isAllFilled = false;

        if (editText_one.getText().toString().length() > 0 && editText_two.getText().toString().length() > 0 && editText_three.getText().toString().length() > 0 && editText_four.getText().toString().length() > 0) {
            isAllFilled = true;
            return isAllFilled;
        }

        return isAllFilled;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        editText_four.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    return true;
                }
                return false;
            }


        });

    }

    @Override
    public void onClick(View v) {
        FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "Button", "LoginScreen"));

       /* String pin = "654321";
        if (pin.equals(editText_one.getText().toString() + editText_two.getText().toString() + editText_three.getText().toString() +
                editText_four.getText().toString() + editText_five.getText().toString() + editText_six.getText().toString())) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else if((editText_one.getText().toString() + editText_two.getText().toString() + editText_three.getText().toString() +
                editText_four.getText().toString() + editText_five.getText().toString() + editText_six.getText().toString()).length() == 6) {
            Toast.makeText(getApplicationContext(),"Please enter valid otp",Toast.LENGTH_SHORT).show();
        }*/
        checkLoginPin = editText_one.getText().toString() + editText_two.getText().toString() + editText_three.getText().toString() +
                editText_four.getText().toString();
        //Boolean pinExist = passwordHelper.isExist(checkLoginPin);

        if (sessionManager.getParticularField(SessionManager.PIN) != null) {

            if (sessionManager.getParticularField(SessionManager.PIN).equals(checkLoginPin) && checkLoginPin != null) {
                sessionManager.updatePin(checkLoginPin);
                saveData();
            } else if (!sessionManager.getParticularField(SessionManager.PIN).equals("")) {
                showAlert("Wrong PIN");
            } else {
                showAlert("Please activate your profile");
            }
        } else {
            showAlert("Please activate your profile");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void saveData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        dialog.show();
        final String value = sessionManager.getParticularField(SessionManager.USER_NAME);
        client = sessionManager.getParticularField(SessionManager.CLIENT_ID);

        StringRequest postReq = new StringRequest(Request.Method.POST, GET_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.i("TEST", "get userrrr response" + response);
                        /*XmlToJson xmlToJson = new XmlToJson.Builder(response).build();
                        JSONObject jsonObject = xmlToJson.toJson();
                        tinyDB.putString(TinyDbKeys.USER_DATA, jsonObject.toString());*/
                        Log.d(TAG, "response" + tinyDB.getString(TinyDbKeys.USER_DATA));
                        try {
                            if (response.contains("cart_id")) {
                                JSONArray array = new JSONArray(response);
                                JSONObject user = array.getJSONObject(0);
                                Log.d(TAG, "User data" + user);
                                sessionManager.updateNewsletter(user.getString("newsletter"));
                                sessionManager.updateClientId(user.getString("client_id"));
                                sessionManager.updateMobile(user.getString("mobile"));
                                sessionManager.updateDomainId(user.getString("domain_id"));
                                sessionManager.updateCId(user.getString("domain_id"));
                                sessionManager.updateFirstName(user.getString("first_name"));
                                sessionManager.updateLastName(user.getString("last_name"));
                                sessionManager.updateLoyality(user.getString("loyality"));
                                sessionManager.updateEmail(user.getString("email"));
                                sessionManager.updateCardExt(user.getString("card_ext"));
                                sessionManager.updateState(user.getString("state"));
                                sessionManager.updateMerchantFee(user.getString("merchnat_fee"));
                                sessionManager.updateFacebookLink(user.getString("facebook_link"));
                                sessionManager.updateClientName(user.getString("client_name"));
                                sessionManager.updateCountry(user.getString("country"));
                                sessionManager.updateUserName(user.getString("username"));
                                sessionManager.updateUserId(user.getString("id"));
                                sessionManager.updateTwitterLink(user.getString("twitter_link"));
                                sessionManager.updateUserType(user.getString("type"));
                                sessionManager.updateCartId(user.getString("cart_id"));
                                sessionManager.updateIsFirstRun(false);
                                Log.e(TAG, "Data Save in SharedPref");

                                startActivity(new Intent(context, ActivityDashboard.class));
                                finish();
                            } else {
                                showToastLong(context, getString(R.string.failed_to_get_data_from_server));
                            }
                        } catch (Exception je) {
                            System.out.println(je.toString());
                        }
                        Log.d(TAG, "Save Data onResponse: " + response);

                        Log.d(TAG, "" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uname", sessionManager.getParticularField(SessionManager.USER_ID));
                params.put("client_id", sessionManager.getParticularField(SessionManager.CLIENT_ID));
                params.put("response_type", "json");
                return params;
            }
        };
        requestQueue.add(postReq);
    }

    private void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                ActivityLogin.this).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public class MyPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    private class PinChange extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            sessionManager.updatePin(checkLoginPin);
            String response = tinyDB.getString(TinyDbKeys.USER_DATA);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject root = jsonObject.getJSONObject("root");
                Log.i("TEST", "String to JSON" + jsonObject);
                JSONObject user = root.getJSONObject("user");
                sessionManager.updateNewsletter(user.getString("newsletter"));
                sessionManager.updateClientId(user.getString("client_id"));
                sessionManager.updateMobile(user.getString("mobile"));
                sessionManager.updateDomainId(user.getString("domain_id"));
                sessionManager.updateCId(user.getString("domain_id"));
                sessionManager.updateFirstName(user.getString("first_name"));
                sessionManager.updateLastName(user.getString("last_name"));
                sessionManager.updateLoyality(user.getString("loyality"));
                sessionManager.updateEmail(user.getString("email"));
                sessionManager.updateCardExt(user.getString("card_ext"));
                sessionManager.updateState(user.getString("state"));
                sessionManager.updateMerchantFee(user.getString("merchnat_fee"));
                sessionManager.updateFacebookLink(user.getString("facebook_link"));
                sessionManager.updateClientName(user.getString("client_name"));
                sessionManager.updateCountry(user.getString("country"));
                sessionManager.updateUserName(user.getString("username"));
                sessionManager.updateUserId(user.getString("id"));
                sessionManager.updateTwitterLink(user.getString("twitter_link"));
                sessionManager.updateUserType(user.getString("type"));
                Log.e(TAG, "Data Save in SharedPref");
                startActivity(new Intent(context, ActivityDashboard.class));
                finish();
            } catch (Exception je) {
                System.out.println(je.toString());
            }
            Log.d(TAG, "Save Data onResponse: " + response);
            dialog.dismiss();
        }
    }
}