package com.hook2book.hbsync.Activities
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.databinding.ActivityBasicInfoBinding

class BasicInfo : BaseActivity() {
    private lateinit var binding: ActivityBasicInfoBinding
    private lateinit var radioButtonValue: String
    lateinit var spinner: Spinner
    lateinit var rbPaperBack: RadioButton
    lateinit var rbHardBound: RadioButton
    lateinit var rbGorup: RadioGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Basic Info", false)
        val languages = resources.getStringArray(R.array.Languages)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, languages)
        spinner = findViewById(R.id.language_spinner)
        rbPaperBack = findViewById(R.id.rb_paper_back)
        rbHardBound = findViewById(R.id.rb_hard_bound)
        rbGorup = findViewById(R.id.format_radio_button)
        radioButtonValue = "none"
        if (spinner != null) {
            spinner.adapter = adapter
            spinner.setSelection(3, true)
        }
        if (intent.hasExtra("isbn")) {
            binding.isbnInputInner.setText(intent.getStringExtra("isbn"))
        }
        if (intent.hasExtra("writerName")) {
            binding.writerNameInputInner.setText(intent.getStringExtra("writerName"))
        }
        if (intent.hasExtra("pages")) {
            binding.pagesInputInner.setText(intent.getStringExtra("pages"))
        }
        if (intent.hasExtra("language")) {
            val spinnderPosition = languages.indexOf(intent.getStringExtra("language"))
            spinner.post(Runnable { spinner.setSelection(spinnderPosition) })
        }
        if (intent.hasExtra("radioButton")) {
            if (rbPaperBack.text.equals(intent.getStringExtra("radioButton"))) {
                rbPaperBack.isChecked = true
                radioButtonValue = "Paper Back"
            } else {
                rbHardBound.isChecked = true
                radioButtonValue = "Hard Bound"
            }
        }
        binding.formatRadioButton.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            radioButtonValue = radio.text.toString()
            Toast.makeText(
                applicationContext, " On checked change :" + " ${radio.text}", Toast.LENGTH_SHORT
            ).show()
        })

        if (spinner != null) {
            spinner.adapter = adapter
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("Back button pressed")
                // Code that you need to execute on back press, e.g. finish()
                val returnIntent = Intent()
                returnIntent.putExtra("activityName", "BasicInfo")
                if (!binding.isbnInputInner.text.toString().isEmpty()) {
                    Log.i("TAG", "onBackPressed: isbn " + binding.isbnInputInner.text.toString())
                    returnIntent.putExtra(
                        "isbn", binding.isbnInputInner.text.toString()
                    )
                } else {
                    returnIntent.putExtra(
                        "isbn", "empty"
                    )
                }
                if (!binding.writerNameInputInner.text.toString().isEmpty()) {
                    returnIntent.putExtra(
                        "writerName", binding.writerNameInputInner.text.toString()
                    )
                } else {
                    returnIntent.putExtra(
                        "writerName", "empty"
                    )
                }
                if (!binding.pagesInputInner.text.toString().isEmpty()) {
                    returnIntent.putExtra(
                        "pages", binding.pagesInputInner.text.toString()
                    )
                } else {
                    returnIntent.putExtra(
                        "pages", "empty"
                    )
                }
                if (spinner.selectedItem.toString() != "None") {
                    returnIntent.putExtra(
                        "language", spinner.selectedItem.toString()
                    )
                } else {
                    returnIntent.putExtra(
                        "language", "empty"
                    )
                }
                if (radioButtonValue != "none") {
                    returnIntent.putExtra(
                        "radioButton", radioButtonValue
                    )
                } else {
                    returnIntent.putExtra(
                        "radioButton", "Paper Back"
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
        returnIntent.putExtra("activityName", "BasicInfo")
        if (!binding.isbnInputInner.text.toString().isEmpty()) {
            Log.i("TAG", "onBackPressed: isbn "+binding.isbnInputInner.text.toString())
            returnIntent.putExtra(
                "isbn", binding.isbnInputInner.text.toString()
            )
        } else {
            returnIntent.putExtra(
                "isbn", "empty"
            )
        }
        if (!binding.writerNameInputInner.text.toString().isEmpty()) {
            returnIntent.putExtra(
                "writerName", binding.writerNameInputInner.text.toString()
            )
        } else {
            returnIntent.putExtra(
                "writerName", "empty"
            )
        }
        if (!binding.pagesInputInner.text.toString().isEmpty()) {
            returnIntent.putExtra(
                "pages", binding.pagesInputInner.text.toString()
            )
        } else {
            returnIntent.putExtra(
                "pages", "empty"
            )
        }
        if (spinner.getSelectedItem().toString() != "None") {
            returnIntent.putExtra(
                "language", spinner.getSelectedItem().toString()
            )
        } else {
            returnIntent.putExtra(
                "language", "empty"
            )
        }
        if (radioButtonValue != "none") {
            returnIntent.putExtra(
                "radioButton", radioButtonValue
            )
        } else {
            returnIntent.putExtra(
                "radioButton", "Paper Back"
            )
        }
        setResult(Activity.RESULT_OK, returnIntent)
        finish()

    }*/
}