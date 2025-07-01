package com.hook2book.hbsync.Activities
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.databinding.ActivityShortDescriptionBinding

class ShortDescription : BaseActivity() {
    private lateinit var binding: ActivityShortDescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShortDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Short Description", false)
        if (intent.hasExtra("shortDescription")) {
            binding.shortDescriptionInputInner.setText(intent.getStringExtra("shortDescription"))
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val returnIntent = Intent()
                returnIntent.putExtra("activityName", "ShortDescription")
                if (!binding.shortDescriptionInputInner.text.toString().isEmpty()) {
                    returnIntent.putExtra(
                        "shortDescription", binding.shortDescriptionInputInner.text.toString()
                    )
                }
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        })
    }/*override fun onBackPressed() {
        super.onBackPressed()
        val returnIntent = Intent()
        returnIntent.putExtra("activityName", "ShortDescription")
        if (!binding.shortDescriptionInputInner.text.toString().isEmpty()) {
            returnIntent.putExtra(
                "shortDescription", binding.shortDescriptionInputInner.text.toString()
            )
        }
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }*/
}