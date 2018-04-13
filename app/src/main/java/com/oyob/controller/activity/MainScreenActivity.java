package com.oyob.controller.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.Func2;
import com.github.kayvannj.permission_utils.Func3;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.oyob.controller.R;
import com.oyob.controller.utils.SessionManager;


/**
 * Created by Ramasamy on 9/7/2017.
 */
public class MainScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_BOTH = 3001;
    static boolean isMainScreen;
    SessionManager sessionManager;
    TextView loginTextView;
    TextView activateTextView;
    Context context;
    private PermissionUtil.PermissionRequestObject mPermissionRequest;
    //private TextView help;
    private ImageView help;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        context = getApplicationContext();
        sessionManager = new SessionManager(context);
        loginTextView = findViewById(R.id.login);
        activateTextView = findViewById(R.id.activateTextView);

        activateTextView.setOnClickListener(this);
        loginTextView.setOnClickListener(this);

        linearLayout=findViewById(R.id.main_screen);
        //linearLayout.getBackground().setAlpha(120);

        //help=findViewById(R.id.help);

        help = findViewById(R.id.help);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
                builder.setMessage("If you are a current user, and have logged in before on the website or app, click on Login and Create your PIN. If this your first time to the program and have never accessed the website or the app before, click on ACTVATE and set up your account.\n" +
                        " \n" +
                        "If you have any trouble, call us on 1300 368 846")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        isMainScreen = sessionManager.isFirstRun();
        //isMainScreen = context.getSharedPreferences("PREFERENCES", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (!isMainScreen && !TextUtils.isEmpty(sessionManager.getParticularField(SessionManager.CLIENT_ID)) && !TextUtils.isEmpty(sessionManager.getParticularField(SessionManager.PIN))) {
            Intent intent = new Intent(MainScreenActivity.this, ActivityDashboard.class);
            startActivity(intent);
        }
        AskForPermission();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login:
                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(intent);
                break;
            case R.id.activateTextView:
                //Intent intent1 = new Intent(getApplicationContext(), GenerateTempPinActivity.class);
                Intent intent1 = new Intent(getApplicationContext(), ActivityActivate.class);
                startActivity(intent1);
        }
    }

    private void AskForPermission() {
        mPermissionRequest = PermissionUtil.with(this).request(Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.READ_CALENDAR
                , Manifest.permission.WRITE_CALENDAR)
                .onResult(
                        new Func2() {
                            @Override
                            protected void call(int requestCode, String[] permissions, int[] grantResults) {
                                //permissionRationale.clear();
                                for (int i = 0; i < permissions.length; i++) {
                                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                        doOnPermissionGranted(permissions[i]);
                                    } else {
                                        doOnPermissionDenied(permissions[i]);
                                    }
                                }
                            }
                        })
                .onAllGranted(new Func() {
                    @Override
                    protected void call() {
                        //Logger.d("onAllGranted: ");
                    }
                }).onAnyDenied(new Func() {
                    @Override
                    protected void call() {
                        // Logger.e("onAnyDenied: ");
                    }
                }).onRational(new Func3() {
                    @Override
                    protected void call(String permissionName) {
                        // Logger.e("onRational: " + permissionName);

                    }
                })
                .ask(REQUEST_CODE_BOTH);
    }

    private void doOnPermissionDenied(String permission) {
        // Logger.d(permission + " Permission Denied or is on \"Do Not SHow Again\"");
    }

    private void doOnPermissionGranted(String permission) {
        // Logger.d(permission + " Permission Granted");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermissionRequest != null) {
            mPermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}