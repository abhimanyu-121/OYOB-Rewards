package com.oyob.controller.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oyob.controller.R;
import com.oyob.controller.model.ModelClass;
import com.oyob.controller.model.MyShopModel;
import com.oyob.controller.utils.AppConstants;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by 121 on 9/14/2017.
 */
public class MyShopeAdapter extends RecyclerView.Adapter<MyShopeAdapter.PersonViewHolder> {
    private String uname;
    List<ModelClass> persons;
    private Context context;
    static final String TAG = "ExclusiveRecycleAdapter";
    private static final String SET_FAV = AppConstants.SERVER_ROOT + "set_favourites.php";
    private String androidId;
    private String reportDate;
    private String cid;
    private String userId;
    private String cat_id;
    ArrayList<MyShopModel> shopModels = new ArrayList<>();
    public static String extra="";

    public MyShopeAdapter(Context context, ArrayList<MyShopModel> shopModels) {
        this.shopModels = shopModels;
        this.context = context;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mycart, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int position) {

        // holder.txt_category.setText(shopModels.get(position).getCategoryName());
        holder.item_name.setText(shopModels.get(position).getItemName());
        double a= Double.valueOf(shopModels.get(position).getPrice())*(shopModels.get(position).getQty());
        //holder.txt_price.setText("$" + shopModels.get(position).getPrice());
        holder.txt_price.setText("$" + a);
        holder.txt_qty.setText("" + shopModels.get(position).getQty());
        holder.txt_discount.setText(shopModels.get(position).getDiscount());
        holder.warehouse_quantity.setText(shopModels.get(position).getWarehouse_product_quantity());


        extra=extra+"\n"+String.valueOf(position+1)+". "+"ProductName:"+shopModels.get(position).getItemName()+"\nQuantity:"+shopModels.get(position).getQty()+"\nId:"+shopModels.get(position).getPid()+"\nPrice:"+shopModels.get(position).getPrice();

        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (shopModels != null) {
            return shopModels.size();
        }
        return 0;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView txt_category;
        TextView item_name;
        TextView txt_discount;
        TextView txt_price;
        TextView txt_qty;
        TextView warehouse_quantity;
        ImageView imageView;
        ImageView favimageView, btn_remove;
        CardView relativeLayout;

        PersonViewHolder(View itemView) {
            super(itemView);
            txt_category =  itemView.findViewById(R.id.txt_category);
            item_name =  itemView.findViewById(R.id.item_name);
            txt_price =  itemView.findViewById(R.id.txt_price);
            txt_qty =  itemView.findViewById(R.id.txt_qty);
            txt_discount =  itemView.findViewById(R.id.txt_discount);
            btn_remove =  itemView.findViewById(R.id.btn_remove);
            warehouse_quantity =  itemView.findViewById(R.id.warehouse_quantity);

        }
    }

}