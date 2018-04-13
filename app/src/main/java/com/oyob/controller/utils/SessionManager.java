package com.oyob.controller.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.oyob.controller.activity.ActivityDashboard;

import java.util.HashMap;

public class SessionManager {
    public static final String IS_FIRST_RUN = "IsFirstRun";
    public static final String IS_FIRST_RUN1 = "isFirstRun1";
    public static final String IS_FIRST_RUN2 = "isFirstRun2";
    public static final String IS_MAIN_SCREEN = "isMainScreen";
    // All Shared Preferences Keys
    public static final String CART_COUNT = "cart_count";
    public static final String CART_ID = "cart_id";
    public static final String ADDRESS_ID = "address_id";
    public static final String USER_ID = "userId";
    public static final String PRODUCT_ID = "productId";
    public static final String CLIENT_ID = "clientId";
    public static final String CID = "cid";
    public static final String DOMAIN_ID = "domainId";
    public static final String USER_TYPE = "type";
    public static final String USER_NAME = "userName";
    public static final String FACEBOOK_LINK = "facebookLink";
    public static final String TWITTER_LINK = "twitterLink";
    public static final String LOYALITY = "loyality";
    public static final String USER_EMAIL = "userEmail";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String DOB = "dob";
    public static final String STATE = "state";
    public static final String COUNTRY = "country";
    public static final String MOBILE = "mobile";
    public static final String PASSCODE = "passcode";
    public static final String CARD_EXT = "cardExt";
    public static final String CLIENT_NAME = "clientName";
    public static final String NEWSLETTER = "newsletter";
    public static final String MERCHANT_FEE = "merchnat_fee";
    public static final String BEARER = "bearer";
    public static final String PASSWORD = "password";
    public static final String PIN = "pin";
    public static final String INTERESTS = "interests";
    public static final String DEVICE_ID = "deviceId";
    public static final String USER_NOTIFICATIONS_ID = "gcm_id";
    public static final String CUSTOMER_ID = "customer_id";
    // Sharedpref file name
    private static final String PREF_NAME = "AtWorkCredentials";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsUserLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String user_id, String client_id, String domain_id, String user_type,
                                   String user_name, String fb_link, String twt_link, String loyality,
                                   String user_email, String first_name, String last_name, String state,
                                   String country, String mobile, String card_ext, String client_name,
                                   String newsletter, String merchant_fee, String bearer, String password,
                                   String device_id, String user_gcmid, String customer_id) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing login value as TRUE
        editor.putBoolean(IS_FIRST_RUN, true);

        // Storing user id in pref

        // Storing user name in pref
        editor.putString(USER_ID, user_id);
        editor.putString(CLIENT_ID, client_id);
        editor.putString(DOMAIN_ID, domain_id);
        editor.putString(USER_TYPE, user_type);
        editor.putString(USER_NAME, user_name);
        editor.putString(FACEBOOK_LINK, fb_link);
        editor.putString(TWITTER_LINK, twt_link);
        editor.putString(LOYALITY, loyality);
        editor.putString(USER_EMAIL, user_email);
        editor.putString(FIRST_NAME, first_name);
        editor.putString(LAST_NAME, last_name);
        editor.putString(STATE, state);
        editor.putString(COUNTRY, country);
        editor.putString(MOBILE, mobile);
        editor.putString(CARD_EXT, card_ext);
        editor.putString(CLIENT_NAME, client_name);
        editor.putString(NEWSLETTER, newsletter);
        editor.putString(MERCHANT_FEE, merchant_fee);
        editor.putString(BEARER, bearer);
        editor.putString(PASSWORD, password);
        editor.putString(DEVICE_ID, device_id);
        editor.putString(USER_NOTIFICATIONS_ID, user_gcmid);
        editor.putString(CUSTOMER_ID, customer_id);

        // commit changes
        editor.apply();
    }

    public String getParticularField(String key) {
        return pref.getString(key, null);
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, pref.getString(USER_ID, null));
        user.put(CLIENT_ID, pref.getString(CLIENT_ID, null));
        user.put(USER_NOTIFICATIONS_ID, pref.getString(USER_NOTIFICATIONS_ID, null));
        // return user
        return user;
    }

    public int notificationSubscribed() {
        return pref.getInt(USER_NOTIFICATIONS_ID, 0);
    }

    public void updateNotificationDetails(int notifications) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putInt(USER_NOTIFICATIONS_ID, notifications);

        editor.apply();
    }

    public void updatePerticularField(String key, String value) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(key, value);

        editor.apply();
    }

    public void updateIsFirstRun(boolean value) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putBoolean(IS_FIRST_RUN, value);

        editor.apply();
    }

    public void updateIsLoggin(boolean value) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putBoolean(IS_LOGIN, value);

        editor.apply();
    }

    public void updateProductId(String product_id) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(PRODUCT_ID, product_id);

        editor.apply();
    }

    public void updateCustomerId(String customer_id) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(CUSTOMER_ID, customer_id);

        editor.apply();
    }

    public void updateCartCount(int cart_count) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putInt(CART_COUNT, cart_count);

        editor.apply();
    }

    public void updateCartId(String cart_id) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(CART_ID, cart_id);

        editor.apply();
    }

    public void updateAddress(String address_id) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(ADDRESS_ID, address_id);

        editor.apply();
    }

    public void updateUserId(String userId) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(USER_ID, userId);

        editor.apply();
    }

    public void updateClientId(String clientId) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(CLIENT_ID, clientId);

        editor.apply();
    }

    public void updateCId(String cid) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(CID, cid);

        editor.apply();
    }

    public void updateDomainId(String domainId) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(DOMAIN_ID, domainId);

        editor.apply();
    }

    public void updateUserType(String userType) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(USER_TYPE, userType);

        editor.apply();
    }

    public void updateUserName(String username) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(USER_NAME, username);

        editor.apply();
    }

    public void updateFacebookLink(String fbLink) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(FACEBOOK_LINK, fbLink);

        editor.apply();
    }

    public void updateTwitterLink(String twtLink) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(TWITTER_LINK, twtLink);

        editor.apply();
    }

    public void updateLoyality(String loyality) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(LOYALITY, loyality);

        editor.apply();
    }

    public void updateEmail(String email) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(USER_EMAIL, email);

        editor.apply();
    }

    public void updateFirstName(String firstName) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(FIRST_NAME, firstName);

        editor.apply();
    }

    public void updateLastName(String lastName) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(LAST_NAME, lastName);
        editor.apply();
    }

    public void updateDOB(String dob) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(DOB, dob);
        editor.apply();
    }

    public void updateState(String state) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(STATE, state);

        editor.apply();
    }

    public void updateCountry(String country) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(COUNTRY, country);

        editor.apply();
    }

    public void updatePasscode(String passcode) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(PASSCODE, passcode);

        editor.apply();
    }

    public void updateMobile(String mobile) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(MOBILE, mobile);

        editor.apply();
    }

    public void updateCardExt(String cardExt) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(CARD_EXT, cardExt);

        editor.apply();
    }

    public void updateClientName(String clientName) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(CLIENT_NAME, clientName);

        editor.apply();
    }

    public void updateNewsletter(String newsletter) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(NEWSLETTER, newsletter);

        editor.apply();
    }

    public void updateMerchantFee(String merchantFee) {
        editor = pref.edit();
        editor.putString(MERCHANT_FEE, merchantFee);
        editor.apply();
    }

    public void updateBearer(String bearer) {
        editor = pref.edit();
        editor.putString(BEARER, bearer);
        editor.apply();
    }

    public void updatePassword(String password) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(PASSWORD, password);

        editor.apply();
    }

    public void updatePin(String pin) {
        editor = pref.edit();
        editor.putString(PIN, pin);

        editor.apply();
    }

    public void updateInterests(String interests) {
        editor = pref.edit();
        editor.putString(INTERESTS, interests);

        editor.apply();
    }

    public void updateDeviceId(String deviceId) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(DEVICE_ID, deviceId);

        editor.apply();
    }

    public void updateGCMID(String gcm_id) {
        editor = pref.edit();
        // Storing user_notification_prefrence in pref
        editor.putString(USER_NOTIFICATIONS_ID, gcm_id);

        editor.apply();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, ActivityDashboard.class);
            i.putExtra("fromSplash", false);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();

		/*// After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginUserActivity.class);

		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);*/
    }

    public void finish_all_data_of_user() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isFirstRun() {
        return pref.getBoolean(IS_FIRST_RUN, false);
    }

    public boolean isFirstRun1() {
        return pref.getBoolean(IS_FIRST_RUN1, false);
    }

    public boolean isFirstRun2() {
        return pref.getBoolean(IS_FIRST_RUN2, false);
    }

    public boolean isMainScreen() {
        return pref.getBoolean(IS_MAIN_SCREEN, false);
    }
}