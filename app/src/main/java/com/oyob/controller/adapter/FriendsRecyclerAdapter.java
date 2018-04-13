package com.oyob.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.oyob.controller.R;

import com.oyob.controller.model.FriendsModel;
import com.oyob.controller.sharePreferenceHelper.SharedPreferenceHelper;
import com.squareup.picasso.Picasso;

import java.util.List;
import static com.oyob.controller.fragment.FriendsFragment.MERCHANTIMAGEURL;




/**
 * Created by 121 on 9/15/2017.
 */

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.PersonViewHolder> {
    List<FriendsModel> persons;
    private Context context;
    boolean isPressed;

    public FriendsRecyclerAdapter(Context context,List<FriendsModel> persons) {
        this.persons = persons;
        this.context=context;
    }

    @Override
    public FriendsRecyclerAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_adapter, parent, false);
        FriendsRecyclerAdapter.PersonViewHolder pvh = new FriendsRecyclerAdapter.PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final FriendsRecyclerAdapter.PersonViewHolder holder, int position) {
        final FriendsModel modelClass=persons.get(position);
        holder.personName.setText(persons.get(position).name);
        holder.personAge.setText(persons.get(position).Discount);

        holder.favimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPressed= modelClass.isPresssed();
                boolean Result= SharedPreferenceHelper.getBoolean(view.getContext(),"result",true);
                SharedPreferenceHelper.set(view.getContext(), "result", Result);
                if (Result) {
                    holder.favimageView.setColorFilter(view.getResources().getColor(R.color.colorAccent));
                    Toast.makeText(view.getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    SharedPreferenceHelper.set(view.getContext(), "result", false);
                    holder.favimageView.setColorFilter(view.getResources().getColor(android.R.color.white));
                    Toast.makeText(view.getContext(), "Removed Successfully", Toast.LENGTH_SHORT).show();
                }
                Result = false;

            }
        });


        if(!MERCHANTIMAGEURL.isEmpty())
        {
            Picasso.with(context).load(persons.get(position).getAndroid_image_url()).into(holder.imageView);
        }
        else {
            Glide.with(context).load(persons.get(position).getAndroid_image_url()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
            return persons.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView personName;
        TextView personAge;
        ImageView imageView;
        ImageView favimageView;

        PersonViewHolder(View itemView) {
            super(itemView);
            personName = (TextView) itemView.findViewById(R.id.rea_tv_merchant_name);
            personAge = (TextView) itemView.findViewById(R.id.rea_tv_get_now);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            favimageView = (ImageView) itemView.findViewById(R.id.friend_fav_imageView);
        }

    }
}
