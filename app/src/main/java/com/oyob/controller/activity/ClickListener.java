package com.oyob.controller.activity;

import android.view.View;

/**
 * Created by 121 on 9/18/2017.
 */

 public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
