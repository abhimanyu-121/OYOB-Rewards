package com.oyob.controller.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.oyob.controller.R;


import com.oyob.controller.activity.ActivityDashboard;
import com.oyob.controller.activity.MyShopActivity;
import com.oyob.controller.activity.OfferDetailActivity;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.GPSTracker;
import com.oyob.controller.utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentBase implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private GoogleMap mMap;

    static final String TAG = MapsActivity.class.getSimpleName();
    private int REQ_PERMISSION=8787;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityDashboard.setTitleToolbar(getString(R.string.around_me));

        ImageView img_client_logo = view.findViewById(R.id.img_client_logo);
        img_client_logo.setVisibility(View.GONE);
        String client_logo = AppConstants.SERVER_ROOT + "get_client_banner.php?cid=" + sessionManager.getParticularField(SessionManager.CLIENT_ID);
        Picasso.with(_context).load(client_logo).into(img_client_logo);
        setHasOptionsMenu(true);
        return view;
    }

    TextView txtViewCount;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_shop, menu);
        super.onCreateOptionsMenu(menu, inflater);

        final View notificaitons = menu.findItem(R.id.action_myshop).getActionView();
        txtViewCount = notificaitons.findViewById(R.id.txtCount);
        final int count = Integer.parseInt(ActivityDashboard.cartCount);
        updateHotCount(count);
        txtViewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // updateHotCount(count);
            }
        });
        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    TODO
                if (count > 0) {
                    Fragment newFragment = new MyShopActivity();
                    android.support.v4.app.FragmentTransaction ft = ((AppCompatActivity) _context).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, newFragment, "").commit();
                } else {
                    Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateHotCount(final int count) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (count == 0)
                    txtViewCount.setVisibility(View.GONE);
                else {
                    txtViewCount.setVisibility(View.VISIBLE);
                    txtViewCount.setText(String.valueOf(count));
                    // supportInvalidateOptionsMenu();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            mMap.setOnInfoWindowClickListener(this);
            mMap.setMyLocationEnabled(true);
            get_products_by_loc();
        } else {
            // Show rationale and request permission.
            requestPermissions(

                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQ_PERMISSION
            );
        }




//        // Set a listener for marker click.
//
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//
//        /*// Enable / Disable my location button
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Enable / Disable Compass icon
//        mMap.getUiSettings().setCompassEnabled(true);
//
//        // Enable / Disable Rotate gesture
//        mMap.getUiSettings().setRotateGesturesEnabled(true);
//
//        // Enable / Disable zooming functionality
//        mMap.getUiSettings().setZoomGesturesEnabled(true);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_PERMISSION) {
            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                mMap.setOnInfoWindowClickListener(this);
                mMap.setMyLocationEnabled(true);
                get_products_by_loc();
            } else {
                // Permission was denied. Display an error message.
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("To find around you, you need to give location permission!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.dismiss();
                                requestPermissions(

                                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                                        REQ_PERMISSION
                                );

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
    }}

    private void drawMarkerWithCircle(LatLng position) {
        if (mMap != null) {
            double radiusInMeters = 1000.0;
            int strokeColor = 0xff4285F4; //red outline
            int shadeColor = 0x444285F4; //opaque red fill

            CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(4);
            mMap.addCircle(circleOptions);

            /*MarkerOptions markerOptions = new MarkerOptions().position(position);
            mMap.addMarker(markerOptions);*/
        }
    }


    GPSTracker gpsTracker;

    public void get_products_by_loc() {

        gpsTracker = new GPSTracker(_context);
        if (gpsTracker.canGetLocation() && gpsTracker.returnLocation() != null) {
            Location location = gpsTracker.returnLocation();
            LatLng latlng1 = new LatLng(location.getLatitude(), location.getLongitude());
//            LatLng latlng1 = new LatLng(-33.889581, 151.204020);
            drawMarkerWithCircle(latlng1);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng1, 13));

            String cid = sessionManager.getParticularField(SessionManager.CLIENT_ID);
            String country = sessionManager.getParticularField(SessionManager.COUNTRY);

            String countURL = AppConstants.SERVER_ROOT + "get_products_by_loc.php?lat=" + location.getLatitude() + "&lng=" + location.getLongitude() + "&cid=" + cid + "&b=0.100000&c=" + country + "&response_type=json";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            Log.i("TEST","arrr "+countURL);

            StringRequest postReq = new StringRequest(Request.Method.GET, countURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("error_msg")) {
                                String error_msg = jsonObject.getString("error_msg");
                                showToastLong(_context, getString(R.string.not_products_available));
                                //showAlert(error_msg);
                            }
                            if (jsonObject.has("product")) {
                                JSONArray product = jsonObject.getJSONArray("product");
                                for (int i = 0; i < product.length(); i++) {
                                    JSONObject jsonObject1 = product.getJSONObject(i);
                                    String id = jsonObject1.getString("id");
                                    String name = jsonObject1.getString("name");
                                    String latitude = jsonObject1.getString("latitude");
                                    String longitude = jsonObject1.getString("longitude");
                                    String pin_type = jsonObject1.getString("pin_type");

                                    MarkerOptions markerOptions;
                                    if (pin_type.equalsIgnoreCase("0")) {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.default1));
                                    } else if (pin_type.equalsIgnoreCase("1")) {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.aautomative1));
                                    } else if (pin_type.equalsIgnoreCase("2")) {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.dining1));
                                    } else if (pin_type.equalsIgnoreCase("3")) {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.healthandbauty2));
                                    } else if (pin_type.equalsIgnoreCase("4")) {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.home1));
                                    } else if (pin_type.equalsIgnoreCase("5")) {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.shop1));
                                    } else if (pin_type.equalsIgnoreCase("6")) {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.travel1));
                                    } else if (pin_type.equalsIgnoreCase("7")) {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.entertain1));
                                    } else if (pin_type.equalsIgnoreCase("8")) {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.default1));
                                    } else if (pin_type.equalsIgnoreCase("9")) {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.golf1));
                                    } else {
                                        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.default1));
                                    }

                                    Double latitudeD = Double.parseDouble(latitude);
                                    Double longitudeD = Double.parseDouble(longitude);
                                    if (latitudeD != null && longitudeD != null) {
                                        LatLng latlng = new LatLng(latitudeD, longitudeD);
                                        markerOptions.position(latlng).title(name);
                                        mMap.addMarker(markerOptions).setTag(id);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }

            });

            requestQueue.add(postReq);

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }

    }

    private void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                getActivity()).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String id = (String) marker.getTag();
        System.out.println("id " + id);
        if (!TextUtils.isEmpty(id)) {
            Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
            intent.putExtra("type", "banner");
            intent.putExtra("ProductID", id);
            getActivity().startActivity(intent);
        }

    }
}