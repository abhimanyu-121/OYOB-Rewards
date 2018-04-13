package com.oyob.controller.service;

import android.util.Log;

import com.oyob.controller.model.FirsttimeLoginParser;
import com.oyob.controller.utils.AppConstants;


@SuppressWarnings("unused")
public class APILink implements NetworkListener {

    private static final int REQUEST_TEMP_PIN_VAlIDATION = 1;
    private static final int PIN_CREATE_REQUEST = 2;
    private static final int LOGIN_REQUEST = 3;
    private static APILink apiLink;
    private ServiceListener serviceListener;
    private String username;
    private String pin;

    public static APILink getApiLinkInstance() {
        if (apiLink == null) {
            apiLink = new APILink();
        }
        return apiLink;
    }

    // Temporary  Pin Request
    public void TempPinValidation(ServiceListener serviceListener, String tempPin) {
        try {
            this.serviceListener = serviceListener;
            this.pin = tempPin;
            HttpClient.getWWDispatchHandler().sendRequestAsync(
                    AppConstants.TEMP_PIN_VALIDATION, sendPinrequest(tempPin), null, this,
                    REQUEST_TEMP_PIN_VAlIDATION);

        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
                Log.w("RAMASAMY-->DEBUG", e);
            }
        }
    }

    private String sendPinrequest(String pin) {
        String pinTemp = null;
        try {
            pinTemp = "pin=" + pin;
            return pinTemp;
        } catch (Exception e) {
            return pinTemp;
        }
    }

    //   Pin Creation Request
    public void sendFirstTimePinDetails(ServiceListener serviceListener,
                                        String pinOne, String pinTwo) {
        this.serviceListener = serviceListener;
        HttpClient.getWWDispatchHandler().sendRequestAsync(
                AppConstants.PIN_CREATE,
                sendFirstLogin(pinOne, pinTwo), null, this,
                PIN_CREATE_REQUEST);
    }

    private String sendFirstLogin(String pinOne, String pinTwo) {
        String str = "pin1=" + pinOne + "&pin2=" + pinTwo;
        return str;
    }


    //   login Request
    public void sendLoginRequest(ServiceListener serviceListener,
                                 String username, String password, String affiliateId) {
        try {
            this.serviceListener = serviceListener;
            this.username = username;
            HttpClient.getWWDispatchHandler().sendRequestAsync(
                    AppConstants.USER_VIC_LOGIN,
                    getLoginRequestData(username, password, affiliateId), null,
                    this, LOGIN_REQUEST);
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
                Log.w("HARI-->DEBUG", e);
            }
        }
    }

    private String getLoginRequestData(String username, String password,
                                       String subDomainURL) {
        String loginPin = null;
        try {
            loginPin = "pin=" + username + "&pwd=" + password + "&sub="
                    + subDomainURL;
            return loginPin;
        } catch (Exception e) {
            return loginPin;
        }
    }

    @Override
    public void onRequestCompleted(String response, String errorString, int eventType) {
        switch (eventType) {
            case REQUEST_TEMP_PIN_VAlIDATION:
                if (errorString == null && response != null) {
                    new FirsttimeLoginParser().IdParser(response);
                    serviceListener.onServiceComplete(response, eventType);
                }
                break;
            case PIN_CREATE_REQUEST:
                if (errorString == null && response != null) {
                    new FirsttimeLoginParser().IdParser(response);
                    serviceListener.onServiceComplete(response, eventType);
                }
                break;

            case LOGIN_REQUEST:
                if (errorString == null && response != null) {
                    new FirsttimeLoginParser().IdParser(response);
                    serviceListener.onServiceComplete(response, eventType);
                }
                break;
        }
    }

}
