package com.oyob.controller.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import static com.oyob.controller.activity.ActivityDashboard.linearLayout;
import android.widget.TextView;

import com.oyob.controller.R;
import com.oyob.controller.fragment.ChildExclusiveFragment;
import com.oyob.controller.fragment.ExclusiveFragment;
import com.oyob.controller.interfaces.OnRecyclerViewItemClick;
import com.oyob.controller.model.ModelClass;
import com.oyob.controller.networkCall.ProcessAsynctack;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.SessionManager;

import java.util.List;


/**
 * Created by 121 on 10/5/2017.
 */

public class PagerActivity extends ActivityBase implements OnRecyclerViewItemClick {


    String aboutBundle = null;
    String Url = null;
    List<ModelClass> modelClassList;
    List<ModelClass> modelClass;
    String cid = "";
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ImageView imageView;
    ListView listView;
    private boolean isSuccessApiCall = false;
    ProcessAsynctack process;
    String concatString = "";
    public static String PRODUCTIMAGEURL = "";
    public static String MERCHANTIMAGEURL = "";
    boolean isPressed;

    public static String favValue;
    ModelClass childExclusiveModel;
    boolean aBoolean;
    boolean changeaBoolean;
    ModelClass atPosition;
    String string = "";
    String favstring = "";


    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String imageId = "";
    String image = "";
    String uname = "";
    String merchantImageId = "";
    Bundle bundle;
    String id = "";
    static final String TAG = ExclusiveFragment.class.getSimpleName();
    private static final String SET_FAV = AppConstants.SERVER_ROOT+"set_favourites.php";
    String userId;
    String ProductUrl;
    String apiClientId;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle1 = getIntent().getExtras();
        childExclusiveModel = new ModelClass();
        aboutBundle = sessionManager.getParticularField(SessionManager.CLIENT_ID);
        userId= SharedPreferenceHelper.getPref("userId",getApplicationContext());
        apiClientId= SharedPreferenceHelper.getPref("client_id", getApplicationContext());

        Intent intent = getIntent();
        id = intent.getStringExtra("tagExclusive");
        final Bundle bundle = new Bundle();
        bundle.putString("userId",userId);
        bundle.putString("client_id",apiClientId);
        bundle.putString("id",id);

        if (savedInstanceState == null) {
            fragment = new ChildExclusiveFragment();
            fragment.setArguments(bundle);
            linearLayout.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, fragment).commit();
        }
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        backArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.exclusive_toolbar);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
    }


    /*public void SetFavApiCall(final String userId, final String productId, final boolean flag) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setMessage("Favourite item...");
        pDialog.show();

        StringRequest postReq = new StringRequest(Request.Method.POST, SET_FAV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject.getJSONObject("status");
                        Log.d(TAG, "onResponse:setFavorite " + jsonObject);
                        JSONObject obj4 = null;
                        //Getting all the keys inside json object with key- pages
                        Iterator<String> keys = jsonObject.keys();
                        while (keys.hasNext()) {
                            String keyValue = (String) keys.next();
                            obj4 = jsonObject.getJSONObject(keyValue);
                            ConnectModel connectModel = new ConnectModel();
                            //getting string values with keys- pageid and title
                            String status = obj4.getString("200");
                            if (status.equals("success")) {
                                childExclusiveModel.setFavPress(childExclusiveModel.getFavPress());
                            }
                            Log.d(TAG, "onResponse: ");
                            Log.d("response", response.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", userId);
                params.put("pid", productId);
                params.put("flag", String.valueOf(flag));
                Log.d(TAG, "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        requestQueue.add(postReq);
    }
*/
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ActivityDashboard.class);
        intent.putExtra("client_id", aboutBundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void listItemClick(View view, int position) {

    }


    @Override
    public void productRedeemClick(View view, int position) {

    }

    @Override
    public void favoriteItemclick(View view, int position) {
  /*      childExclusiveModel = (ModelClass) modelClass.get(position);
        Log.d(TAG, "favoriteItemclick: " + position);
        FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle(uname, "Favourite", "ConnectOffersScreen"));

        if (childExclusiveModel.getFavPress().equals(String.valueOf(0))) {
            SetFavApiCall(String.valueOf(uname), String.valueOf(id), stringToBool(String.valueOf(1)));
            childExclusiveModel.setFavPress(String.valueOf(1));

            Log.d(TAG, "favoriteItemclick: " + String.valueOf(1));
        } else if (childExclusiveModel.getFavPress().equals(String.valueOf(1))) {
            SetFavApiCall(String.valueOf(uname), String.valueOf(id), stringToBool(String.valueOf(0)));
            childExclusiveModel.setFavPress(String.valueOf(0));
            Log.d(TAG, "favoriteItemclick: " + String.valueOf(0));
        }
    */}


}
