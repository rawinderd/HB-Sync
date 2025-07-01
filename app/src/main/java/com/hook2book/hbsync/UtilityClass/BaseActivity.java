package com.hook2book.hbsync.UtilityClass;

//import static com.hook2book.MainActivity.SHARED_PREFS;


import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hook2book.hbsync.R;


public abstract class BaseActivity extends AppCompatActivity
{

    private static final String TAG = "BaseActivity";
    private static final String STARTVALUE = "startValue";
    private static final String ENDVALUE = "endValue";
    private static final String STATUSVALUE = " statusValue";
    private static final String COLORCODE = " colorCode";

    private final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_NETWORK_STATE

    };
    private String initialLocale;
    private Toolbar toolbar;
    private TextView mTitle;
    private boolean isOptionMenuRequired;
    private MenuItem searchItem;
    // protected SRMultiApplication mMyApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //   mMyApp = (SRMultiApplication) this.getApplicationContext();



/*
CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()S
                .setDefaultFontPath("fonts/montserrat_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
*/

    }



   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }*/

    public void setToolbar(Toolbar toolbar, boolean isBackButtonRequired, String title, boolean isOptionMenuRequired)
    {
        this.toolbar = toolbar;
        TextView toolbarText;
        toolbarText = findViewById(R.id.toolbar_text);
        setSupportActionBar(toolbar);
        //toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_launcher)); // setting a logo in Toolbar
        // toolbar.setTitle(getString(R.string.app_name));
        //toolbar.setTitle(title);
        this.isOptionMenuRequired = isOptionMenuRequired;
        getSupportActionBar().setDisplayHomeAsUpEnabled(isBackButtonRequired);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (title == null)
        {
            toolbar.setTitle(getResources().getString(R.string.app_name));
        } else
        {
            if (title.equals("Home"))
            {
                toolbar.setLogo(getResources().getDrawable(R.mipmap.logofortitlecopy));

            } else
            {
                toolbarText.setText(title);
                toolbar.setTitle("");
                //getSupportActionBar().setTitle(title);
            }
        }
        if (isBackButtonRequired)
        {
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onBackPressed();
                }
            });
        }
    }

    public void updateToolBarText(Toolbar toolbarString, String title)
    {
        this.toolbar = toolbarString;
        TextView toolbarText;
        toolbarText = findViewById(R.id.toolbar_text);
        toolbarText.setText(title);
    }

    public void Toasti(String toastString)
    {
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
    }

    public void ToastiShort(String toastString)
    {
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
    }


    public AlertDialog DialogGen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(R.layout.layout_loading_dialog);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

  /*  public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(50);
        }
    }*/

    /*

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        // return super.onCreateOptionsMenu(menu);
        return isOptionMenuRequired;
    }
*/
    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_cart:
                if (Preferences.loadLoginStatus(getApplicationContext()).equals("1")) {
                    Intent intent = new Intent(getApplicationContext(), CartActivityLocal.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "To Main Cart ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(intent);
                }
                break;
            //Toast.makeText(this, "Cart Button Pressed", Toast.LENGTH_SHORT).show();
            case R.id.action_search: {
                onSearchRequested();
                break;
            }
            case R.id.action_wishlist: {

                Intent intent = new Intent(getApplicationContext(), WishActivity.class);
                startActivity(intent);
                //Toast.makeText(this, "Wishlist from Base pressed", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.action_home: {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                return true;
            }
            default:
        }
        return super.onOptionsItemSelected(item);
    }
*//*
    public void setToolbarText(String title) {
        if (title != null)
            mTitle.setText(title);
        else
            mTitle.setText(getResources().getString(R.string.app_name));
    }

    public Activity getActivity() {
        return this;
    }


    public void hideActionButton() {

        searchItem.setVisible(false);
    }

    public String loadLoginStatus() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String loginValue = sharedPreferences.getString(HomeActivity.LOGINSTATUS, "");
        return loginValue;
    }


    public void saveStartValue(String start)
    {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STARTVALUE, start);
        editor.apply();
    }
    public void saveEndValue(String end)
    {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ENDVALUE, end);
        editor.apply();
    }
    public String loadStartValue(){
        Preferences pref = new Preferences(getApplication());
        pref.getStringRecord(STARTVALUE, "");
        pref.recordString(STARTVALUE, "hello");
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(STARTVALUE, "");
    }
    public String loadEndValue(){
        Preferences pref = new Preferences(getApplication());
        pref.getStringRecord(ENDVALUE, "");
        pref.recordString(ENDVALUE, "hello");
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(ENDVALUE, "");
    }
    public String loadStatusValue()
    {
        Preferences pref = new Preferences(getApplication());
        pref.getStringRecord(STATUSVALUE, "");
        pref.recordString(STATUSVALUE, "hello");
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(STATUSVALUE, "");
    }
    public void saveColorValue(String colorCode)
    {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COLORCODE, colorCode);
        editor.apply();
    }
    public String loadColorValue(){
        Preferences pref = new Preferences(getApplication());
        pref.getStringRecord(COLORCODE, "");
        pref.recordString(COLORCODE, "hello");
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(COLORCODE, "");
    }*/

}