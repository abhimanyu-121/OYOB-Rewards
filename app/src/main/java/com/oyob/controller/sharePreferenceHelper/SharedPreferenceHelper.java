package com.oyob.controller.sharePreferenceHelper;



import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;


public class SharedPreferenceHelper {

    private static final String TAG = "SharedPreferenceHelper";
    private static SharedPreferenceHelper   sharedPreference;

    public static SharedPreferenceHelper getInstance() {
        if (sharedPreference == null) {
            sharedPreference = new SharedPreferenceHelper();
        }
        return sharedPreference;
    }

    public SharedPreferenceHelper() {
        super();
    }


    /**
     * Checks whether the preferences contains a key or not
     *
     * @param key The string resource Id of the key
     * @return <code>true</code> if the key exists, <code>false</code> otherwise
     */
    public static boolean contains(Context context , final int key) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains(context.getString(key));
    }

    public static String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }


    /**
     * Get String value for a particular key.
     *
     * @param key The string resource Id of the key
     * @return String value that was stored earlier, or empty string if no mapping exists
     */
    public static String getString(Context context , final int key) {

        return context.getString(key, "");
    }


    /**
     * Get String value for a particular key.
     *
     * @param key      The string resource Id of the key
     * @param defValue The default value to return
     * @return String value that was stored earlier, or the supplied default value if no mapping
     * exists
     */
    public static String getString(Context context , final String key, final String defValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defValue);
    }


    /**
     * Get String value for a particular key.
     *
     * @param key      The string resource Id of the key
     * @param defValue The default value to return
     * @return String value that was stored earlier, or the supplied default value if no mapping
     * exists
     */
    public static String getString(Context context , final int key, final String defValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(key), defValue);
    }


    /**
     * Get int value for key.
     *
     * @param key      The string resource Id of the key
     * @param defValue The default value
     * @return value or defValue if no mapping exists
     */
    public static int getInt(Context context , final String key, final int defValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, defValue);
    }


    /**
     * Get int value for key.
     *
     * @param key      The string resource Id of the key
     * @param defValue The default value
     * @return value or defValue if no mapping exists
     */
    public static int getInt(Context context , final int key, final int defValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(key), defValue);
    }

    /**
     * Get float value for a particular key.
     *
     * @param key      The string resource Id of the key
     * @param defValue The default value to return if the requested key is not present
     * @return value or defValue if no mapping exists
     */
    public static float getFloat(Context context , final int key, final float defValue) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getFloat(context.getString(key), defValue);

    }

    /**
     * Get double value for a particular key.
     *
     * @param key      The string resource Id of the key
     * @param defValue The default value to return if the requested key is not present
     * @return value or defValue if no mapping exists
     */
    public static double getDouble(Context context , final int key, final double defValue) {

        final String stringValue = context.getString(key);

        if (TextUtils.isEmpty(stringValue)) {
            return defValue;
        } else {

            try {
                return Double.parseDouble(stringValue);
            } catch (final NumberFormatException e) {
                return defValue;
            }
        }

    }



    /**
     * Get long value for a particular key.
     *
     * @param key      The string resource Id of the key
     * @param defValue The default value to fetch if the requested key doesn't exist
     * @return value or defValue if no mapping exists
     */
    public static long getLong(Context context , final int key, final long defValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(context.getString(key), defValue);
    }


    /**
     * Get long value for a particular key.
     *
     * @param key      The string resource Id of the key
     * @param defValue The default value to fetch if the requested key doesn't exist
     * @return value or defValue if no mapping exists
     */
    public static long getLong(Context context , final String key, final long defValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, defValue);
    }



    /**
     * Get boolean value for a particular key.
     *
     * @param key      The string resource Id of the key
     * @param defValue The default value to fetch if the key doesn't exist
     * @return value or defValue if no mapping exists
     */
    public static boolean getBoolean(Context context , final int key, final boolean defValue) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(key), defValue);
    }


    /**
     * Get boolean value for a particular key.
     *
     * @param key      The string value key
     * @param defValue The default value to fetch if the key doesn't exist
     * @return value or defValue if no mapping exists
     */
    public static boolean getBoolean(Context context , final String key, final boolean defValue) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defValue);
    }


    /**
     * Set String value for a particular key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void set(Context context , final int key, final String value) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(key), value);
        editor.apply();
    }


    /**
     * Set String value for a particular key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void set(Context context , final String key, final String value) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    /**
     * Set int value for key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void set(Context context , final int key, final int value) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(context.getString(key), value);
        editor.apply();
    }


    /**
     * Set float value for a key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void set(Context context , final int key, final float value) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();

        editor.putFloat(context.getString(key), value);
        editor.apply();
    }


    /**
     * Set double value for a key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void set(Context context , final int key, final double value) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();

        editor.putString(context.getString(key), String.valueOf(value));
        editor.apply();
    }


    /**
     * Set long value for key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void set(Context context , final int key, final long value) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(context.getString(key), value);
        editor.apply();
    }


    /**
     * Set long value for key.
     *
     * @param key
     * @param value
     */
    public static void set(Context context , final String key, final int value) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Set long value for key.
     *
     * @param key
     * @param value
     */
    public static void set(Context context , final String key, final long value) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }


    /**
     * Set boolean value for key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void set(Context context , final int key, final boolean value) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(context.getString(key), value);
        editor.apply();
    }


    /**
     * Set boolean value for key.
     *
     * @param key   The string value of the key
     * @param value The value to set for the key
     */
    public static void set(Context context , final String key, final boolean value) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    /**
     * Clear all preferences.
     *
     * @param context
     */
    public static void clearPreferences(final Context context) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }


    /**
     * Removes the given keys from the Shared Preferences
     *
     * @param context
     * @param keys    The keys to removed
     */
    public static void removeKeys(final Context context, final int... keys) {

        assert (keys != null);
        assert (keys.length > 0);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();

        for (final int aKey : keys) {

            editor.remove(context.getString(aKey));
        }

        editor.apply();
    }


    /**
     * Register a listener to listen for changes to Shared Preferences
     *
     * @param onSharedPreferenceChangeListener A listener to listen for preference changes
     */
    public static void registerSharedPreferencesChangedListener(Context context , final SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }


    /**
     * Unregister a previously registered listener
     *
     * @param onSharedPreferenceChangeListener The listener to unregister
     */
    public static void unregisterSharedPreferencesChangedListener(Context context , final SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

}
