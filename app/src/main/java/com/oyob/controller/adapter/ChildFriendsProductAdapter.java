package com.oyob.controller.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.oyob.controller.R;


import com.oyob.controller.fragment.ChildFriendsFragment;
import com.oyob.controller.model.ChildFriendsModel;
import com.oyob.controller.utils.URLImageParser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 121 on 9/24/2017.
 */

public class ChildFriendsProductAdapter   extends RecyclerView.Adapter<ChildExclusiveProductFragmentAdapter.PersonViewHolder> {


    List<ChildFriendsModel> childFriendsModels;

    private Context context;

    public ChildFriendsProductAdapter(Context context, List<ChildFriendsModel> persons) {
        this.childFriendsModels = persons;
        this.context = context;

    }

    @Override
    public ChildExclusiveProductFragmentAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_exclusive_adapter, parent, false);
        ChildExclusiveProductFragmentAdapter.PersonViewHolder pvh = new ChildExclusiveProductFragmentAdapter.PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ChildExclusiveProductFragmentAdapter.PersonViewHolder holder, int position) {

        ChildFriendsModel friendsModel = childFriendsModels.get(position);
        holder.personName.setText(childFriendsModels.get(position).name);
        holder.Text.setText(childFriendsModels.get(position).text);
        holder.personAge.setText(childFriendsModels.get(position).Discount);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = friendsModel.getDetails();


        URLImageParser p = new URLImageParser(holder.Details, context);
        Spanned htmlSpan = Html.fromHtml(html, p, null);
        holder.Details.setText(htmlSpan);

       /* holder.Details.loadDataWithBaseURL("", html, mimeType, encoding, "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.Text.setText(Html.fromHtml(childExclusiveModel.getText(),Html.FROM_HTML_MODE_COMPACT));
        }
        else
        {
            holder.Text.setText(Html.fromHtml(childExclusiveModel.getText()));
        }
*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.Text.setText(Html.fromHtml(friendsModel.getText(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.Text.setText(Html.fromHtml(friendsModel.getText()));

        }

        if (!ChildFriendsFragment.MERCHANTIMAGEURL.isEmpty()) {
            Picasso.with(context).load(childFriendsModels.get(position).getAndroid_image_url()).into(holder.imageView);
        } else {
            Glide.with(context).load(childFriendsModels.get(position).getAndroid_image_url()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(holder.imageView);
        }
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
        return childFriendsModels.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        TextView personAge;
        TextView Details;
        TextView Text;
        ImageView imageView;
        ImageView childimageView;

        PersonViewHolder(View itemView) {
            super(itemView);
            personName = (TextView) itemView.findViewById(R.id.rea_tv_merchant_name);
            personAge = (TextView) itemView.findViewById(R.id.rea_tv_get_now);
            Text = (TextView) itemView.findViewById(R.id.textView_text);
            Details.setHorizontalScrollBarEnabled(true);
            Details.setVerticalScrollBarEnabled(true);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}