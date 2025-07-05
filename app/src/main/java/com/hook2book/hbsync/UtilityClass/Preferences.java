package com.hook2book.hbsync.UtilityClass;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.hook2book.hbsync.Model.SellerData.SellerData;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This Class is used to stored the preferences, it is a secured mechanism where all stored values and keys are encrypted
 * so that it can be prevent from unauthorised access
 */
public class Preferences extends Activity {

    public static final String SHARED_PREFS = "sharedPrefsToken";
    public static final String PRODUCT_COUNT = "productToken", TOKEN = "token", CONSUMER_KEY = "consumerKey", CONSUMER_SECRET = "consumerSecret", STARTVALUE = "startValue", ENDVALUE = "endValue", LOGINSTATUS = "loginStatus", CUSTID = "custId",NONCE="nonce", LOGIINNAME="loginName",CARTTOTAL="cartTotal",USEREMAIL="userEmail",DISPLAYNAME="displayName", GATEWAYINFO="gatewayInfo", UPDATEFLAG="updateflag";
    private static byte[] sKey;
    private static SharedPreferences sFile;
    private static final String TAG = "Preferences";
    private final Context context;
   private SharedPreferences  mPrefs;

    public Preferences(Context context) {
        this.context = context;
        // Proxy design pattern
        if (Preferences.sFile == null) {
            Preferences.sFile = PreferenceManager.getDefaultSharedPreferences(context);
        }
        mPrefs = getPreferences(MODE_PRIVATE);
        // Initialize encryption/decryption key
        try {
            final String key = Preferences.generateAesKeyName(context);
            String value = Preferences.sFile.getString(key, null);
            if (value == null) {
                value = Preferences.generateAesKeyValue();
                Preferences.sFile.edit().putString(key, value).commit();
            }
            Preferences.sKey = Preferences.decode(value);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    public static void saveAccountInfo(Context context, SellerData sellerData)
    {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sellerData);
        editor.putString("sellerData", json);
        editor.apply();
    }
    public static SellerData loadAccountInfo(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String accountInfo = sharedPreferences.getString("sellerData", "");
        Gson gson = new Gson();
        SellerData sellerData = gson.fromJson(accountInfo, SellerData.class);
        return sellerData;
    }
    public static void saveToken(Context context, String value) {

        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TOKEN, String.valueOf(value));
        Log.i(TAG, "SaveToken: " + value);
        editor.apply();
    }

    public static String loadToken(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String loginValue = sharedPreferences.getString(TOKEN, "");
        Log.i(TAG, "loadToken: " + loginValue);
        return loginValue;



    }
    public static void saveProductCount(Context context, String value) {

        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PRODUCT_COUNT, String.valueOf(value));
        Log.i(TAG, "PRODUCT_COUNT: " + value);
        editor.apply();
    }

    public static String loadProductCount(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String loginValue = sharedPreferences.getString(PRODUCT_COUNT, "");
        Log.i(TAG, "PRODUCT_COUNT: " + loginValue);
        return loginValue;



    }

    public static void saveCustId(Context context, String value) {

        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CUSTID, String.valueOf(value));
        editor.apply();
    }

    public static String loadCustID(Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String loginValue = sharedPreferences.getString(CUSTID, "");
        Log.i(TAG, "loadToken: " + loginValue);
        return loginValue;
    }

    public static void saveNonce(Context context, String value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(NONCE, String.valueOf(value));
        Log.i(TAG, "Save Nonce: " + value);
        editor.apply();
    }

    public static String loadNonce(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String loginValue = sharedPreferences.getString(NONCE, "");
        Log.i(TAG, "Load Nonce: " + loginValue);
        return loginValue;
    }

    public static void saveCombinedKey(Context context, String value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CONSUMER_KEY, String.valueOf(value));
        editor.apply();
    }

    public static void saveConsumerSecret(Context context, String value) {

        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CONSUMER_SECRET, String.valueOf(value));
        editor.apply();
    }

    public static String loadCombinedKey(Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        ///SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String loginValue = sharedPreferences.getString(CONSUMER_KEY, "");
        return loginValue;
    }

   /* public static String loadConsumerSecret(Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String loginValue = sharedPreferences.getString(CONSUMER_SECRET, "");
        return loginValue;
    }*/

    public static void saveUserName(Context applicationContext, String displayName) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DISPLAYNAME, displayName);
        editor.apply();

    }
    public static String loadUserName(Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String userName = sharedPreferences.getString(DISPLAYNAME, "");
        return userName;
    }
    public static void saveUserEmail(Context applicationContext, String userEmail) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USEREMAIL, userEmail);
        editor.apply();
    }

    public static String loadUserEmail(Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String userEmail = sharedPreferences.getString(USEREMAIL, "");
        return userEmail;
    }
    public static String loadDisplayName(Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String displayName = sharedPreferences.getString(DISPLAYNAME, "");
        return displayName;
    }



    public static boolean clear(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Log.i(TAG, "clearToken: ");
        return pref.edit().clear().commit();
    }

    public static void saveLoginStatus(Context applicationContext, String s) {
        //SharedPreferences sharedPreferences = App.getApplication().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGINSTATUS, s);
        // editor.putFloat(STARTVALUE, start);
        editor.apply();
    }

    public static String loadLoginStatus(Context context) {
        Preferences pref = new Preferences(context);
        pref.getStringRecord(LOGINSTATUS, "");
        pref.recordString(LOGINSTATUS, "hello");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String startValue = sharedPreferences.getString(LOGINSTATUS, "");
        return startValue;
    }
    public static void saveName(Context applicationContext, String s) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIINNAME, s);
        editor.apply();
    }

    public static String loadName(Context context) {
        Preferences pref = new Preferences(context);
        pref.getStringRecord(LOGIINNAME, "");
        pref.recordString(LOGIINNAME, "hello");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String startValue = sharedPreferences.getString(LOGIINNAME, "");
        return startValue;
    }

    public static void saveStartValue(Context context,String start) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STARTVALUE, start);
        editor.apply();
    }

    public  static void saveEndValue(Context context,String end) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ENDVALUE, end);
        editor.apply();
    }

    public static String loadStartValue(Context context) {
        Preferences pref = new Preferences(context);
        pref.getStringRecord(STARTVALUE, "");
        pref.recordString(STARTVALUE, "hello");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String startValue = sharedPreferences.getString(STARTVALUE, "");
        return startValue;
    }

    public static String loadEndValue(Context context) {
        Preferences pref = new Preferences(context);
        pref.getStringRecord(ENDVALUE, "");
        pref.recordString(ENDVALUE, "hello");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String endValue = sharedPreferences.getString(ENDVALUE, "");
        return endValue;
    }
    public static void saveCartTotal(Context applicationContext, String s) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CARTTOTAL, s);
        editor.apply();
    }

    public static String loadCartTotal(Context context) {
        Preferences pref = new Preferences(context);
        pref.getStringRecord(CARTTOTAL, "");
        pref.recordString(CARTTOTAL, "hello");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String startValue = sharedPreferences.getString(CARTTOTAL, "");
        return startValue;
    }
    public static void saveGatewayInfo(Context applicationContext, String value) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GATEWAYINFO, value);
        editor.apply();

    }
    public static String loadGatewayInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String userName = sharedPreferences.getString(GATEWAYINFO, "");
        return userName;
    }
    public static void saveUpdateFlag(Context applicationContext, String value) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UPDATEFLAG, value);
        editor.apply();

    }
    public static String loadUpdateflag(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String updateFlag = sharedPreferences.getString(UPDATEFLAG, "");
        return updateFlag;
    }




    private static String encode(byte[] input) {
        return Base64.encodeToString(input, Base64.NO_PADDING | Base64.NO_WRAP);
    }

    private static byte[] decode(String input) {
        return Base64.decode(input, Base64.NO_PADDING | Base64.NO_WRAP);
    }

    private static String generateAesKeyName(Context context) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final char[] password = context.getPackageName().toCharArray();
        final byte[] salt = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes();

        // Number of PBKDF2 hardening rounds to use, larger values increase
        // computation time, you should select a value that causes
        // computation to take >100ms
        final int iterations = 1000;

        // Generate a 256-bit key
        final int keyLength = 256;

        final KeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        return Preferences.encode(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).getEncoded());
    }

    private static String generateAesKeyValue() throws NoSuchAlgorithmException {
        // Do *not* seed secureRandom! Automatically seeded from system entropy
        final SecureRandom random = new SecureRandom();

        // Use the largest AES key length which is supported by the OS
        final KeyGenerator generator = KeyGenerator.getInstance("AES");
        try {
            generator.init(256, random);
        } catch (Exception e) {
            try {
                generator.init(192, random);
            } catch (Exception e1) {
                generator.init(128, random);
            }
        }
        return Preferences.encode(generator.generateKey().getEncoded());
    }

    private static String encrypt(String cleartext) {
        if (cleartext == null || cleartext.length() == 0) {
            return cleartext;
        }
        try {
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Preferences.sKey, "AES"));
            return Preferences.encode(cipher.doFinal(cleartext.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            Log.w(TAG, "encrypt", e);
            return null;
        }
    }

    private static String decrypt(String ciphertext) {
        if (ciphertext == null || ciphertext.length() == 0) {
            return ciphertext;
        }
        try {
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Preferences.sKey, "AES"));
            return new String(cipher.doFinal(Preferences.decode(ciphertext)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.w(TAG, "decrypt", e);
            return null;
        }
    }




    public void recordString(String key, String value) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Preferences.encrypt(key), Preferences.encrypt(String.valueOf(value)));
        editor.commit();

    }

    public void recordBoolean(String key, boolean value) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(Preferences.encrypt(key), Preferences.encrypt(String.valueOf(value)));
        editor.commit();

    }

    public void recordLong(String key, long value) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Preferences.encrypt(key), Preferences.encrypt(String.valueOf(value)));
        editor.commit();
    }

    public void recordInt(String key, int value) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Preferences.encrypt(key), Preferences.encrypt(String.valueOf(value)));
        editor.commit();
    }

    public String getStringRecord(String key, String defaultValue) {
        final String encryptedValue = PreferenceManager.getDefaultSharedPreferences(context).getString(Preferences.encrypt(key), null);

        return (encryptedValue != null) ? Preferences.decrypt(encryptedValue) : defaultValue;
    }

    public long getIntRecord(String key, int defaultValue) {
        final String encryptedValue = PreferenceManager.getDefaultSharedPreferences(context).getString(Preferences.encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(Preferences.decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }

    }

    public long getLongRecord(String key, long defaultValue) {
        final String encryptedValue = PreferenceManager.getDefaultSharedPreferences(context).getString(Preferences.encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(Preferences.decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }

    }

    public boolean getRecordBoolean(String key, boolean defaultValue) {
        final String encryptedValue = PreferenceManager.getDefaultSharedPreferences(context).getString(Preferences.encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(Preferences.decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }

    }

    public boolean clear() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.edit().clear().commit();
    }



}
