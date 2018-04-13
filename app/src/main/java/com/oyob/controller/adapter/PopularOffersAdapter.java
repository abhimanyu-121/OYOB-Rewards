package com.oyob.controller.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oyob.controller.R;


import com.oyob.controller.model.PopularOfferModal;
import com.oyob.controller.utils.Constant;
import com.oyob.controller.utils.Mylogger;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by 121 on 9/18/2017.
 */

public class PopularOffersAdapter extends RecyclerView.Adapter {

    List<PopularOfferModal> nameList;
    private Context context;


    public PopularOffersAdapter(Context context, List<PopularOfferModal> pageModels) {
        this.nameList = pageModels;
        this.context = context;
    }

    public void addData(List<PopularOfferModal> dataViews) {
        this.nameList.addAll(dataViews);
        notifyDataSetChanged();
    }

    public PopularOfferModal getItemAtPosition(int position) {
        return nameList.get(position);
    }

    public void addLoadingView() {
        //add loading item
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                nameList.add(null);
                notifyItemInserted(nameList.size() - 1);
            }
        });
    }

    public void removeLoadingView() {
        //Remove loading item
        nameList.remove(nameList.size() - 1);
        notifyItemRemoved(nameList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_offers_adapter, parent, false);
            return new PersonViewHolder(view);
        } else if (viewType == Constant.VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_loading, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PopularOfferModal mainPageModel = nameList.get(position);

        if (holder instanceof PersonViewHolder) {
            PersonViewHolder personViewHolder = (PersonViewHolder) holder;

            personViewHolder.productName.setText(mainPageModel.getName());
            String url = mainPageModel.getImg();
            Mylogger.getInstance().Logit("MainPageAdpter", url);
            Picasso.with(context).load(url).into(personViewHolder.img_cat_logo);
        } else if (holder instanceof LoadingHolder) {
            LoadingHolder loadingHolder = (LoadingHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return nameList == null ? 0 : nameList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return nameList.get(position) == null ? Constant.VIEW_TYPE_LOADING : Constant.VIEW_TYPE_ITEM;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        ImageView img_cat_logo;
        RelativeLayout layout;
        TextView textView_product;

        PersonViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.textView_product);
            img_cat_logo = itemView.findViewById(R.id.img_cat_logo);
            layout = itemView.findViewById(R.id.layout);
            textView_product = itemView.findViewById(R.id.textView_product);
        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }
}