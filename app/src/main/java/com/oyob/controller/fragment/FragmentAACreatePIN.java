package com.oyob.controller.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.oyob.controller.activity.ActivityLogin;
import com.oyob.controller.utils.SessionManager;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.oyob.controller.utils.AppConstants.PIN_CREATE;


/**
 * Created by Ramasamy on 9/4/2017.
 */
public class FragmentAACreatePIN extends FragmentBase implements View.OnClickListener {

    public static final String TAG = FragmentAACreatePIN.class.getSimpleName();
    EditText editText_one, editText_two, editText_three, editText_four;
    EditText editText_seven, editText_eight, editText_nine, editText_ten;
    String one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    Button button;

    String s1 = null;
    String s2 = null;
    //private FirebaseAnalytics mFirebaseAnalytics;
    String androidId;
    Date currentTime;
    String userId = "";
    String errorcode = "";
    DateFormat df;
    String reportDate;

    public static FragmentAACreatePIN newInstance() {
        Bundle args = new Bundle();
        FragmentAACreatePIN fragment = new FragmentAACreatePIN();
        fragment.setArguments(args);
        return fragment;
    }

    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aa_activate3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currentTime = Calendar.getInstance().getTime();
        reportDate = df.format(currentTime);

        editText_one = view.findViewById(R.id.editText1);
        editText_two = view.findViewById(R.id.editText2);
        editText_three = view.findViewById(R.id.editText3);
        editText_four = view.findViewById(R.id.editText4);

        editText_seven = view.findViewById(R.id.editText7);
        editText_eight = view.findViewById(R.id.editText8);
        editText_nine = view.findViewById(R.id.editText9);
        editText_ten = view.findViewById(R.id.editText10);

        editText_four.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
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

        button = view.findViewById(R.id.pinActivateButton);
        button.setOnClickListener(this);

        editText_eight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
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

       /* editText_one.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_one.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_two.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_two.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_three.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_three.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_four.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_four.setTransformationMethod(new MyPasswordTransformationMethod());

        editText_seven.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_seven.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_eight.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_eight.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_nine.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_nine.setTransformationMethod(new MyPasswordTransformationMethod());
        editText_ten.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_ten.setTransformationMethod(new MyPasswordTransformationMethod());*/

        one = editText_one.getText().toString();
        two = editText_two.getText().toString();
        three = editText_three.getText().toString();
        four = editText_four.getText().toString();

        seven = editText_seven.getText().toString();
        eight = editText_eight.getText().toString();
        nine = editText_nine.getText().toString();
        ten = editText_ten.getText().toString();

        editText_one.requestFocus();

        editText_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_two.requestFocus();
                }
            }
        });

        editText_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_three.requestFocus();
                }
            }
        });

        editText_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_four.requestFocus();
                }
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

                if (editable.toString().length() > 0) {
                    editText_seven.requestFocus();

                }
            }
        });


        editText_seven.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_eight.requestFocus();
                }
            }
        });

        editText_eight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_nine.requestFocus();
                }
            }
        });

        editText_nine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    editText_ten.requestFocus();
                }
            }
        });
        editText_ten.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > 0) {
                    hideKeyboardwithoutPopulate(getActivity());
                }
            }
        });


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


        editText_seven.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String six = editText_seven.getText().toString();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && six.trim().length() == 0) {
                    editText_four.setText("");
                    editText_four.requestFocus();
                    System.out.println("text delete in  editText3" + editText_four);
                }
                return false;
            }
        });


        editText_eight.setOnKeyListener(new View.OnKeyListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String two = editText_eight.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && two.trim().length() == 0) {
                    editText_seven.setText("");
                    editText_seven.requestFocus();
                    System.out.println("text delete in  editText2" + editText_seven);
                }
                return false;
            }
        });

        editText_nine.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String three = editText_nine.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && three.trim().length() == 0) {
                    editText_eight.setText("");
                    editText_eight.requestFocus();
                    System.out.println("text delete in  editText3" + editText_eight);
                }
                return false;
            }
        });

        editText_ten.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                String four = editText_ten.getText().toString().trim();

                if (keycode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == 0 && four.trim().length() == 0) {
                    editText_nine.setText("");
                    editText_nine.requestFocus();
                    System.out.println("text delete in  editText3" + editText_ten);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Boolean aBoolean = null;
        s1 = editText_one.getText().toString() + editText_two.getText().toString() + editText_three.getText().toString() +
                editText_four.getText().toString();
        s2 = (editText_seven.getText().toString() + editText_eight.getText().toString() + editText_nine.getText().toString() +
                editText_ten.getText().toString());
        if (s1.equals(s2) && s1 != null && s2 != null) {

              /*  APILink.getApiLinkInstance().sendFirstTimePinDetails(this, s1,s2);
                Intent intent= new Intent(_context, LoginActivity.class);
                startActivity(intent);*/


            userId = sessionManager.getParticularField(SessionManager.USER_ID);

            //PinApiCall(String.valueOf(userId), String.valueOf(s1));

            PinChange pinChange = new PinChange();
            pinChange.execute();

            FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(TAG, "Button", "PinActivation"));

            /*PinApiCall(String.valueOf(6775046),String.valueOf(123456));*/
            // Building  notification

        } else if (!s1.isEmpty() && s2.isEmpty()) {
            Toast.makeText(_context, "Please enter valid PIN", Toast.LENGTH_SHORT).show();
        } else if (!s2.isEmpty() && s1.isEmpty()) {
            Toast.makeText(_context, "Please enter valid PIN", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(_context, "New PIN and confirm PIN should be same", Toast.LENGTH_SHORT).show();
        }
        /*if (aBoolean == true) {
                *//*Toast.makeText(_context, "Successfully Created Pin", Toast.LENGTH_LONG).show();*//*
        } else {
                *//*Toast.makeText(_context, "failure", Toast.LENGTH_LONG).show();*//*
        }*/
        /*new BackgroundTask().execute();*/

    }

    public void PinApiCall(final String userid, final String PinValue) {
        RequestQueue requestQueue = Volley.newRequestQueue(_context);
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Pin Activating...");
        pDialog.show();
        StringRequest postReq = new StringRequest(Request.Method.POST, PIN_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                showToastShort(_context, response);
                Log.d("Create Pin", "Pin Fragment Response" + response);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    if (obj != null) {
                        Intent intent = new Intent(_context, ActivityLogin.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(_context, "New PIN and confirm PIN should be same", Toast.LENGTH_LONG).show();
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);
                params.put("password", PinValue);
                return params;
            }
        };

        requestQueue.add(postReq);
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
            sessionManager.updatePin(s1);
            startActivity(new Intent(_context, ActivityLogin.class));
            getActivity().finish();
            dialog.dismiss();
        }
    }
}