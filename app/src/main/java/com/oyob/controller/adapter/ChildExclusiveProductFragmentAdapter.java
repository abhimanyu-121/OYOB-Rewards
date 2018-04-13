package com.oyob.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.oyob.controller.R;
import com.oyob.controller.interfaces.OnRecyclerViewItemClick;
import com.oyob.controller.model.ModelClass;


import java.util.List;


/**
 * Created by 121 on 9/19/2017.
 */

public class ChildExclusiveProductFragmentAdapter extends RecyclerView.Adapter<ChildExclusiveProductFragmentAdapter.PersonViewHolder> {

    public static final String TAG = ChildExclusiveProductFragmentAdapter.class.getSimpleName();

    List<ModelClass> childExclusiveModels;
    private Context context;
    static OnRecyclerViewItemClick onRecyclerViewItemClick;
    String value;

    public ChildExclusiveProductFragmentAdapter(Context context, List<ModelClass> persons) {
        this.childExclusiveModels = persons;
        this.context = context;

    }

    @Override
    public ChildExclusiveProductFragmentAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_exclusive_adapter, parent, false);
        ChildExclusiveProductFragmentAdapter.PersonViewHolder pvh = new ChildExclusiveProductFragmentAdapter.PersonViewHolder(v);
        return pvh;
    }

    public void setClickListener(OnRecyclerViewItemClick clickListener) {
        this.onRecyclerViewItemClick = clickListener;
    }

    @Override
    public void onBindViewHolder(final ChildExclusiveProductFragmentAdapter.PersonViewHolder holder, final int position) {

       /* final ModelClass childExclusiveModel = childExclusiveModels.get(position);
        holder.rea_tv_merchant_name.setText(childExclusiveModels.get(position).name);
        holder.personAge.setText(childExclusiveModels.get(position).Discount);
        if (!childExclusiveModel.getAndroid_image_url().isEmpty()) {
            holder.button.setVisibility(View.VISIBLE);
           holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(childExclusiveModels.get(position).getAndroid_image_url()));
                    view.getContext().startActivity(intent);
                    FirebaseAnalyticsClass.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, FirebaseAnalyticsClass.getBundle("Redeem", "Click", childExclusiveModels.get(position).getAndroid_image_url()));

                }
            });
        }

        if (childExclusiveModel.getFavPress().equals(String.valueOf(1))) {
            holder.favImageView.setImageResource(R.drawable.fav_color);
            Log.d(TAG, "onBindViewHolder: favSuccessString true state" + value);
            *//*rea_iv_is_favorite.setColorFilter(context.getResources().getColor(R.color.colorAccent));*//*
        } else if (childExclusiveModel.getFavPress().equals(String.valueOf(0))) {
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

        *//*holder.Text.setText(childExclusiveModels.get(position).text);*//*

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = childExclusiveModel.getDetails();
        holder.webView.loadData(html, "text/html", "UTF-8");

*/


      /*  URLImageParser p = new URLImageParser(holder.Details, context);
        Spanned htmlSpan = Html.fromHtml(html, p, null);
        holder.Details.setText(htmlSpan);*/

       /* holder.Details.loadDataWithBaseURL("", html, mimeType, encoding, "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.Text.setText(Html.fromHtml(childExclusiveModel.getText(),Html.FROM_HTML_MODE_COMPACT));
        }
        else
        {
            holder.Text.setText(Html.fromHtml(childExclusiveModel.getText()));
        }
*/
    /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            holder.Text.setText(Html.fromHtml(childExclusiveModel.getText(),Html.FROM_HTML_MODE_COMPACT));
        }
        else
        {
            holder.Text.setText(Html.fromHtml(childExclusiveModel.getText()));

        }*/
       /* if (!PRODUCTIMAGEURL.isEmpty()) {
            Picasso.with(context).load(childExclusiveModels.get(position).getAndroid_image_url()).into(holder.imageView);
        } else {
            Glide.with(context).load(childExclusiveModels.get(position).getAndroid_image_url()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(holder.imageView);
        }*/
        /*if(!PRODUCTIMAGEURL.isEmpty())
        {
            Picasso.with(context).load(childExclusiveModel.getChild_image_url()).into(holder.childimageView);
        }
        else {
            Glide.with(context).load(childExclusiveModel.getChild_image_url()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(holder.childimageView);
        }*/

    }

    @Override
    public int getItemCount() {
        if (childExclusiveModels != null) {
            return childExclusiveModels.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            imageView = (ImageView) itemView.findViewById(R.id.image);
            favImageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);

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
