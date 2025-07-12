package com.hook2book.hbsync.Activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hook2book.hbsync.Adapters.ProductTagsAdapter
import com.hook2book.hbsync.EnumClasses.ApiStatus
import com.hook2book.hbsync.Model.NewTag.newTag
import com.hook2book.hbsync.Model.ProductTags.ProductTagsMain
import com.hook2book.hbsync.Model.ProductTags.RemovedTagMain
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.ViewModels.ProductTagsViewModel
import com.hook2book.hbsync.databinding.ActivityProductTagsBinding

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable


class ProductTags2 : BaseActivity(), ProductTagsAdapter.ItemClickListener {
    private lateinit var binding: ActivityProductTagsBinding
    lateinit var productTagLocal: MutableList<ProductTagsMain>
    lateinit var ChippedTagList: MutableList<RemovedTagMain>
    private lateinit var chipGroup: ChipGroup
    lateinit var adapter: ProductTagsAdapter
    lateinit var productTagsViewModel: ProductTagsViewModel
    lateinit var recyclerview: RecyclerView
    var pageNo = 1
    var totalPages: Int? = 0
    var isScrolling = false
    lateinit var addTag: TextView
    var keyword: String = "None"
    var textErasedStatus: Boolean = false
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductTagsBinding.inflate(layoutInflater)
        productTagLocal = ArrayList()
        ChippedTagList = mutableListOf()
        productTagsViewModel = ViewModelProvider(this).get(ProductTagsViewModel::class.java)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Tags", false)
        setUpRecycler()
        dialog = DialogGen()
        addTag = findViewById(R.id.add_tag_input_inner)
        addTag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (start >= 2) {
                    textErasedStatus = true
                    val textlistener: TextListener = TextListener(this@ProductTags2.lifecycle)
                    textlistener.textListenerFunction(
                        s.toString(), this@ProductTags2.lifecycle
                    ) { newText ->
                        newText?.let {
                            if (it.isEmpty()) {
                                Log.i("TAG", "afterTextChanged: Search Reset")

                                //viewModel.resetSearch()
                            } else {
                                Log.i("TAG", "afterTextChanged: Search on " + it)
                                //viewModel.searchMovies(it)
                                keyword = it
                                resetData()
                                productTagsViewModel.fetchTagList(keyword, 1)
                                dialog.show()
                            }
                        }
                    }
                }
                if ((start == 1 || start == 0) && textErasedStatus == true) {
                    resetData()
                    keyword = "None"
                    textErasedStatus = false
                    productTagsViewModel.fetchTagList(keyword, 1)
                    dialog.show()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        chipGroup = findViewById<ChipGroup>(R.id.chip_group_product_tags)
        chipGroup.setOnClickListener(View.OnClickListener {
            Toasti(it.toString())
        })

        adapter.setData(productTagLocal)
        productTagsViewModel.fetchTagList(keyword, 1)
        dialog.show()
        productTagsViewModel.getTagList().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    dialog.dismiss()
                    adapter.setData(it.data)
                    totalPages = (it.totalPages)?.toIntOrNull()
                    productTagLocal.addAll(it.data!!)
                    if (it.data.size <= 0) {
                        binding.addTagBtn.isEnabled = true
                    }
                }

                ApiStatus.ERROR -> Toasti(it.message)
                ApiStatus.LOADING -> TODO()
            }
        }
        binding.addTagBtn.setOnClickListener {
            productTagsViewModel.createNewTag(binding.addTagInputInner.text.toString())
        }
        productTagsViewModel.getNewTag().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    chipGroup.addChipSearch(this, it.data!!)
                }
                ApiStatus.ERROR -> {
                    Toasti("Error: " + it.message)


                }
                ApiStatus.LOADING -> TODO()
            }
        }

        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = adapter.itemCount
                val visibleItemCount = recyclerView.childCount
                val firstVisibleItem =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
                if (isScrolling && totalItemCount - visibleItemCount <= firstVisibleItem) {
                    isScrolling = false
                    if (totalPages == 1) {
                        Toasti("End of Tags by page 1")
                    } else {
                        if (pageNo == (totalPages?.plus(1))) {
                            Toasti("End of Tags")
                        } else {
                            ToastiShort("Loading More Tags ")
                            fetchTags()
                        }
                    }
                }
            }
        })
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val returnIntent = Intent()
                val b = Bundle()
                if (ChippedTagList.isNotEmpty()) {
                    b.putSerializable("tags", ChippedTagList as Serializable)
                    returnIntent.putExtra("tagsBundle", b)
                    setResult(Activity.RESULT_OK, returnIntent)
                }
                finish()
            }
        })
    }

    private fun fetchTags() {
        dialog.show()
        pageNo = pageNo + 1
        productTagsViewModel.fetchTagList(keyword, pageNo)
    }

    private fun resetData() {
        pageNo = 0
        adapter.clearList()
        productTagLocal.clear()
        // make local lists reset also , but with precuatoioins
    }

    private fun setUpRecycler() {
        recyclerview = findViewById<RecyclerView>(R.id.tags_recycler)
        adapter = ProductTagsAdapter(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }

    internal class TextListener(lifecycle: Lifecycle) {
        var debouncePeriod: Long = 1000
        val coroutineScope = lifecycle.coroutineScope
        var searchJob: Job? = null
        fun textListenerFunction(
            newText: String?, lifecycle: Lifecycle, onDebouncingQueryTextChange: (String?) -> Unit
        ) {
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                newText?.let {
                    delay(debouncePeriod)
                    onDebouncingQueryTextChange(newText)
                }
            }
        }
    }

    override fun onItemClick(position: Int) {
        chipGroup.addChip(this, position, "new")
    }

    fun ChipGroup.addChip(context: Context, position: Int, inputType: String) {
        Chip(context).apply {                       // chip add code
            id = View.generateViewId()
            text = productTagLocal.get(position).name
            this.setOnCloseIconClickListener {                  // chip close code
                chipGroup.removeView(this)
                addRemovedChipToMainTagListPositionFinder(id)
            }
            isClickable = true
            isCheckable = true
            isCloseIconVisible = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
            addToChippedTagList(id, position)
        }
    }

    fun ChipGroup.addChipSearch(context: Context, tagInfo: newTag) {
        Chip(context).apply {
            id = View.generateViewId()
            text = tagInfo.name
            this.setOnCloseIconClickListener {                  // chip close code
                chipGroup.removeView(this)
                //addRemovedChipToMainTagListPositionFinder(id)
            }
            isClickable = true
            isCheckable = true
            isCloseIconVisible = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
            addToChippedTagList(id, 0)
        }
    }


    private fun UpdateChipIdInChippedList(id: Int, position: Int) {
        ChippedTagList.get(position).ChipPosition = id
    }

    private fun addToChippedTagList(id: Int, TagPositionInTagList: Int) {
        ChippedTagList.add(
            RemovedTagMain(
                id, productTagLocal.get(TagPositionInTagList), TagPositionInTagList
            )
        )
        productTagLocal.removeAt(TagPositionInTagList)
        adapter.RemoveFromAdapter(TagPositionInTagList)
    }

    private fun addRemovedChipToMainTagListPositionFinder(id: Int) {
        for (i in 0..ChippedTagList.size - 1) {
            Log.i("TAG", "PositionFinder local list: " + ChippedTagList.get(i).tag.name + " i=" + i)
        }
        for (i in 0..ChippedTagList.size - 1) {
            Log.i(
                "TAG",
                "PositionFinder: chip position " + ChippedTagList.get(i).ChipPosition + " " + id + " Tag position " + ChippedTagList.get(
                    i
                ).TagPosition + " i Position " + i + " Name-> " + ChippedTagList.get(i).tag.name
            )
            if (ChippedTagList.get(i).ChipPosition == id) {
                addRemovedChipToTagList(i, ChippedTagList.get(i).TagPosition)
                break
            }
        }

    }

    fun addRemovedChipToTagList(
        positionOfTagData: Int, TagPositionInTagList: Int
    ) {       // add deleted chip back to list of Tags
        productTagLocal.add(TagPositionInTagList, ChippedTagList.get(positionOfTagData).tag)
        adapter.addTagtoTagList(TagPositionInTagList, ChippedTagList.get(positionOfTagData).tag)
        ChippedTagList.removeAt(positionOfTagData)
    }
}