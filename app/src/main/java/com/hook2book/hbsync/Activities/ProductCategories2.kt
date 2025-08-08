package com.hook2book.hbsync.Activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hook2book.hbsync.Adapters.ProductCategoriesAdapter2
import com.hook2book.hbsync.Adapters.ProductTagsAdapter
import com.hook2book.hbsync.Model.Categories.RemovedCategoryChipMain
import com.hook2book.hbsync.Model.NewTag.CategoriesForListing2
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.ViewModels.ProductCategoriesViewModel
import com.hook2book.hbsync.databinding.ActivityProductCategoriesBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Serializable

class ProductCategories2 : BaseActivity(), ProductTagsAdapter.ItemClickListener {
    private lateinit var binding: ActivityProductCategoriesBinding
    lateinit var productCategoriesViewModel: ProductCategoriesViewModel
    private var parentCategories: MutableList<CategoriesForListing2> = ArrayList()
    private var childCategories: MutableList<CategoriesForListing2> = ArrayList()
    private var ParentChildCategoriesMix: MutableList<CategoriesForListing2> = ArrayList()
    private var productCategoriesLocal: MutableList<CategoriesForListing2> = ArrayList()
    private var removedCategorylist: MutableList<RemovedCategoryChipMain> = ArrayList()
    lateinit var adapter: ProductCategoriesAdapter2
    private lateinit var chipGroup: ChipGroup
    lateinit var recyclerview: RecyclerView
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecycler()
        dialog = DialogGen()
        chipGroup = findViewById<ChipGroup>(R.id.chip_group_product_categories)
        chipGroup.setOnClickListener(View.OnClickListener {
            Toasti(it.toString())
        })
        val toolbar: Toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, true, "Categories", false)
        productCategoriesViewModel =
            ViewModelProvider(this).get(ProductCategoriesViewModel::class.java)

        GlobalScope.launch {
            productCategoriesViewModel.getLocalCategories()
        }
        productCategoriesViewModel.fetchLocalCategories().observe(this, Observer {
            if (it.size > 0) {
                dialog.dismiss()
                for (i in 0..it.size - 1) {
                    if (it.get(i).categoryParent != 206) {
                        if (it.get(i).categoryParent == 0) {
                            parentCategories.add(
                                CategoriesForListing2(
                                    it.get(i), "parent", 0, ""
                                )
                            )
                        } else {
                            childCategories.add(
                                CategoriesForListing2(
                                    it.get(i), "Child", 1, ""
                                )
                            )
                        }
                    }
                }
                for (i in 0..parentCategories.size - 1) {
                    Log.i(
                        "TAG",
                        "onCreate: Parent Categories " + parentCategories.get(i).categoryDetail.categoryName
                    )
                }
                for (i in 0..parentCategories.size - 1) {
                    ParentChildCategoriesMix.add(
                        CategoriesForListing2(
                            parentCategories.get(i).categoryDetail, "yes", 0, "root"
                        )
                    )
                    for (j in 0..childCategories.size - 1) {
                        if (parentCategories.get(i).categoryDetail.id == childCategories.get(j).categoryDetail.categoryParent) {
                            ParentChildCategoriesMix.add(
                                CategoriesForListing2(
                                    childCategories.get(j).categoryDetail,
                                    "no",
                                    parentCategories.get(i).categoryDetail.id,
                                    parentCategories.get(i).categoryDetail.categoryName
                                )
                            )
                        }
                    }
                }
                ParentChildCategoriesMix.sortedWith(
                    compareBy({ it.parentName }, { it.categoryDetail.categoryParent })
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
                    setResult(RESULT_OK, returnIntent)
                }
                finish()
            }
        })
    }


    private fun setUpRecycler() {
        recyclerview = findViewById<RecyclerView>(R.id.tags_recycler)
        adapter = ProductCategoriesAdapter2(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }

    private fun fetchCategories() {
        dialog.show()
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

    fun addRemovedChipToCategoryList(categoriesForListing: CategoriesForListing2, position: Int) {
        productCategoriesLocal.add(position, categoriesForListing)
        //Toasti("Position " + position)
        adapter.addCategorytoCategoryList(position, categoriesForListing)
    }


    fun ChipGroup.addChip(
        context: Context, categoriesForListing: CategoriesForListing2, position: Int
    ) {
        Chip(context).apply {
            id = View.generateViewId()
            text = categoriesForListing.categoryDetail.categoryName
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
        id: Int, categoriesForListing: CategoriesForListing2, tagPosition: Int
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