package com.oyob.controller.interfaces;

import android.view.View;

/**
 * Created by 121 on 10/4/2017.
 */

public interface OnRecyclerViewItemClick {
    void  listItemClick(View view,int  position);
    void  favoriteItemclick(View view,int  position);
    void  productRedeemClick(View view,int  position);
}
