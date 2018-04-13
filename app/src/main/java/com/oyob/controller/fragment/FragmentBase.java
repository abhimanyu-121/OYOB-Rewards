package com.oyob.controller.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.oyob.controller.R;
import com.oyob.controller.networkCall.PDRestClient;
import com.oyob.controller.utils.SessionManager;


public class FragmentBase extends Fragment {

    public Context _context;
    public SessionManager sessionManager;
    // Access a Cloud Firestore instance from your Activity
    public ProgressDialog dialog;

    public int screenHeight = 0, screenWidth = 0, statusBarHeight = 0;
    Typeface typeface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getActivity();
        sessionManager = new SessionManager(_context);
        dialog = new ProgressDialog(_context);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        statusBarHeight = rectangle.top;
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

    public void setEditTextFocusListener(final EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view != null && b)
                    editText.setSelection(editText.getText().length());
            }
        });
    }

    public boolean ensureNonNullFragment(Fragment fragment) {
        return fragment != null && fragment.isAdded() && fragment.isVisible();
    }

    @Override
    public void onDestroyView() {
        PDRestClient.cancel(_context);
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}