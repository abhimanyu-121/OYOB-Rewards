package com.oyob.controller.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oyob.controller.R;


import com.oyob.controller.model.MainPageModel;
import com.oyob.controller.utils.Constant;
import com.oyob.controller.utils.Mylogger;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by 121 on 9/18/2017.
 */

public class MainPageAdapter extends RecyclerView.Adapter {

    List<MainPageModel> nameList;
    private Context context;


    public void addData(List<MainPageModel> dataViews) {
        this.nameList.addAll(dataViews);
        notifyDataSetChanged();
    }

    public MainPageModel getItemAtPosition(int position) {
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

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        ImageView img_cat_logo;

        PersonViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.textView_product);
            img_cat_logo = (ImageView) itemView.findViewById(R.id.img_cat_logo);
        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }

    public MainPageAdapter(Context context, List<MainPageModel> pageModels) {
        this.nameList = pageModels;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_adapter, parent, false);
            return new PersonViewHolder(view);
        } else if (viewType == Constant.VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_loading, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MainPageModel mainPageModel = nameList.get(position);

        if (holder instanceof PersonViewHolder) {
            PersonViewHolder personViewHolder = (PersonViewHolder) holder;
            personViewHolder.productName.setText(mainPageModel.getName());
            String url = Constant.CATEGORY_LOGO_PREFIXURL + mainPageModel.getId() + "." + mainPageModel.getExt();
            Mylogger.getInstance().Logit("MainPageAdpter", url);
            if (!TextUtils.isEmpty(mainPageModel.getNew_cat_image())) {
                Picasso.with(context).load(mainPageModel.getNew_cat_image()).into(personViewHolder.img_cat_logo);
            }

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
}