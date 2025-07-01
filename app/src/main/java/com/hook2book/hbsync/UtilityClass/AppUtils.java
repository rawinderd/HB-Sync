package com.hook2book.hbsync.UtilityClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.hook2book.hbsync.R;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppUtils
{

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private static final long backPressed = 0;

    static
    {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "m");
        suffixes.put(1_000_000_000L, "g");
        suffixes.put(1_000_000_000_000L, "t");
    }

  /*  public static void tapToExit(Activity activity) {
        if (backPressed + 2500 > System.currentTimeMillis()) {
            activity.finish();
        } else {
            showToast(activity.getApplicationContext(), activity.getResources().getString(R.string.tapAgain));
        }
        backPressed = System.currentTimeMillis();
    }*/

    /* public static boolean isNetworkAvailable(Context context) {
         ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             Network nw = connectivityManager.getActiveNetwork();
             if (nw == null) return false;
             NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
             return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
         } else {
             NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
             return nwInfo != null && nwInfo.isConnected();
         }
     }
 */
    private static String formatPhoneNumber(String previousPhoneNumber)
    {
        if (previousPhoneNumber != null)
        {
            previousPhoneNumber = previousPhoneNumber.replaceAll(" ", "");
            if (!previousPhoneNumber.startsWith("0") && !previousPhoneNumber.startsWith("+"))
            {
                return "0" + previousPhoneNumber;
            }
        }
        return previousPhoneNumber;
    }

    /*public static void noInternetWarning(View view, final Context context) {
        if (!isNetworkAvailable(context)) {
            Snackbar snackbar = Snackbar.make(view, context.getString(R.string.internet_connection), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(context.getString(R.string.connect), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            snackbar.show();
        }
    }*/

    public static void showSnackBar(final View view, final Activity activity, String message)
    {

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Back", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activity.finish();
            }
        });
        snackbar.show();

    }

    public static void showSnackBarSimple(final View view, final Activity activity, String message)
    {

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        /*snackbar.setAction("Back", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });*/
        snackbar.show();

    }

   /* public static void showSnakbarRetryIP(final View view, final Activity activity, String message) {

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPublicIP() {
                    @Override
                    public void yourIp(String ip) {
                        if (ip != null) {
                            MyApplication.getPreference().recordString(Constant.Json.IP_ADDRESS, ip);
                        }
                    }
                }.fetch(activity);
            }
        });
        snackbar.show();

    }*/

    public static void sendEmail(Activity activity, String email, String subject, String body)
    {
        if (email != null)
        {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            i.putExtra(Intent.EXTRA_TEXT, body);
            try
            {
                activity.startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public static boolean isPackageInstalled(Context context, String packagename)
    {
        try
        {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packagename, 0);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public static int dpToPx(Context c, int dp)
    {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void bounceView(View view)
    {
        ScaleAnimation fade_in = new ScaleAnimation(0.0f, 1f, 0.0f, 1f, 1, 0.5f, 1, 0.5f);
        fade_in.setDuration(250);
        fade_in.setFillAfter(true);
        view.startAnimation(fade_in);
    }

    public static String getHostName(String url)
    {
        try
        {
            URI uri = new URI(url);
            String new_url = uri.getHost();
            if (!new_url.startsWith("www.")) new_url = "www." + new_url;
            return new_url;
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
            return url;
        }
    }

    /*public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
*/
    public static boolean isPasswordValid(String password)
    {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        return pattern.matcher(password).matches();
    }

    public static String bigNumberFormat(long value)
    {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return bigNumberFormat(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + bigNumberFormat(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static boolean isdarkTheme(Context ctx)
    {
        int night_mode = ctx.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return night_mode == Configuration.UI_MODE_NIGHT_YES;
    }


    /*public static void setSmartSystemBar(Activity act) {
        if (isdarkTheme(act)) {
            setSystemBarColor(act, R.color.whiteCardColor);
        } else {
            setSystemBarColor(act, R.color.whiteCardColor);
            setSystemBarLight(act);
        }
    }*/


    public static void setSystemBarColor(Activity act)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static void setSystemBarColor(Activity act, @ColorRes int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }

    public static void setSystemBarLight(Activity act)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            View view = act.findViewById(android.R.id.content);
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    public static void changeMenuIconColor(Menu menu, @ColorInt int color)
    {
        for (int i = 0; i < menu.size(); i++)
        {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable == null) continue;
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static void changeOverflowMenuIconColor(Toolbar toolbar, @ColorInt int color)
    {
        try
        {
            Drawable drawable = toolbar.getOverflowIcon();
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        } catch (Exception e)
        {
        }
    }


    /**
     * For device_name info parameters
     */
    public static String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer))
        {
            return model;
        } else
        {
            return manufacturer + " " + model;
        }
    }

    public static String getAndroidVersion()
    {
        return Build.VERSION.RELEASE;
    }

    public static int getVersionCode(Context ctx)
    {
        try
        {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            return -1;
        }
    }


   /* public static String getVersionName(Context ctx) {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return ctx.getString(R.string.app_name) + " " + ctx.getString(R.string.app_version) + " " + info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return ctx.getString(R.string.version_unknown);
        }
    }*/

    /*public static String getVersionNamePlain(Context ctx) {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return ctx.getString(R.string.version_unknown);
        }
    }*/


    public static String getDeviceID(Context context)
    {
        String deviceID = Build.SERIAL;
        if (deviceID == null || deviceID.trim().isEmpty() || deviceID.equalsIgnoreCase("unknown") || deviceID.equals("0"))
        {
            try
            {
                deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (Exception e)
            {
            }
        }
        return deviceID;
    }

    public static boolean isEmailValid(String email)
    {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }


}
