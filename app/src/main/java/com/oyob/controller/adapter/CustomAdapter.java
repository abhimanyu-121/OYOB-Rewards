package com.oyob.controller.adapter;

/**
 * Created by rama on 7/19/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oyob.controller.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;




/**
 * Created by rama on 11/15/2016.
 */

public class CustomAdapter extends BaseAdapter {
    JSONObject jsonObject;
    Context context;

    public CustomAdapter(Context context, JSONObject jsonObject) {
        this.context = context;
        this.jsonObject = jsonObject;
    }

    @Override
    public int getCount() {
        return jsonObject.length();
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
        String name = "";
        String highlight = "";
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.row_exclusive_adapter, null);
        }

        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

        TextView textView2 = (TextView) v.findViewById(R.id.rea_tv_merchant_name);
        TextView textView3 = (TextView) v.findViewById(R.id.rea_tv_get_now);
        JSONObject preferencesJSON = null;
        try {
            preferencesJSON = jsonObject.getJSONObject("cat");
            for(int j= 0;j<preferencesJSON.length();j++)
            {
            Iterator keys = preferencesJSON.keys();
            while(keys.hasNext()) {
                String dynamicKey = (String)keys.next();
                JSONObject line = preferencesJSON.getJSONObject(dynamicKey);
                for(int k=0;k<line.length();k++)
                {
                    name = line.getString("name");
                    highlight = line.getString("highlight");
                }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        textView2.setText(name);
        textView3.setText(highlight);
        return v;
    }
}