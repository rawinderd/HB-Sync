package com.hook2book.hbsync.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hook2book.hbsync.Adapters.ProductCategoriesAdapter
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.databinding.ActivityProductCategoriesBinding
import com.sikhreader.Adapters.ProductTagsAdapter
import com.sikhreader.Model.Categories.CategoriesForListing
import com.sikhreader.Model.Categories.RemovedCategoryChipMain
import com.sikhreader.ViewModels.ProductCategoriesViewModel
import java.io.Serializable

class ProductCategories : BaseActivity(), ProductTagsAdapter.ItemClickListener {
    private lateinit var binding: ActivityProductCategoriesBinding
    lateinit var productCategoriesViewModel: ProductCategoriesViewModel
    private var parentCategories: MutableList<CategoriesForListing> = ArrayList()
    private var childCategories: MutableList<CategoriesForListing> = ArrayList()

    //private var ParentChildCategories: MutableList<CategoriesForListing> = ArrayList()
    private var ParentChildCategoriesMix: MutableList<CategoriesForListing> = ArrayList()
    private var productCategoriesLocal: MutableList<CategoriesForListing> = ArrayList()
    private var removedCategorylist: MutableList<RemovedCategoryChipMain> = ArrayList()
    lateinit var adapter: ProductCategoriesAdapter
    private lateinit var chipGroup: ChipGroup
    lateinit var recyclerview: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecycler()
        chipGroup = findViewById<ChipGroup>(R.id.chip_group_product_categories)
        chipGroup.setOnClickListener(View.OnClickListener {
            Toasti(it.toString())
        })
        val toolbar: Toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Categories", false)
        productCategoriesViewModel =
            ViewModelProvider(this).get(ProductCategoriesViewModel::class.java)
        fetchCategories()
        productCategoriesViewModel.getCategoriesList().observe(this, Observer {
            if (it.data?.size!! > 0) {
                for (i in 0..it.data.size - 1) {
                    if (it.data.get(i).parent != 1751) {
                        if (it.data.get(i).parent == 0) {
                            parentCategories.add(
                                CategoriesForListing(
                                    it.data.get(i), "parent", 0, ""
                                )
                            )
                            //ParentChildCategories.add(CategoriesForListing(it.data.get(i), "parent", 1, ""))
                        } else {
                            childCategories.add(
                                CategoriesForListing(
                                    it.data.get(i), "Child", 1, ""
                                )
                            )
                            //ParentChildCategories.add(CategoriesForListing(it.data.get(i), "child", 1, ""))
                        }
                    }
                }
                for (i in 0..parentCategories.size - 1) {
                    ParentChildCategoriesMix.add(
                        CategoriesForListing(
                            parentCategories.get(i).categoryDetail, "yes", 0, "root"
                        )
                    )
                    for (j in 0..childCategories.size - 1) {
                        if (parentCategories.get(i).categoryDetail.id == childCategories.get(j).categoryDetail.parent) {
                            ParentChildCategoriesMix.add(
                                CategoriesForListing(
                                    childCategories.get(j).categoryDetail,
                                    "no",
                                    parentCategories.get(i).categoryDetail.id,
                                    parentCategories.get(i).categoryDetail.name
                                )
                            )
                        }
                    }
                }
                ParentChildCategoriesMix.sortedWith(compareBy({ it.parentName },
                    { it.categoryDetail.parent })
                )
                productCategoriesLocal.addAll(ParentChildCategoriesMix)
                adapter.setData(ParentChildCategoriesMix)
            }
        })
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val returnIntent = Intent()
                val bCategoryNames = Bundle()
                if (removedCategorylist.isNotEmpty()) {
                    bCategoryNames.putSerializable(
                        "categories", removedCategorylist as Serializable
                    )
                    returnIntent.putExtra("categoriesBundle", bCategoryNames)
                    setResult(Activity.RESULT_OK, returnIntent)
                }
                finish()
            }
        })
    }

    private fun setUpRecycler() {
        recyclerview = findViewById<RecyclerView>(R.id.tags_recycler)
        adapter = ProductCategoriesAdapter(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }

    private fun fetchCategories() {
        productCategoriesViewModel.fetchCategories()
    }

    override fun onItemClick(position: Int) {
        chipGroup.addChip(this, productCategoriesLocal.get(position), position)
        RemoveCategoryFromLocalAndAdaprterProductCategoryList(position)
    }

    fun RemoveCategoryFromLocalAndAdaprterProductCategoryList(position: Int) {
        productCategoriesLocal.removeAt(position)
        adapter.RemoveFromAdapter(position)
    }

    fun addRemovedChipToCategoryList(categoriesForListing: CategoriesForListing, position: Int) {
        productCategoriesLocal.add(position, categoriesForListing)
        Toasti("Position " + position)
        adapter.addCategorytoCategoryList(position, categoriesForListing)
    }


    fun ChipGroup.addChip(
        context: Context, categoriesForListing: CategoriesForListing, position: Int
    ) {
        Chip(context).apply {
            id = View.generateViewId()
            text = categoriesForListing.categoryDetail.name
            this.setOnCloseIconClickListener {
                chipGroup.removeView(this)
                addRemovedChipToMainCategoryList(id)
            }
            isClickable = true
            isCheckable = true
            isCloseIconVisible = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
            addToRemovedCategoryList(id, categoriesForListing, position)
        }
    }

    private fun addToRemovedCategoryList(
        id: Int, categoriesForListing: CategoriesForListing, tagPosition: Int
    ) {
        removedCategorylist.add(RemovedCategoryChipMain(id, categoriesForListing, tagPosition))
    }

    private fun addRemovedChipToMainCategoryList(id: Int) {
        for (i in 0..removedCategorylist.size - 1) {
            if (removedCategorylist.get(i).ChipPosition == id) {
                addRemovedChipToCategoryList(
                    removedCategorylist.get(i).categoriesForListing,
                    removedCategorylist.get(i).CategoryPosition
                )
            }
        }
    }
}