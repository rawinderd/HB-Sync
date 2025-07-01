package com.hook2book.hbsync.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Settings", false)
        binding.stopProductSwitch.setOnClickListener(View.OnClickListener {
            Toasti("update api for Stop Product & Main Product")
        })
        binding.trashProductText2.setOnClickListener(View.OnClickListener {
            Toasti("Trash Product API Called and Main Product Table")
        })

    }
}