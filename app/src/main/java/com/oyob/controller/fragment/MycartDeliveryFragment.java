package com.oyob.controller.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.oyob.controller.R;
import com.oyob.controller.activity.MyShopActivity;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.SessionManager;
import com.oyob.controller.utils.Utility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by piyush on
 */
public class MycartDeliveryFragment extends FragmentBase implements AdapterView.OnItemClickListener {

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    //------------ make your specific key ------------
    //private static final String API_KEY = "AIzaSyAU9ShujnIg3IDQxtPr7Q1qOvFVdwNmWc4";
    private static final String API_KEY = "AIzaSyAyM4AehJFgEAXOFzkxBvlU4MnUqmW7X10";   //our key
    AutoCompleteTextView autoCompView;
    ArrayList<String> referenceList = null;
    String placeURL;
    double latitudeA;
    double longitudeA;
    private Context _context;
    private EditText edt_email, edt_mobile, edt_address2, edt_country, edt_state, edt_pincode, edt_city;
    private Button btn_next;
    private String address_id;
    private String TAG = "MainPageFragment";
    public static String a1, a2, a3, a4, a_state, a_country, a_mobile, a_email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(_context).inflate(R.layout.my_cart__delivery_fragment, container, false);
        Button btnNext = (Button) rootView.findViewById(R.id.btn_next);

        autoCompView = rootView.findViewById(R.id.autoCompleteTextView);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(_context, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        edt_email = rootView.findViewById(R.id.edt_email);
        edt_mobile = rootView.findViewById(R.id.edt_mobile);
        edt_address2 = rootView.findViewById(R.id.edt_address2);
        edt_country = rootView.findViewById(R.id.edt_country);
        edt_state = rootView.findViewById(R.id.edt_state);
        edt_pincode = rootView.findViewById(R.id.edt_pincode);
        edt_city = rootView.findViewById(R.id.edt_city);
        btn_next = rootView.findViewById(R.id.btn_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edt_email.getText().toString();
                String mobile = edt_mobile.getText().toString();
                String address1 = autoCompView.getText().toString();
                String address2 = edt_address2.getText().toString();
                String country = edt_country.getText().toString();
                String state = edt_state.getText().toString();
                String pincode = edt_pincode.getText().toString();
                String city = edt_city.getText().toString();

                if (TextUtils.isEmpty(email) || Utility.isEmailValid(email) == false) {
                    showAlert("Please enter valid email");
                    return;
                } else if (TextUtils.isEmpty(mobile)) {
                    showAlert("Please enter mobile");
                    return;
                } else if (TextUtils.isEmpty(address1)) {
                    showAlert("Please enter address1");
                    return;
                } else if (TextUtils.isEmpty(country)) {
                    showAlert("Please enter country");
                    return;
                } else if (TextUtils.isEmpty(state)) {
                    showAlert("Please enter state");
                    return;
                } else if (TextUtils.isEmpty(city)) {
                    showAlert("Please enter city");
                    return;
                } else if (TextUtils.isEmpty(pincode)) {
                    showAlert("Please enter pincode");
                    return;
                } else {

                    String cid = sessionManager.getParticularField(SessionManager.CLIENT_ID);
                    String userId = sessionManager.getParticularField(SessionManager.USER_ID);
                    String username = sessionManager.getParticularField(SessionManager.USER_NAME);
                    String url = AppConstants.SERVER_ROOT + "user_shipping_details.php?user_id=" + userId + "&client_id=" + cid + "&email=" + email + "&mobile=" + mobile + "&address1=" + address1 + "&address2=" + address2 + "&city=" + city + "&state=" + state + "&pincode=" + pincode + "&country=" + country + "&name=" + username + "&suburb="+""+"&address_id=" + address_id;

                    final ProgressDialog pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Loading...");
                    pDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    pDialog.dismiss();
                                    // Display the first 500 characters of the response string.
                                    Log.d(TAG, "onResponse: user_shipping_details" + response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.has("response")) {
                                            address_id = jsonObject.getString("address_id");
                                            sessionManager.getParticularField(SessionManager.ADDRESS_ID);

                                            // showAlert(jsonObject.getString("response"));
                                            // redirect to payment
                                            MyShopActivity.ClickPayment();
                                        } else {
                                            showAlert("Address not saved.");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                        }
                    });
                    stringRequest.setShouldCache(false);
                    queue.add(stringRequest);
                }
            }
        });

        getUserAddress();

        return rootView;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        placeURL = getPlaceDetailsUrl(referenceList.get(position));
        System.out.println("====placeURL=====" + placeURL);
        new PlacesParseAsyn().execute();
    }

    public ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;
        referenceList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=true&key=" + API_KEY);
            //sb.append("&components=country:gr");
            sb.append("&types=address");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            URL url = new URL(sb.toString());
            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("t", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("d", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            System.out.println("========jsonResults.toString()=========" + jsonResults.toString());

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            referenceList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                referenceList.add(predsJsonArray.getJSONObject(i).getString("reference"));
            }

        } catch (JSONException e) {
            Log.d("MyCart", "Cannot process JSON results", e);
        }

        return resultList;
    }

    //================================Get Address detail from Google API=============================
    private String getPlaceDetailsUrl(String ref) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyAyM4AehJFgEAXOFzkxBvlU4MnUqmW7X10";

        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

        return url;
    }

    public void getUserAddress() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        String cid = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        String uname = sessionManager.getParticularField(SessionManager.USER_ID);
        String url = AppConstants.SERVER_ROOT + "get_user_address.php?uname=" + uname + "&client_id=" + cid+"&response_type="+"json";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "onResponse: get_user_address" + response);
                        Log.i("TEST","user address "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("status")) {
                                showAlert(jsonObject.getString("status"));
                            } else if (jsonObject.has("address")) {
                                JSONArray address = jsonObject.getJSONArray("address");
                                for (int i = 0; i < address.length(); i++) {
                                    JSONObject base = address.getJSONObject(i);
                                    address_id = base.getString("id");
                                    sessionManager.updateAddress(address_id);
                                    sessionManager.getParticularField(SessionManager.ADDRESS_ID);
                                    String email = base.getString("email");
                                    String mobile = base.getString("mobile");
                                    String address1 = base.getString("address1");
                                    String address2 = base.getString("address2");
                                    String country = base.getString("country");
                                    String state = base.getString("state");
                                    String city = base.getString("city");
                                    String zipcode = base.getString("zipcode");

                                    a1=city;
                                    a2=zipcode;
                                    a3=address1;
                                    a4=address2;
                                    a_country=country;
                                    a_state=state;
                                    a_email=email;
                                    a_mobile=mobile;

                                    if (!TextUtils.isEmpty(email)) {
                                        edt_email.setText(email);
                                    }

                                    if (!TextUtils.isEmpty(mobile)) {
                                        edt_mobile.setText(mobile);
                                    }

                                    if (!TextUtils.isEmpty(address1)) {
                                        autoCompView.setText(address1);
                                    }

                                    if (!TextUtils.isEmpty(address2)) {
                                        edt_address2.setText(address2);
                                    }

                                    if (!TextUtils.isEmpty(country)) {
                                        edt_country.setText(country);
                                    }

                                    if (!TextUtils.isEmpty(state)) {
                                        edt_state.setText(state);
                                    }

                                    if (!TextUtils.isEmpty(city)) {
                                        edt_city.setText(city);
                                    }

                                    if (!TextUtils.isEmpty(zipcode)) {
                                        edt_pincode.setText(zipcode);
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        });
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
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

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public class PlacesParseAsyn extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                URL url = new URL(placeURL);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } catch (MalformedURLException e) {
                // Log.e(LOG_TAG, "Error processing Places API URL", e);
            } catch (IOException e) {
                // Log.e(LOG_TAG, "Error connecting to Places API", e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            try {
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                latitudeA = (Double) jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat");
                longitudeA = (Double) jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            System.out.println("====latitude===" + latitudeA);
            System.out.println("====longitude===" + longitudeA);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent()) {
                Geocoder geocoder = new Geocoder((getContext()), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitudeA, longitudeA, 1);
                    Log.e("Addresses", "-->" + addresses);
                    if (addresses.size() > 0) {
                        Log.v("getLocality", "" + addresses.get(0).getLocality());
                        String placecity = addresses.get(0).getLocality();
                        String pincode = addresses.get(0).getPostalCode();
                        String country = addresses.get(0).getCountryName();
                        String state = addresses.get(0).getAdminArea();
                        String city = addresses.get(0).getSubAdminArea();
                        //Log.v("getAddressLine",""+addresses.get(0).getAddressLine(0));
                        //Log.v("getAddressLine",""+addresses.get(0).getAddressLine(1));
                        //Log.v("getCountryName",""+addresses.get(0).getCountryName());
                        //Log.v("getPostalCode",""+addresses.get(0).getPostalCode());
                        //Log.v("getLatitude",""+addresses.get(0).getLatitude());
                        //Log.v("getLongitude",""+addresses.get(0).getLongitude());


                        //edt_address2
                        if (!TextUtils.isEmpty(country)) {
                            edt_country.setText(country);
                        }

                        if (!TextUtils.isEmpty(state)) {
                            edt_state.setText(state);
                        }

                        if (!TextUtils.isEmpty(pincode)) {
                            edt_pincode.setText(pincode);
                        }

                        if (!TextUtils.isEmpty(city)) {
                            edt_city.setText(city);
                        }
                    }
                    // result = addresses.get(0).toString();
                } catch (IOException e) {
                    Log.i("IOException", "" + e);
                    e.printStackTrace();
                }
            }
        }
    }

}
