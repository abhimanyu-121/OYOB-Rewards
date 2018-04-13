package com.oyob.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.oyob.controller.R;

import com.oyob.controller.interfaces.OnRecyclerViewItemClick;
import com.oyob.controller.model.ConnectModel;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.squareup.picasso.Picasso;

import java.util.List;
import static com.oyob.controller.fragment.ConnectFragment.PRODUCTIMAGEURL;


/**
 * Created by 121 on 9/15/2017.
 */

public class ConnectRecyclerAdapter extends RecyclerView.Adapter<ConnectRecyclerAdapter.ConnectViewHolder> {
    List<ConnectModel> persons;
    private Context context;
    static final String TAG = "ConnectRecyclerAdapter";
    public OnRecyclerViewItemClick onRecyclerViewItemClick;
    String value;

    public ConnectRecyclerAdapter(Context a, List<ConnectModel> persons) {
        this.persons = persons;
        this.context = a;
        value = SharedPreferenceHelper.getPref("fav",context);

    }

    @Override
    public ConnectRecyclerAdapter.ConnectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.connect_adapter, parent, false);
        ConnectViewHolder pvh = new ConnectViewHolder(v);
        return pvh;
    }
    public void setClickListener(OnRecyclerViewItemClick clickListener) {
        this.onRecyclerViewItemClick = clickListener;
    }
    @Override
    public void onBindViewHolder(final ConnectRecyclerAdapter.ConnectViewHolder holder, final int position) {

      final ConnectModel  modelClass = persons.get(position);
        holder.personName.setText(persons.get(position).name);
        holder.personAge.setText(persons.get(position).Discount);

        if (modelClass.getFav_bg().equals(String.valueOf(1))) {
            Log.d(TAG, "onBindViewHolder: "+modelClass.getFav_bg());
            holder.favView.setImageResource(R.drawable.fav_color);
            SharedPreferenceHelper.set(context,"fav",modelClass.getFav_bg());

        }
        else if (modelClass.getFav_bg().equals(String.valueOf(0))) {
            Log.d(TAG, "onBindViewHolder:  false state" + modelClass.getFav_bg());
            holder.favView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            SharedPreferenceHelper.set(context,"fav",modelClass.getFav_bg());
        }

        holder.favView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClick != null)
                    onRecyclerViewItemClick.favoriteItemclick(v, position);
                   notifyDataSetChanged();
            }
        });
        //add other click listeners here
        if (!PRODUCTIMAGEURL.isEmpty()) {
            Picasso.with(context).load(modelClass.getAndroid_image_url()).into(holder.imageView);
        } else {
            Glide.with(context).load(modelClass.getAndroid_image_url()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (persons != null) {
            return persons.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ConnectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView personName;
        TextView personAge;
        ImageView imageView;
        ImageView favView;

        ConnectViewHolder(View itemView) {
            super(itemView);
            personName =  itemView.findViewById(R.id.rea_tv_merchant_name);
            personAge =  itemView.findViewById(R.id.rea_tv_get_now);
            imageView =  itemView.findViewById(R.id.image);
            favView =  itemView.findViewById(R.id.fav_icon_connect);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if (onRecyclerViewItemClick != null)
                onRecyclerViewItemClick.listItemClick(view, getAdapterPosition());
            if (onRecyclerViewItemClick != null)
                onRecyclerViewItemClick.favoriteItemclick(view, getAdapterPosition());

        }

    }
}
