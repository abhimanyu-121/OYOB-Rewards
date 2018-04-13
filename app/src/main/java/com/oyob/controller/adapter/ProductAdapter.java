package com.oyob.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oyob.controller.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 121 on 9/14/2017.
 */

public class ProductAdapter extends BaseAdapter
{
    JSONArray jsonArray;
    Context context;

    public ProductAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        String uniquename = "";
        String imageString = "";
        String placeName = "";
        String ratingString = "";
        String typeString = "";
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.product_adapter, null);
        }

        TextView textView1 = (TextView) v.findViewById(R.id.textView);
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(i);
            uniquename = jsonObject.getString("name");
            textView1.setText(uniquename);


        } catch (JSONException e) {
            e.printStackTrace();
        }



        return v;
    }
}