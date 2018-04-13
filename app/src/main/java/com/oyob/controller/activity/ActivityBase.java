package com.oyob.controller.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.oyob.controller.R;
import com.oyob.controller.networkCall.PDRestClient;
import com.oyob.controller.utils.SessionManager;
import com.oyob.controller.utils.TinyDB;


/**
 * Created by Narender Kumar on 3/21/2017.
 * For SFWorx Technologies (LLP), Faridabad (India)
 */

public class ActivityBase extends AppCompatActivity {

    Context context;
    public TinyDB tinyDB;
    public ProgressDialog dialog;
    public SessionManager sessionManager;
    //ProgressDialogAsync _progressDialogAsync;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        context = this;
        sessionManager = new SessionManager(context);
        tinyDB = new TinyDB(context);

        dialog = new ProgressDialog(context);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);

        //_progressDialogAsync = new ProgressDialogAsync(context);
    }

    public static void showToastShort(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToastLong(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void setTextChangeListener(EditText editText, final TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout.setError(null);
                textInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void finishAfterTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAfterTransition();
        } else {
            super.finish();
        }
    }

    @Override
    protected void onDestroy() {
        PDRestClient.cancel(context);
        super.onDestroy();
    }
}