package com.oyob.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.oyob.controller.R;
import com.oyob.controller.model.ModelPopularOffers;
import com.oyob.controller.utils.AppConstants;


import java.util.List;

/**
 * Created by Narender Kumar on 2/23/2018.
 * Prominent Developer
 * narender.kumar.nishad@gmail.com
 */

public class AdapterPopularOffers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private RecyclerViewClickListener itemListener;
    private List<ModelPopularOffers> offers;

    public AdapterPopularOffers(Context context, List<ModelPopularOffers> offers, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.offers = offers;
        this.itemListener = itemListener;
    }

    public interface RecyclerViewClickListener {
        void onItemClickListener(View view, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ItemListHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_popular_offers, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vho, int position) {
        ModelPopularOffers singlePopularOffers = offers.get(position);
        ItemListHolder itemListHolder = (ItemListHolder) vho;
        Glide.with(context)
                .load(AppConstants.POPULAR_OFFERS_IMAGE_PREFIX + singlePopularOffers.getImage())
                .into(itemListHolder.rpo_iv_offer_image);
    }

    @Override
    public int getItemCount() {
        return ((null != offers && offers.size() > 0) ? offers.size() : 0);
    }

    private class ItemListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView rpo_iv_offer_image;

        private ItemListHolder(View view) {
            super(view);
            this.rpo_iv_offer_image = view.findViewById(R.id.rpo_tiv_offer_image);
            this.rpo_iv_offer_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.onItemClickListener(view, getLayoutPosition());
        }
    }
}