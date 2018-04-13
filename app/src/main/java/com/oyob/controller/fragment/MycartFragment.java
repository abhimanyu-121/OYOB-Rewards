package com.oyob.controller.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oyob.controller.R;
import com.oyob.controller.activity.ActivityDashboard;
import com.oyob.controller.activity.MyShopActivity;
import com.oyob.controller.adapter.MyShopeAdapter;
import com.oyob.controller.helper.RecyclerViewClickListener;
import com.oyob.controller.helper.RecyclerViewTouchListener;
import com.oyob.controller.model.MyShopModel;
import com.oyob.controller.utils.AppConstants;
import com.oyob.controller.utils.Mylogger;
import com.oyob.controller.utils.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by 121 on 9/18/2017.
 */
public class MycartFragment extends FragmentBase {

    public static ArrayList<MyShopModel> shopModels = new ArrayList<>();
    public static String tt;
    public static String pp;
    MyShopeAdapter myShopeAdapter;
    String userId;
    String client_id;
    int cart_qty_count = 0;
    RadioGroup radio_group_shipping;
    RadioButton radio_regular, radio_registered;
    private String TAG = "MainPageFragment";
    private RecyclerView rv;
    private FragmentTransaction fragmentTransaction;
    private double merchant_fee;
    private ProgressDialog pDialog;
    private TextView txt_total;
    private String url;
    private double totalCost = 0;
    private double shippingCostA = 0.0;
    private TextView merchant_text, merchant_set;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(_context).inflate(R.layout.my_cart_fragment, container, false);
        shopModels.clear();
        rv = rootView.findViewById(R.id.card_recycler_view);
        txt_total = rootView.findViewById(R.id.txt_total);
        merchant_text = rootView.findViewById(R.id.merchant_fee_text);
        merchant_set = rootView.findViewById(R.id.merchant_fee_set);
        LinearLayoutManager llm = new LinearLayoutManager(_context);
        rv.setLayoutManager(llm);
        pDialog = new ProgressDialog(_context);
        pDialog.setMessage("Updating cart");
        myShopeAdapter = new MyShopeAdapter(_context, shopModels);
        rv.setAdapter(myShopeAdapter);
        userId = sessionManager.getParticularField(SessionManager.USER_ID);
        url = AppConstants.SERVER_ROOT + "show_cart.php?uid=" + userId;
        Log.i("TEST","url  "+url);
        merchant_fee = Double.valueOf(sessionManager.getParticularField(SessionManager.MERCHANT_FEE));
        //merchant_fee=1.0;
        if (merchant_fee > Double.valueOf("0.0")) {
            merchant_text.setVisibility(View.VISIBLE);
            merchant_set.setVisibility(View.VISIBLE);
            merchant_set.setText("$"+merchant_fee);
        }
        Mylogger.getInstance().Logit("myshop", url);
        rv.addOnItemTouchListener(new RecyclerViewTouchListener(_context, rv, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {

                ImageView btnPlus = view.findViewById(R.id.btn_plus);
                ImageView btnMinus = view.findViewById(R.id.btn_minus);
                ImageView btn_remove = view.findViewById(R.id.btn_remove);
                btnPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shopModels.get(position).setQty(shopModels.get(position).getQty() + 1);
                        myShopeAdapter.notifyItemChanged(position);
                        UpdatCart(shopModels.get(position).getCart_id(), shopModels.get(position).getCart_item_id(), shopModels.get(position).getMax_group_ship_cost(), shopModels.get(position).getPid(), shopModels.get(position).getQty(), shopModels.get(position).getShippingtype(), Integer.parseInt(userId));
                    }
                });

                btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (shopModels.get(position).getQty() > 1) {
                            shopModels.get(position).setQty(shopModels.get(position).getQty() - 1);
                            myShopeAdapter.notifyItemChanged(position);
                            UpdatCart(shopModels.get(position).getCart_id(), shopModels.get(position).getCart_item_id(), shopModels.get(position).getMax_group_ship_cost(), shopModels.get(position).getPid(), shopModels.get(position).getQty(), shopModels.get(position).getShippingtype(), Integer.parseInt(userId));
                        }
                    }
                });
                btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeFRomCart(shopModels.get(position).getCart_id(), shopModels.get(position).getPid(), shopModels.get(position));
                    }
                });
            }


            @Override
            public void onLongClick(View view, final int position) {

            }
        }
        ));

        radio_group_shipping = rootView.findViewById(R.id.radio_group_shipping);
        radio_regular = rootView.findViewById(R.id.radio_regular);
        radio_registered = rootView.findViewById(R.id.radio_registered);

        radio_group_shipping.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radio_regular) {
                    shippingCostA = 0;
                    pp = String.valueOf(shippingCostA);
                } else if (checkedId == R.id.radio_registered) {
                    shippingCostA = 5.50;
                    pp = String.valueOf(shippingCostA);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyShopActivity.totalMainCost = totalCost + shippingCostA;
                        //double temp = Double.valueOf(txt_total.getText().toString().trim())+shippingCostA;
                        txt_total.setText("$" + MyShopActivity.totalMainCost);

                            if(merchant_fee>0.0)
                            {
                                double rr = totalCost + shippingCostA;
                                double temp=(rr*merchant_fee)/100;
                                double temp1=rr+temp;
                                //txt_total.setText("$" + (temp1));
                                tt = String.valueOf(temp1);


                            }
                            else
                            {
                                tt = txt_total.getText().toString().trim().substring(1);
                            }

                    }
                });
            }
        });

        Button btnNext = rootView.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopModels.size() > 0) {
                    MyShopActivity.ClickDelivery();
                } else {
                    Toast.makeText(_context, "Cart is empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        getCartDetail(url);
        return rootView;
    }

    public void getCartDetail(String productUrl) {
        RequestQueue queue = Volley.newRequestQueue(_context);
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, productUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("TEST", " cart response " + response);

                        Mylogger.getInstance().Logit("myshop", response);
                        pDialog.hide();
                        try {
                            JSONObject main = new JSONObject(response);
                            JSONObject status = main.getJSONObject("status");
                            if (status.getString("200").equalsIgnoreCase("success")) {

                                JSONArray cart_List = main.getJSONArray("cart_List");
                                for (int i = 0; i < cart_List.length(); i++) {
                                    JSONObject base = cart_List.getJSONObject(i);
                                    JSONObject CartItem = base.getJSONObject("CartItem");
                                    JSONObject cart = base.getJSONObject("Cart");
                                    JSONObject product = base.getJSONObject("Product");
                                    JSONObject cartProduct = base.getJSONObject("CartProduct");

                                    int cart_item_id;
                                    String categoryName;
                                    String itemName;
                                    String price;
                                    int qty;
                                    int pid;
                                    int cart_id;
                                    int max_group_ship_cost;
                                    int product_shipping_type;
                                    String cart_product_id;
                                    String discount;
                                    String ware_product;

                                    pid = product.getInt("id");
                                    price = cartProduct.getString("product_price");
                                    cart_product_id = cartProduct.getString("id");
                                    cart_id = cart.getInt("id");

                                    cart_item_id = CartItem.getInt("id");
                                    categoryName = CartItem.getString("name");
                                    discount = product.getString("highlight");
                                    itemName = CartItem.getString("name");
                                    product_shipping_type = cartProduct.getInt("product_shipping_type");
                                    ware_product=cartProduct.getString("product_quantity");
                                    cart_qty_count = CartItem.getInt("product_qty");
                                    //txt_total.setText("$" +(Float.valueOf(price)*cart_qty_count)+shippingCostA);
                                    String temp = String.valueOf((Float.valueOf(price) * cart_qty_count) + shippingCostA);
                                    max_group_ship_cost = 0;

                                    if (!TextUtils.isEmpty(price))
                                        totalCost = totalCost + Double.parseDouble(temp);
                                    shopModels.add(new MyShopModel(cart_item_id, categoryName, itemName, price, cart_id, product_shipping_type, max_group_ship_cost, pid, cart_qty_count, discount, cart_product_id,ware_product));
                                }
                                ActivityDashboard.cartCount = String.valueOf(cart_qty_count);
                                sessionManager.updateCartCount(cart_qty_count);

                                    txt_total.setText("$" + (totalCost));

                                if(merchant_fee>0.0)
                                {
                                    double temp=(totalCost*merchant_fee)/100;
                                    double temp1=totalCost+temp;
                                    //txt_total.setText("$" + (temp1));
                                    tt = String.valueOf(temp1);


                                }
                                else
                                {
                                    tt = txt_total.getText().toString().trim().substring(1);
                                }


                            } else {
                                showAlert("cart is empty");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myShopeAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                pDialog.hide();
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                _context).create();
        alertDialog.setTitle("AtWork");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /*shopModels.clear();
                MyShopActivity.totalMainCost=0.0;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(MycartFragment.this).attach(MycartFragment.this).commit();*/

                dialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public void removeFRomCart(final int cart_id, final int pid, final MyShopModel myShopModel) {
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(_context);
        //https://www.myrewards.com.au/newapp/remove_cart_product.php?uid=6799719&client_id=1963&cart_id=24472&pid=1040967
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.DELETE, AppConstants.SERVER_ROOT + "remove_cart_product.php?uid=" + userId + "&client_id=" + sessionManager.getParticularField(SessionManager.CLIENT_ID) + "&cart_id=" + cart_id + "&pid=" + pid, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("CARTUPDATE", response.toString());
                        shopModels.remove(myShopModel);
                        myShopeAdapter.notifyDataSetChanged();

                        try {
                            JSONObject obj4 = null;
                            Iterator<String> keys = response.keys();
                            while (keys.hasNext()) {
                                String keyValue = (String) keys.next();
                                obj4 = response.getJSONObject(keyValue);

                                //getting string values with keys- pageid and title
                                String status = obj4.getString("200");
                                if (status.equals("success")) {
                                    //showAlert("success");
                                    shopModels.clear();
                                    MyShopActivity.totalMainCost = 0.0;
                                    totalCost = 0;
                                    getCartDetail(url);
                                    //txt_total.setText("$" + (response.getString("total")));


                                } else {
                                    showAlert(response.toString());
                                }
                            }
                        } catch (Exception iox) {
                            System.out.println("iox: " + iox);
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("CARTUPDATE", "");


                    }
                });

        jsObjRequest.setShouldCache(false);
        requestQueue.add(jsObjRequest);
    }

    public void UpdatCart(final int cart_id, final int cart_item_id, final int max_group_ship_cost, final int pid, final int qty, final int shippingtype, final int uid) {
        Mylogger.getInstance().Logit("Updatecart", "updatecart");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(_context);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cart_id", cart_id);
            jsonObject.put("cart_item_id", cart_item_id);
            jsonObject.put("max_group_ship_cost", max_group_ship_cost);
            jsonObject.put("pid", pid);
            jsonObject.put("qty", qty);
            jsonObject.put("shippingtype", shippingtype);
            jsonObject.put("uid", uid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Mylogger.getInstance().Logit("Updatecart", jsonObject.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppConstants.SERVER_ROOT + "update_cart.php", jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        try {
                            JSONObject obj4 = null;
                            Iterator<String> keys = response.keys();
                            while (keys.hasNext()) {
                                String keyValue = keys.next();
                                obj4 = response.getJSONObject(keyValue);

                                //getting string values with keys- pageid and title
                                String status = obj4.getString("200");
                                if (status.equals("success")) {
                                    //showAlert("success");
                                    shopModels.clear();
                                    MyShopActivity.totalMainCost = 0.0;
                                    totalCost = 0;
                                    getCartDetail(url);
                                    //txt_total.setText("$" + (response.getString("total")));


                                } else {
                                    showAlert(response.toString());
                                }
                            }
                        } catch (Exception iox) {
                            System.out.println("iox: " + iox);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("CARTUPDATE", "");
                    }
                });

        jsObjRequest.setShouldCache(false);
        requestQueue.add(jsObjRequest);

    }
}
