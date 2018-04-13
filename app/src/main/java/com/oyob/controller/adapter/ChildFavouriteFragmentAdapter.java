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


import com.oyob.controller.R;

import com.oyob.controller.interfaces.OnRecyclerViewItemClick;
import com.oyob.controller.model.ChildFavouriteModel;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.squareup.picasso.Picasso;

import java.util.List;
import static com.oyob.controller.fragment.ConnectFragment.PRODUCTIMAGEURL;



/**
 * Created by 121 on 10/4/2017.
 */

public class ChildFavouriteFragmentAdapter extends RecyclerView.Adapter<ChildFavouriteFragmentAdapter.PersonViewHolder> {

    public List<ChildFavouriteModel> childConnectModels;
    public Context context;
    static final String TAG = ChildFavouriteFragmentAdapter.class.getSimpleName();
    static OnRecyclerViewItemClick onRecyclerViewItemClick;
    int selectedPosition = -1;
    String value;
    String value1;

    public ChildFavouriteFragmentAdapter(Context context, List<ChildFavouriteModel> persons) {
        this.childConnectModels = persons;
        this.context = context;

    }

    @Override
    public ChildFavouriteFragmentAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_favourite_adapter, parent, false);
        ChildFavouriteFragmentAdapter.PersonViewHolder pvh = new ChildFavouriteFragmentAdapter.PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ChildFavouriteFragmentAdapter.PersonViewHolder holder, final int position) {
        Log.d(TAG, "ChildConnectProductFragmentAdapter position = " + holder.getAdapterPosition());

        final ChildFavouriteModel childFavouriteModel = childConnectModels.get(position);
        holder.personName.setText(childConnectModels.get(position).name);
        holder.personAge.setText(childConnectModels.get(position).Discount);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = childFavouriteModel.getDetails();
        holder.webView.loadData(html, "text/html", "UTF-8");
        if(childFavouriteModel.getRedeemUrl()!= null)
        {
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(childConnectModels.get(position).getRedeemUrl()));
                    view.getContext().startActivity(intent);
                }
            });
        }

        if (selectedPosition == position) {

            value = SharedPreferenceHelper.getPref("apiResponse",context);
            value1 = SharedPreferenceHelper.getPref("apiResponseConnect",context);

            if (value.equals(String.valueOf(1)) || value.equals(String.valueOf(1))) {
                holder.favImageView.setImageResource(R.drawable.fav_color);
                Log.d(TAG, "onBindViewHolder: favSuccessString true state" + value);
            /*rea_iv_is_favorite.setColorFilter(context.getResources().getColor(R.color.colorAccent));*/
            } else if (value.equals(String.valueOf(0)) || value.equals(String.valueOf(0))) {
                Log.d(TAG, "onBindViewHolder:  false state" + value);
                holder.favImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            }

        }
        holder.favImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClick != null)
                    onRecyclerViewItemClick.favoriteItemclick(v, position);
                // selectedPosition = -1
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
      /*  if (childFavouriteModel.getFav_image().equalsIgnoreCase("true")) {
            holder.imageView1.setImageResource(R.drawable.fav_color);
        } else {
            holder.imageView1.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }*/

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
    public void setClickListener(OnRecyclerViewItemClick clickListener) {
        this.onRecyclerViewItemClick = clickListener;
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
            favImageView = (ImageView) itemView.findViewById(R.id.child_connect_imageView);
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
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);

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