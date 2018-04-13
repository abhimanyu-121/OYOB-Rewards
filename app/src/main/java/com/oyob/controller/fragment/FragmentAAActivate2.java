package com.oyob.controller.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import com.oyob.controller.R;
import com.oyob.controller.activity.ActivityTermsandConditions;
import com.oyob.controller.model.ModelCountries;
import com.oyob.controller.model.ModelStates;
import com.oyob.controller.utils.AppConstants;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Narender Kumar on 2/24/2018.
 * Prominent Developer
 * narender.kumar.nishad@gmail.com
 */

public class FragmentAAActivate2 extends FragmentBase implements View.OnClickListener {

    AppCompatEditText faaa2_acet_country, faaa2_acet_state, faaa2_acet_postcode,
            faaa2_acet_email, faaa2_acet_mobile;
    AppCompatCheckBox faaa2_accb_tc;
    AppCompatTextView faaa2_actv_next, terms_conds;
    ArrayList<ModelCountries> countries = new ArrayList<>();
    String state_code = "";
    ArrayList<ModelStates> states = new ArrayList<>();
    ArrayList<String> stringCountries = new ArrayList<>();
    ArrayList<String> stringStates = new ArrayList<>();
    private String country_code="AU", state_code1;

    public static FragmentAAActivate2 newInstance() {
        Bundle args = new Bundle();
        FragmentAAActivate2 fragment = new FragmentAAActivate2();
        fragment.setArguments(args);
        return fragment;
    }

    ActivateTwo activateTwo;

    public interface ActivateTwo {
        public void two(String country, String state, String postcode, String email, String mobile);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container _context has implemented
        // the callback interface. If not, it throws an exception
        try {
            activateTwo = (ActivateTwo) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement LogoutUser");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aa_activate2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        faaa2_acet_country = view.findViewById(R.id.faaa2_acet_country);
        faaa2_acet_country.setFocusable(false);
        faaa2_acet_country.setClickable(true);
        faaa2_acet_country.setText("Australia");
        faaa2_acet_country.setLongClickable(false);
        faaa2_acet_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countriesDialog(faaa2_acet_country, stringCountries, countries);
                getStates(country_code);
            }
        });
        faaa2_acet_state = view.findViewById(R.id.faaa2_acet_state);
        faaa2_acet_state.setFocusable(false);
        faaa2_acet_state.setClickable(true);
        faaa2_acet_state.setLongClickable(false);
        faaa2_acet_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statesDialog(faaa2_acet_state, stringStates, states);
            }
        });
        faaa2_acet_postcode = view.findViewById(R.id.faaa2_acet_postcode);
        faaa2_acet_email = view.findViewById(R.id.faaa2_acet_email);
        faaa2_acet_mobile = view.findViewById(R.id.faaa2_acet_mobile);
        faaa2_accb_tc = view.findViewById(R.id.faaa2_accb_tc);
        faaa2_actv_next = view.findViewById(R.id.faaa2_actv_next);
        terms_conds = view.findViewById(R.id.terms_conds);
        faaa2_actv_next.setOnClickListener(this);
        if (isNetworkConnected(_context))
            getCountriesAndStates();
        else
            showToastLong(_context, getString(R.string.check_internet_connectivity));

        terms_conds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(_context, ActivityTermsandConditions.class);
                _context.startActivity(intent1);
            }
        });
    }

    public void getCountriesAndStates() {
        RequestQueue requestQueue = Volley.newRequestQueue(_context);
        dialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, AppConstants.GET_COUNTRIES, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        Log.i("TEST", "country: " + response);
                        try {
                            if (response != null) {

                                JSONArray jsonArray=response.getJSONArray("country_list");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    ModelCountries country = new ModelCountries(jsonObject.getString("id"),
                                            jsonObject.getString("name"));
                                    countries.add(country);
                                    stringCountries.add(jsonObject.getString("name"));
                                }

                                for (int i = 0; i < response.getJSONArray("state_list").length(); i++) {
                                    JSONArray statesArray = response.getJSONArray("state_list");
                                    JSONObject statesObj = statesArray.optJSONObject(i);

                                    if(statesObj.getString("country_cd").equalsIgnoreCase("AU")) {
                                        ModelStates state = new ModelStates(statesObj.getString("state_cd"),
                                                statesObj.getString("state_name"),
                                                statesObj.getString("country_cd"));
                                        states.add(state);
                                        stringStates.add(statesObj.getString("state_name"));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //getting string values with keys- pageid and title
                    // get course_description
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i("Popular Offers", "Error :" + error.toString());
                        dialog.dismiss();
                    }
                });
        // Access the RequestQueue through your singleton class.
        requestQueue.add(jsObjRequest);
    }

    private void countriesDialog(final EditText editText, final ArrayList<String> _countries, final ArrayList<ModelCountries> countriesArrayList) {
        AlertDialog.Builder b = new AlertDialog.Builder(_context);
        final ArrayAdapter<String> countries_array = new ArrayAdapter<>(_context, android.R.layout.simple_list_item_1, _countries);
        b.setSingleChoiceItems(countries_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                editText.setText(countries_array.getItem(index));
                country_code=countriesArrayList.get(index).getCountryId();
                getStates(country_code);
                dialog.dismiss();

            }
        });
        faaa2_acet_country.setText(editText.getText().toString());
        faaa2_acet_state.setText("");
        b.show();
    }

    private void statesDialog(final EditText editText, ArrayList<String> _states, final ArrayList<ModelStates> statesArrayList) {
        AlertDialog.Builder b = new AlertDialog.Builder(_context);
        final ArrayAdapter<String> states_array = new ArrayAdapter<>(_context, android.R.layout.simple_list_item_1, _states);
        b.setSingleChoiceItems(states_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                editText.setText(states_array.getItem(index));
                state_code = statesArrayList.get(index).getStateCode();
                dialog.dismiss();
            }
        });
        faaa2_acet_state.setText(editText.getText().toString());
        b.show();
    }

    private void checkDetails() {
        String country = faaa2_acet_country.getText().toString().trim();
        String state = faaa2_acet_state.getText().toString().trim();
        String passcode = faaa2_acet_postcode.getText().toString().trim();
        String email = faaa2_acet_email.getText().toString().trim();
        String mobile = faaa2_acet_mobile.getText().toString().trim();

        if (state.length() < 2) {
            showToastLong(_context, getString(R.string.state_cannot_be_empty));
            return;
        }

        else if (passcode.length()<0) {
            showToastLong(_context, "Postcode cannot be empty");
            return;
        }

        else if (!isEmailValid(email)) {
            showToastLong(_context, getString(R.string.invalid_email_address));
            return;
        }

        else if(mobile.length()!=10)
        {
            showToastLong(_context, "Please enter the correct mobile number");
            return;
        }

        else if (!faaa2_accb_tc.isChecked()) {
            showToastLong(_context, getString(R.string.agree_terms_and_conditions));
            return;
        }

        else
        {
            activateTwo.two(country, state_code, passcode, email, mobile);
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onClick(View view) {
        checkDetails();
    }

    public void getStates(final String dd) {
        stringStates.clear();
        states.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(_context);
        dialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, AppConstants.GET_COUNTRIES, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        try {
                            if (response != null) {

                                for (int i = 0; i < response.getJSONArray("state_list").length(); i++) {
                                    JSONArray statesArray = response.getJSONArray("state_list");
                                    JSONObject statesObj = statesArray.optJSONObject(i);

                                    if(statesObj.getString("country_cd").equalsIgnoreCase(dd)) {
                                        ModelStates state = new ModelStates(statesObj.getString("state_cd"),
                                                statesObj.getString("state_name"),
                                                statesObj.getString("country_cd"));
                                        states.add(state);
                                        stringStates.add(statesObj.getString("state_name"));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //getting string values with keys- pageid and title
                    // get course_description
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i("Popular Offers", "Error :" + error.toString());
                        dialog.dismiss();
                    }
                });
        // Access the RequestQueue through your singleton class.
        requestQueue.add(jsObjRequest);
    }

}