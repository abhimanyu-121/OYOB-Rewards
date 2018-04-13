package com.oyob.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.oyob.controller.Analytics.FirebaseAnalyticsClass;
import com.oyob.controller.R;
import static com.oyob.controller.fragment.ConnectFragment.PRODUCTIMAGEURL;


import com.oyob.controller.interfaces.OnRecyclerViewItemClick;
import com.oyob.controller.model.ConnectModel;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by 121 on 9/19/2017.
 */

public class ChildConnectProductFragmentAdapter extends RecyclerView.Adapter<ChildConnectProductFragmentAdapter.PersonViewHolder> {


    public List<ConnectModel> childConnectModels;

    public Context context;
    static final String TAG = ChildConnectProductFragmentAdapter.class.getSimpleName();
    static OnRecyclerViewItemClick onRecyclerViewItemClick;
    String value;

    public ChildConnectProductFragmentAdapter(Context context, List<ConnectModel> persons) {
        this.childConnectModels = persons;
        this.context = context;

    }

    @Override
    public ChildConnectProductFragmentAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_connect_adapter, parent, false);
        ChildConnectProductFragmentAdapter.PersonViewHolder pvh = new ChildConnectProductFragmentAdapter.PersonViewHolder(v);
        return pvh;
    }

    public void setClickListener(OnRecyclerViewItemClick clickListener) {
        this.onRecyclerViewItemClick = clickListener;
    }

    @Override
    public void onBindViewHolder(ChildConnectProductFragmentAdapter.PersonViewHolder holder, final int position) {
        Log.d(TAG, "ChildConnectProductFragmentAdapter position = " + holder.getAdapterPosition());

        final ConnectModel childConnectModel = childConnectModels.get(position);

        holder.personName.setText(childConnectModels.get(position).name);
        holder.personAge.setText(childConnectModels.get(position).Discount);
        if (childConnectModel.getRedeemUrl() != null) {
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(childConnectModels.get(position).getRedeemUrl()));
                    view.getContext().startActivity(intent);
                    FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle("Redeem", "Click", childConnectModels.get(position).getRedeemUrl()));

                }
            });
        }

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = childConnectModel.getDetails();
        holder.webView.loadData(html, "text/html", "UTF-8");

         if (childConnectModel.getFav_bg().equals(String.valueOf(1))) {
                holder.favImageView.setImageResource(R.drawable.fav_color);
                Log.d(TAG, "onBindViewHolder: favSuccessString true state" + value);
            } else if (childConnectModel.getFav_bg().equals(String.valueOf(0))) {
                Log.d(TAG, "onBindViewHolder:  false state" + value);
                holder.favImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            }



        holder.favImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClick != null)
                    onRecyclerViewItemClick.favoriteItemclick(v, position);
                notifyDataSetChanged();
            }
        });


      /*  URLImageParser p = new URLImageParser(holder.Details, context);
        Spanned htmlSpan = Html.fromHtml(html, p, null);
        holder.Details.setText(htmlSpan);*/



     /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.Details.setText(Html.fromHtml(childConnectModel.getDetails(),Html.FROM_HTML_MODE_COMPACT));
        }
        else
        {
            holder.Details.setText(Html.fromHtml(childConnectModel.getDetails()));

        }*/

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.Text.setText(Html.fromHtml(childConnectModel.getText(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.Text.setText(Html.fromHtml(childConnectModel.getText()));

        }*/

        if (!PRODUCTIMAGEURL.isEmpty()) {
            Picasso.with(context).load(childConnectModels.get(position).getAndroid_image_url()).into(holder.imageView);
        } else {
            Glide.with(context).load(childConnectModels.get(position).getAndroid_image_url()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(holder.imageView);
        }
       /* if (!PRODUCTIMAGEURL.isEmpty()) {
            Picasso.with(context).load(childConnectModels.get(position).getDetails()).into(holder.imageView1);
        } else {
            Glide.with(context).load(childConnectModels.get(position).getDetails()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(holder.imageView1);
        }*/
    }

    @Override
    public int getItemCount() {
        if (childConnectModels != null) {
            return childConnectModels.size();
        }
        return 0;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView personName;
        TextView personAge;
        TextView Details;
        TextView Text;
        ImageView imageView;
        ImageView favImageView;
        WebView webView;
        Button button;

        PersonViewHolder(View itemView) {
            super(itemView);
            personName = (TextView) itemView.findViewById(R.id.rea_tv_merchant_name);
            personAge = (TextView) itemView.findViewById(R.id.rea_tv_get_now);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            favImageView = (ImageView) itemView.findViewById(R.id.child_connect_image);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            webView = (WebView) itemView.findViewById(R.id.web_image);
            webView.getSettings().setUseWideViewPort(true);
            webView.setVerticalScrollBarEnabled(true);
            webView.setHorizontalScrollBarEnabled(true);
            webView.setHorizontalScrollBarEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setDefaultFontSize(50);
            webView.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            button = (Button) itemView.findViewById(R.id.redeem);

          /*  Text = (TextView) itemView.findViewById(R.id.textView_text);
            Details = (TextView) itemView.findViewById(R.id.textView_details);
            imageView1 = (ImageView) itemView.findViewById(R.id.child_image);*/


        }

        @Override
        public void onClick(View view) {
            if (onRecyclerViewItemClick != null)
                onRecyclerViewItemClick.listItemClick(view, getAdapterPosition());
            if (onRecyclerViewItemClick != null)
                onRecyclerViewItemClick.favoriteItemclick(view, getAdapterPosition());
            if (onRecyclerViewItemClick != null)
                onRecyclerViewItemClick.productRedeemClick(view, getAdapterPosition());

        }
    }
}
