package com.hook2book.hbsync.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.databinding.ActivityInventoryBinding

class Inventory : BaseActivity() {
    private lateinit var binding: ActivityInventoryBinding
    private lateinit var stockStatusSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Inventory", false)
        val stockStatus = resources.getStringArray(R.array.stockStatusArray)
        stockStatusSpinner = findViewById(R.id.stock_status_spinner)

        if (intent.hasExtra("sku")) {
            binding.skuInputInner.setText(intent.getStringExtra("sku"))
        }
        if (intent.hasExtra("stockStatus")) {
            val spinnderPosition = stockStatus.indexOf(intent.getStringExtra("stockStatus"))
            stockStatusSpinner.post(Runnable { stockStatusSpinner.setSelection(spinnderPosition) })
        }
        binding.manageStockSwitch.isEnabled = false
        binding.manageStockSwitch.isChecked = false
        if (binding.manageStockSwitch.isChecked == false) {
            binding.quantityInputOuter.visibility = View.GONE
            //binding.backOrdersInputOuter.visibility = View.GONE
        } else {
            binding.stockInputOuter.visibility = View.GONE
        }

        if (stockStatus != null) {
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, stockStatus)
            stockStatusSpinner.adapter = adapter
        }

        binding.manageStockSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (binding.manageStockSwitch.isChecked == true) {
                binding.stockInputOuter.visibility = View.GONE
                binding.quantityInputOuter.visibility = View.VISIBLE
                //binding.backOrdersInputOuter.visibility = View.VISIBLE

            } else {
                binding.quantityInputOuter.visibility = View.GONE
                //binding.backOrdersInputOuter.visibility = View.GONE
                binding.stockInputOuter.visibility = View.VISIBLE
            }
        })
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("Back button pressed")
                // Code that you need to execute on back press, e.g. finish()
                val returnIntent = Intent()
                returnIntent.putExtra("activityName", "Inventory")
                if (binding.manageStockSwitch.isChecked == false) {
                    returnIntent.putExtra("manageStockSwitch", "false")
                    returnIntent.putExtra("stockStatus", stockStatusSpinner.selectedItem.toString())
                } else {
                    returnIntent.putExtra("manageStockSwitch", "true")
                    returnIntent.putExtra(
                        "stockQuantity",
                        binding.quantityInputInner.text.toString()
                    )
                }
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        })
    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        val returnIntent = Intent()
        returnIntent.putExtra("activityName", "Inventory")
        if (binding.manageStockSwitch.isChecked == false) {
            returnIntent.putExtra("manageStockSwitch", "false")
            returnIntent.putExtra("stockStatus", stockStatusSpinner.selectedItem.toString())
        } else {
            returnIntent.putExtra("manageStockSwitch", "true")
            returnIntent.putExtra("stockQuantity", binding.quantityInputInner.text.toString())
        }
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }*/

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_stop -> {
                Toasti("Settings Clicked")
            }
        }
        return super.onOptionsItemSelected(item)
    }*/
}