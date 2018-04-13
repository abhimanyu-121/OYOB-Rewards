package com.oyob.controller.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.oyob.controller.R;
import com.oyob.controller.model.ModelInterests;
import com.oyob.controller.utils.AppConstants;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Narender Kumar on 2/24/2018.
 * Prominent Developer
 * narender.kumar.nishad@gmail.com
 */

public class FragmentAAActivate1 extends FragmentBase {

    AppCompatEditText faaa1_acet_first_name, faaa1_acet_last_name, faaa1_acet_dob;
    CheckBox faaa1_cb_travel, faaa1_cb_food, faaa1_cb_health, faaa1_cb_shopping, faaa1_cb_entertainment,
            faaa1_cb_home, faaa1_cb_newsletter;
    AppCompatTextView faaa1_actv_next;
    ArrayList<ModelInterests> interests = new ArrayList<>();
    ArrayList<String> interest_id = new ArrayList<>();
    Calendar myCalendar = Calendar.getInstance();

    public static FragmentAAActivate1 newInstance() {
        Bundle args = new Bundle();
        FragmentAAActivate1 fragment = new FragmentAAActivate1();
        fragment.setArguments(args);
        return fragment;
    }

    ActivateOne activateOne;

    public interface ActivateOne {
        void one(String firstName, String lastName, String dob, ArrayList<String> interest_id, String newsletter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container _context has implemented
        // the callback interface. If not, it throws an exception
        try {
            activateOne = (ActivateOne) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement LogoutUser");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aa_activate1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        faaa1_acet_first_name = view.findViewById(R.id.faaa1_acet_first_name);
        faaa1_acet_last_name = view.findViewById(R.id.faaa1_acet_last_name);
        faaa1_acet_dob = view.findViewById(R.id.faaa1_acet_dob);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        faaa1_acet_dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(_context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        faaa1_cb_travel = view.findViewById(R.id.faaa1_cb_travel);
        faaa1_cb_food = view.findViewById(R.id.faaa1_cb_food);
        faaa1_cb_health = view.findViewById(R.id.faaa1_cb_health);
        faaa1_cb_shopping = view.findViewById(R.id.faaa1_cb_shopping);
        faaa1_cb_entertainment = view.findViewById(R.id.faaa1_cb_entertainment);
        faaa1_cb_home = view.findViewById(R.id.faaa1_cb_home);
        faaa1_cb_newsletter = view.findViewById(R.id.faaa1_cb_newsletter);
        faaa1_actv_next = view.findViewById(R.id.faaa1_actv_next);
        faaa1_actv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDetails();
            }
        });
        if (isNetworkConnected(_context)) {
            getInterest();
        } else {
            showToastLong(_context, getString(R.string.check_internet_connectivity));
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        faaa1_acet_dob.setText(sdf.format(myCalendar.getTime()));
    }

    public void getInterest() {
        RequestQueue requestQueue = Volley.newRequestQueue(_context);
        dialog.show();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, AppConstants.GET_INTEREST, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dialog.dismiss();
                        Log.d("interest", "onResponse: " + response);
                        try {
                            if (response != null) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject object = response.getJSONObject(i);
                                    ModelInterests interest = new ModelInterests(object.getString("id"),
                                            object.getString("name"));
                                    interests.add(interest);
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

    private void checkDetails() {
        String first_name = faaa1_acet_first_name.getText().toString().trim();
        String last_name = faaa1_acet_last_name.getText().toString().trim();
        String dob = faaa1_acet_dob.getText().toString().trim();
        String travel = "1";
        String shop = "2";
        String food = "3";
        String entertainment = "4";
        String health = "5";
        String home_lifestyle = "6";
        String newsletter = "0";

        if (first_name.length() < 2) {
            showToastLong(_context, getString(R.string.first_name_validation));
            return;
        }

        if (last_name.length() < 2) {
            showToastLong(_context, getString(R.string.last_name_validation));
            return;
        }

        if (faaa1_cb_travel.isChecked()) {
            if (!interest_id.contains(travel))
                interest_id.add(travel);
        } else {
            if (interest_id.contains(travel)) {
                interest_id.remove(travel);
            }
        }

        if (faaa1_cb_shopping.isChecked()) {
            if (!interest_id.contains(shop))
                interest_id.add(shop);

        } else {
            if (interest_id.contains(shop)) {
                interest_id.remove(shop);
            }
        }

        if (faaa1_cb_food.isChecked()) {
            if (!interest_id.contains(food))
                interest_id.add(food);
        }

        if (faaa1_cb_entertainment.isChecked()) {
            if (!interest_id.contains(entertainment))
                interest_id.add(entertainment);
        } else {
            if (interest_id.contains(entertainment)) {
                interest_id.remove(entertainment);
            }
        }

        if (faaa1_cb_health.isChecked()) {
            if (!interest_id.contains(health))
                interest_id.add(health);
        } else {
            if (interest_id.contains(health)) {
                interest_id.remove(health);
            }
        }

        if (faaa1_cb_home.isChecked()) {
            if (!interest_id.contains(home_lifestyle))
                interest_id.add(home_lifestyle);
        } else {
            if (interest_id.contains(home_lifestyle)) {
                interest_id.remove(home_lifestyle);
            }
        }

        if (faaa1_cb_newsletter.isChecked()) {
            newsletter = "1";
        } else {
            newsletter = "0";
        }

        activateOne.one(first_name, last_name, dob, interest_id, newsletter);
    }
}