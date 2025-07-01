package com.hook2book.hbsync.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.databinding.ActivityLongDescriptionBinding

class LongDescription : BaseActivity() {
    private lateinit var binding: ActivityLongDescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLongDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Long Description", false)
        if (intent.hasExtra("longDescription")) {
            binding.longDescriptionInputInner.setText(intent.getStringExtra("longDescription"))
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val returnIntent = Intent()
                returnIntent.putExtra("activityName", "LongDescription")
                if (!binding.longDescriptionInputInner.text.toString().isEmpty()) {
                    returnIntent.putExtra(
                        "longDescription", binding.longDescriptionInputInner.text.toString()
                    )
                }
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        })
    }

}