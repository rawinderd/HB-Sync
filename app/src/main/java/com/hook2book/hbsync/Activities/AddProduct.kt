package com.hook2book.hbsync.Activities


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.hook2book.hbsync.EnumClasses.ApiStatus
import com.hook2book.hbsync.Model.Categories.RemovedCategoryChipMain


import com.hook2book.hbsync.Model.NewTag.tagArray
import com.hook2book.hbsync.Model.ProductTags.RemovedTagMain
import com.hook2book.hbsync.Model.SingleProduct.ProductMainSingle
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.UtilityClass.Preferences
import com.hook2book.hbsync.ViewModels.AddProductViewModel
import com.hook2book.hbsync.databinding.ActivityAddProductBinding


class AddProduct : BaseActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private var tagLoadingCounter: Int = 3
    private lateinit var height: String
    private lateinit var width: String
    private lateinit var length: String
    private lateinit var toolbar: Toolbar
    val dimen = arrayOf("#", "#", "#")
    lateinit var saveShareBtn: Button
    var productDataLocal: ProductMainSingle = ProductMainSingle()
    var fetchedProductData: ProductMainSingle = ProductMainSingle()
    var cate: MutableList<RemovedCategoryChipMain> = ArrayList()
    var tagArrayLocal: MutableList<tagArray> = ArrayList()
    lateinit var addProductViewModel: AddProductViewModel
    var tagsList: MutableList<RemovedTagMain> = ArrayList()
    private lateinit var productType: String
    private lateinit var productId: String
    private lateinit var currentState: String
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        setSupportActionBar(findViewById(R.id.home_toolbar))
        productDataLocal.data.book_name = binding.bookNameTextView.text.toString()
        productDataLocal.data.pub_seller_id =
            Preferences.loadAccountInfo(applicationContext).data_outer.get(0).sku_initial
        if (intent.getStringExtra("type") == "newProduct") {
            productType = "newProduct"
            initTextChanger()
            hideAllViews()
            renameForNewActivity()
            setUpSKU()
            currentState = "1"
            inStockSetUp()
            setToolbar(toolbar, false, "Name of The Book", true)
        } else {
            if (intent.getStringExtra("type") == "oldProduct") {
                productId = (intent.getStringExtra("productId")).toString()
                productType = "oldProduct"
                hideAllViews()
                if (productId != null) {
                    dialog.show()
                    addProductViewModel.fetchSingleProductData(productId)
                }
            }
        }
        addProductViewModel.addSingleProductResPonse().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    dialog.dismiss()
                    fetchedProductData = it.data!!
                    SetDataAndHideEmptyViews(it.data)
                    initTextChanger()
                    currentState = it.data.data.status.toString()
                }
                ApiStatus.ERROR -> {
                    dialog.dismiss()
                    Toasti(it.message)
                }
                ApiStatus.LOADING -> {
                    TODO()
                }
            }
        }
        saveShareBtn.setOnClickListener(View.OnClickListener {
            if (saveShareBtn.text.equals("Save")) {
                if (productType.equals("newProduct")) {
                    if (checkImportentConstraints()) {
                        dialog.show()
                        addProductViewModel.addProductToDB1(mapProductDataToJsonObject())
                    }
                } else {
                    if (checkImportentConstraints()) {
                        dialog.show()
                        addProductViewModel.updateProduct(mapProductDataToJsonObject(), productId)
                        if (currentState.equals("3")) {
                            if (!productDataLocal.data.wc_product_id.isNullOrBlank()) {
                                dialog.show()//this condition is to stop product immediately from selling
                                addProductViewModel.updateStatusInWC(
                                    "pending", fetchedProductData.data.wc_product_id.toString()
                                )
                            }
                        }
                    }
                }
            }
        })
        addProductViewModel.addProductResPonse().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    dialog.dismiss()
                    Toasti("Product Inserted Successfully")
                    saveShareBtn.text = "SHARE"
                    Preferences.saveProductCount(applicationContext, (Preferences.loadProductCount(applicationContext)) + 1
                    )
                }
                ApiStatus.ERROR -> {
                    dialog.dismiss()
                    Toasti(it.message)
                }
                ApiStatus.LOADING -> TODO()
            }
        }
        //function Repeated
        addProductViewModel.updateProductResPonse().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    dialog.dismiss()
                    //Toasti("Product Updated Successfully Pre Table")
                    refreshPage(productId)
                    saveShareBtn.text = "Share"
                }

                ApiStatus.ERROR -> {
                    dialog.dismiss()
                    Toasti(it.message)
                }
                ApiStatus.LOADING -> TODO()
            }
        }
        addProductViewModel.updateStatusInWCResponse().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    dialog.dismiss()
                    Toasti("Product Updated Successfully")
                    refreshPage(productId)
                }
                ApiStatus.ERROR -> {
                    dialog.dismiss()
                }
                ApiStatus.LOADING -> TODO()
            }
        }

        addProductViewModel.updateStatusInPreResponse().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    dialog.dismiss()
                    Toasti("Product Updated Successfully")
                    refreshPage(productId)
                }
                ApiStatus.ERROR -> {
                    dialog.dismiss()
                    Toasti("Product Not Updated")
                }
                ApiStatus.LOADING -> TODO()
            }
        }


        binding.bookNameTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                UpdateToolBarText(toolbar, binding.bookNameTextView.text.toString())
                productDataLocal.data.book_name = binding.bookNameTextView.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //TODO("Not yet implemented")
            }
        })


        var launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (RESULT_OK != result.resultCode) {
                    Toasti("Result not ok")
                } else {
                    val intent = result.data
                    if (intent == null) {
                        Toast.makeText(
                            this, "Activity hasn't returned an intent", Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (intent.hasExtra("newPrice")) {
                            Log.i("TAG", "Returned: " + intent.getStringExtra("newPrice"))
                            Toasti("Returned " + intent.getStringExtra("newPrice"))
                        }
                        if (intent.getStringExtra("activityName") == "BasicInfo") {
                            if (intent.hasExtra("isbn")) {
                                if (intent.getStringExtra("isbn").equals("empty")) {
                                    binding.productIsbnText.visibility = View.GONE
                                    binding.productIsbnTextview.visibility = View.GONE
                                } else {
                                    binding.productIsbnText.visibility = View.VISIBLE
                                    binding.productIsbnTextview.visibility = View.VISIBLE
                                    binding.productIsbnTextview.text = intent.getStringExtra("isbn")
                                    productDataLocal.data.isbn = intent.getStringExtra("isbn")
                                }

                            }
                            if (intent.hasExtra("writerName")) {
                                if (intent.getStringExtra("writerName").equals("empty")) {
                                    binding.productBasicWriterName.visibility = View.GONE
                                    binding.productBasicWriterNameTextview.visibility = View.GONE
                                } else {
                                    binding.productBasicWriterName.visibility = View.VISIBLE
                                    binding.productBasicWriterNameTextview.visibility = View.VISIBLE
                                    binding.productBasicWriterNameTextview.text =
                                        intent.getStringExtra("writerName")
                                    productDataLocal.data.writer_name =
                                        intent.getStringExtra("writerName")!!
                                    searchTagToAddForWriterName(
                                        intent.getStringExtra("writerName").toString()
                                    )
                                }
                            }
                            if (intent.hasExtra("pages")) {
                                if (intent.getStringExtra("pages").equals("empty")) {
                                    binding.productBasicPages.visibility = View.GONE
                                    binding.productBasicPagesTextview.visibility = View.GONE
                                } else {
                                    binding.productBasicPages.visibility = View.VISIBLE
                                    binding.productBasicPagesTextview.visibility = View.VISIBLE
                                    binding.productBasicPagesTextview.text =
                                        intent.getStringExtra("pages")
                                    productDataLocal.data.pages = intent.getStringExtra("pages")!!
                                }

                            }
                            if (intent.hasExtra("language")) {
                                if (intent.getStringExtra("language").equals("empty")) {
                                    binding.productBasicLanguage.visibility = View.GONE
                                    binding.productBasicLanguageTextview.visibility = View.GONE
                                } else {
                                    binding.productBasicLanguage.visibility = View.VISIBLE
                                    binding.productBasicLanguageTextview.visibility = View.VISIBLE
                                    binding.productBasicLanguageTextview.text =
                                        intent.getStringExtra("language")
                                    productDataLocal.data.language =
                                        intent.getStringExtra("language")!!
                                    searchTagToAddLanguage(
                                        intent.getStringExtra("language").toString()
                                    )
                                }

                            }
                            if (intent.hasExtra("radioButton")) {
                                if (intent.getStringExtra("radioButton").equals("empty")) {
                                    binding.productBasicFormat.visibility = View.GONE
                                    binding.productBasicFormatTextview.visibility = View.GONE
                                } else {
                                    binding.productBasicFormat.visibility = View.VISIBLE
                                    binding.productBasicFormatTextview.visibility = View.VISIBLE
                                    binding.productBasicFormatTextview.text =
                                        intent.getStringExtra("radioButton")
                                    productDataLocal.data.format =
                                        intent.getStringExtra("radioButton")!!
                                }

                            }
                        }
                        if (intent.getStringExtra("activityName") == "ShippingInfo") {
                            if (intent.hasExtra("weight")) {
                                if (intent.getStringExtra("weight").equals("empty")) {
                                    binding.productShippingWeight.visibility = View.GONE
                                    binding.productShippingWeightTextview.visibility = View.GONE
                                } else {
                                    binding.productShippingWeight.visibility = View.VISIBLE
                                    binding.productShippingWeightTextview.visibility = View.VISIBLE
                                    binding.productShippingWeightTextview.text =
                                        intent.getStringExtra("weight")
                                    productDataLocal.data.weight = intent.getStringExtra("weight")!!
                                }

                            }
                            if (intent.hasExtra("length")) {
                                if (intent.getStringExtra("length").equals("empty")) {
                                    binding.productShippingDimensions.visibility = View.GONE
                                    binding.productShippingDimensionsTextview.visibility = View.GONE

                                } else {
                                    binding.productShippingDimensions.visibility = View.VISIBLE
                                    binding.productShippingDimensionsTextview.visibility =
                                        View.VISIBLE

                                    length = intent.getStringExtra("length").toString()
                                    makeDimenstionFunction(
                                        intent.getStringExtra("length").toString(), 0
                                    )
                                    productDataLocal.data.length = intent.getStringExtra("length")!!
                                }

                            }
                            if (intent.hasExtra("width")) {
                                if (intent.getStringExtra("width").equals("empty")) {
                                    binding.productShippingDimensions.visibility = View.GONE
                                    binding.productShippingDimensionsTextview.visibility = View.GONE
                                } else {
                                    binding.productShippingDimensions.visibility = View.VISIBLE
                                    binding.productShippingDimensionsTextview.visibility =
                                        View.VISIBLE
                                    width = intent.getStringExtra("width").toString()
                                    makeDimenstionFunction(
                                        intent.getStringExtra("width").toString(), 1
                                    )
                                    productDataLocal.data.width = intent.getStringExtra("width")!!
                                }

                            }
                            if (intent.hasExtra("height")) {
                                if (intent.getStringExtra("height").equals("empty")) {
                                    binding.productShippingDimensions.visibility = View.GONE
                                    binding.productShippingDimensionsTextview.visibility = View.GONE
                                } else {
                                    binding.productShippingDimensions.visibility = View.VISIBLE
                                    binding.productShippingDimensionsTextview.visibility =
                                        View.VISIBLE
                                    height = intent.getStringExtra("height").toString()
                                    makeDimenstionFunction(
                                        intent.getStringExtra("height").toString(), 2
                                    )
                                    productDataLocal.data.height = intent.getStringExtra("height")!!
                                }
                            }

                            if (intent.hasExtra("shippingClass")) {
                                if (intent.getStringExtra("shippingClass").equals("empty")) {
                                    binding.productShippingClass.visibility = View.GONE
                                    binding.productShippingClassTextview.visibility = View.GONE
                                } else {
                                    binding.productShippingClass.visibility = View.VISIBLE
                                    binding.productShippingClassTextview.visibility = View.VISIBLE
                                    binding.productShippingClassTextview.text =
                                        intent.getStringExtra("shippingClass")
                                    productDataLocal.data.shipping_class =
                                        intent.getStringExtra("shippingClass")!!
                                }

                            }
                        }
                        if (intent.getStringExtra("activityName") == "ProductPrice") {
                            if (intent.hasExtra("regularPrice")) {
                                if (intent.getStringExtra("regularPrice").equals("empty")) {
                                    binding.productRegularPriceTextview.visibility = View.GONE
                                    binding.productRegularPrice.visibility = View.GONE
                                } else {
                                    binding.productRegularPriceTextview.visibility = View.VISIBLE
                                    binding.productRegularPrice.visibility = View.VISIBLE
                                    binding.productRegularPriceTextview.text =
                                        intent.getStringExtra("regularPrice")
                                    productDataLocal.data.price =
                                        intent.getStringExtra("regularPrice")!!
                                }
                            }
                            if (intent.hasExtra("salePrice")) {
                                if (intent.getStringExtra("salePrice").equals("empty")) {
                                    binding.productSalePriceTextview.visibility = View.GONE
                                    binding.productSalePrice.visibility = View.GONE
                                    productDataLocal.data.sale_price = ""
                                } else {
                                    binding.productSalePriceTextview.visibility = View.VISIBLE
                                    binding.productSalePrice.visibility = View.VISIBLE
                                    binding.productSalePriceTextview.text =
                                        intent.getStringExtra("salePrice")
                                    productDataLocal.data.sale_price =
                                        intent.getStringExtra("salePrice")!!
                                }
                            }
                        }

                        if (intent.getStringExtra("activityName") == "ShortDescription") {
                            if (intent.hasExtra("shortDescription")) {
                                if (intent.getStringExtra("salePrice").equals("empty")) {
                                    binding.shortDescriptionTextview.visibility = View.GONE
                                } else {
                                    binding.shortDescriptionTextview.visibility = View.VISIBLE
                                    binding.shortDescriptionTextview.text =
                                        intent.getStringExtra("shortDescription")
                                    productDataLocal.data.short_description =
                                        intent.getStringExtra("shortDescription")!!
                                }
                            }
                        }
                        if (intent.getStringExtra("activityName") == "LongDescription") {
                            if (intent.hasExtra("longDescription")) {
                                if (intent.getStringExtra("longDescription").equals("empty")) {
                                    binding.longDescriptionOfBook.text = "Clear"
                                    // binding.longDescriptionOfBook.visibility = View.GONE
                                } else {
                                    // binding.longDescriptionOfBook.visibility = View.VISIBLE
                                    binding.longDescriptionOfBook.text =
                                        intent.getStringExtra("longDescription")
                                    productDataLocal.data.description =
                                        intent.getStringExtra("longDescription")!!
                                }
                            }
                        }
                        if (intent.hasExtra("tagsBundle")) {
                            val args = intent.getBundleExtra("tagsBundle")
                            val `object` =
                                args!!.getSerializable("tags") as ArrayList<RemovedTagMain>?
                            if (`object` != null) {
                                var tagList: String? = `object`.get(0).tag.name
                                // var tagIds:String?=`object`.get(0).tag.wc_tag_id
                                for (i in 1..`object`.size - 1) {
                                    tagList += ", " + `object`.get(i).tag.name
                                    // tagIds+= ", " + `object`.get(i).tag.wc_tag_id
                                }
                                binding.listOfTags.visibility = View.VISIBLE
                                binding.listOfTags.text = tagList
                                tagsList = `object`
                            }
                        }
                        if (intent.hasExtra("categoriesBundle")) {
                            val args = intent.getBundleExtra("categoriesBundle")
                            val `object` =
                                args!!.getSerializable("categories") as ArrayList<RemovedCategoryChipMain>?
                            if (`object` != null) {
                                var categorylist: String =
                                    `object`.get(0).categoriesForListing.categoryDetail.categoryName
                                // var categoryIds: String = `object`.get(0).categoriesForListing.categoryDetail.id.toString()
                                for (i in 1..`object`.size - 1) {
                                    categorylist += ", " + `object`.get(i).categoriesForListing.categoryDetail.categoryName
                                    //categoryIds+=", "+`object`.get(i).categoriesForListing.categoryDetail.id.toString()
                                }

                                binding.listOfCategories.visibility = View.VISIBLE
                                binding.listOfCategories.text = categorylist
                                cate = `object`
                                searchTagToAddCategory(`object`.get(0).categoriesForListing.categoryDetail.categoryName)

                            }
                        }
                        if (intent.getStringExtra("activityName") == "Inventory") {
                            if (intent.hasExtra("manageStockSwitch")) {
                                if (intent.getStringExtra("manageStockSwitch").equals("true")) {
                                    binding.productStockQuantity.visibility = View.VISIBLE
                                    binding.productStockQuantityTextview.visibility = View.VISIBLE
                                    binding.productStockQuantityTextview.text =
                                        intent.getStringExtra("stockQuantity")
                                    binding.productStockStatus.visibility = View.GONE
                                    binding.productStockStatusTextview.visibility = View.GONE
                                } else {
                                    binding.productStockQuantity.visibility = View.GONE
                                    binding.productStockQuantityTextview.visibility = View.GONE
                                    binding.productStockStatus.visibility = View.VISIBLE
                                    binding.productStockStatusTextview.visibility = View.VISIBLE
                                    if (intent.getStringExtra("stockStatus")!!
                                            .equals("outofstock")) {
                                        if (currentState.equals("1")) {
                                            Toasti("You Cant Out of Stock a Pending Product or New Product")
                                        } else {
                                            binding.productStockStatusTextview.text =
                                                intent.getStringExtra("stockStatus")
                                            productDataLocal.data.stock =
                                                intent.getStringExtra("stockStatus")!!
                                        }
                                    } else {
                                        binding.productStockStatusTextview.text =
                                            intent.getStringExtra("stockStatus")
                                        productDataLocal.data.stock =
                                            intent.getStringExtra("stockStatus")!!
                                    }
                                }
                            }
                        }
                    }
                }
            }

        binding.priceBlock.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ProductPrice::class.java)
            if (!productDataLocal.data.price.isNullOrBlank()) {
                intent.putExtra("regularPrice", productDataLocal.data.price)
            }
            if (!productDataLocal.data.sale_price.isNullOrBlank()) {
                intent.putExtra("salePrice", productDataLocal.data.sale_price)
            }
            launcher.launch(intent)
        })
        binding.basicInfoBlock.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, BasicInfo::class.java)
            if (!productDataLocal.data.isbn.isNullOrBlank()) {
                intent.putExtra("isbn", productDataLocal.data.isbn)
            }
            if (!productDataLocal.data.writer_name.isNullOrBlank()) {
                intent.putExtra("writerName", productDataLocal.data.writer_name)
            }
            if (!productDataLocal.data.pages.isNullOrBlank()) {
                intent.putExtra("pages", productDataLocal.data.pages)
            }
            if (!productDataLocal.data.language.isNullOrBlank()) {
                intent.putExtra("language", productDataLocal.data.language)
            }
            if (!productDataLocal.data.format.isNullOrBlank()) {
                intent.putExtra("radioButton", productDataLocal.data.format)
            }
            launcher.launch(intent)
        })
        binding.inventoryBlock.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Inventory::class.java)
            if (!productDataLocal.data.sku.isNullOrBlank()) {
                intent.putExtra("sku", productDataLocal.data.sku)
            }
            if (!productDataLocal.data.stock.isNullOrBlank()) {
                intent.putExtra("stockStatus", productDataLocal.data.stock)
            }
            launcher.launch(intent)
        })
        binding.shippingBlock.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ShippingInfo::class.java)
            if (!productDataLocal.data.weight.isNullOrBlank()) {
                intent.putExtra("weight", productDataLocal.data.weight)
            }
            if (!productDataLocal.data.length.isNullOrBlank()) {
                intent.putExtra("length", productDataLocal.data.length)
            }
            if (!productDataLocal.data.width.isNullOrBlank()) {
                intent.putExtra("width", productDataLocal.data.width)
            }
            if (!productDataLocal.data.height.isNullOrBlank()) {
                intent.putExtra("height", productDataLocal.data.height)
            }
            if (!productDataLocal.data.shipping_class.isNullOrBlank()) {
                intent.putExtra("shippingClass", productDataLocal.data.shipping_class)
            }
            launcher.launch(intent)
        })/*binding.shortDescriptionBlock.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ShortDescription::class.java)
            launcher.launch(intent)
        })*/
        binding.longDescriptionOfBook.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, LongDescription::class.java)
            if (!productDataLocal.data.description.isNullOrBlank()) {
                intent.putExtra("longDescription", productDataLocal.data.description)
            }
            launcher.launch(intent)
        })
        binding.tagsBlock.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ProductTags2::class.java)
            launcher.launch(intent)
        })
        binding.shortDescriptionBlock.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ShortDescription::class.java)
            if (!productDataLocal.data.short_description.isNullOrBlank()) {
                intent.putExtra("shortDescription", productDataLocal.data.short_description)
            }
            launcher.launch(intent)
        })
        binding.categoriesBlock.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ProductCategories2::class.java)
            launcher.launch(intent)
        })
        binding.productRemarkInputInner.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                productDataLocal.data.remarks = s.toString()
            }
        })
        addProductViewModel.getSearchedWriterTag().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    if (it.data?.size!! > 0) {
                        if (productDataLocal.data.writer_name.equals(it.data.get(0).name)) {
                            //Toasti("Matched name=" + it.data.get(0).name + " id=" + it.data.get(0).id)
                            tagArrayLocal.add(tagArray(it.data.get(0).id, it.data.get(0).name))
                        } else {
                           /* Toasti(
                                "Not Matched name=" + it.data.get(0).name + " id=" + it.data.get(0).id
                            )*/
                            addProductViewModel.createWriterNameTag(productDataLocal.data.writer_name.toString())
                        }

                    } else {
                        addProductViewModel.createWriterNameTag(productDataLocal.data.writer_name.toString())

                    }
                }
                ApiStatus.ERROR -> {

                }
                ApiStatus.LOADING -> TODO()
            }
        }
        addProductViewModel.getNewWriterNameTag().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    tagArrayLocal.add(tagArray(it.data?.id, it.data?.name))
                }
                ApiStatus.ERROR -> {
                    Toasti("Error " + it.message)

                }
                ApiStatus.LOADING -> TODO()
            }
        }
        addProductViewModel.getSearchedTagLanguage().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    if (it.data?.size!! > 0) {
                        if (productDataLocal.data.language.equals(it.data.get(0).name)) {
                            tagArrayLocal.add(tagArray(it.data.get(0).id, it.data.get(0).name))
                        } else {
                            addProductViewModel.createWriterNameTag(productDataLocal.data.writer_name.toString())
                        }

                    } else {
                        addProductViewModel.createWriterNameTag(productDataLocal.data.writer_name.toString())
                    }

                }
                ApiStatus.ERROR -> TODO()
                ApiStatus.LOADING -> TODO()
            }
        }
        addProductViewModel.getSearchedTagCategory().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    if (it.data?.size!! > 0) {
                        if (cate.get(0).categoriesForListing.categoryDetail.categoryName.equals(
                                it.data.get(
                                    0
                                ).name
                            )) {
                            tagArrayLocal.add(tagArray(it.data.get(0).id, it.data.get(0).name))
                        } else {
                            addProductViewModel.createWriterNameTag(productDataLocal.data.writer_name.toString())
                        }

                    } else {
                        addProductViewModel.createWriterNameTag(productDataLocal.data.writer_name.toString())
                    }

                }
                ApiStatus.ERROR -> TODO()
                ApiStatus.LOADING -> TODO()
            }
        }
    }


    private fun refreshPage(productId: String) {

        val intent = Intent(applicationContext, AddProduct::class.java)
        Log.i("TAG", "refreshPage: Called " + productId)
        intent.putExtra("type", "oldProduct")
        intent.putExtra("productId", productId)
        startActivity(intent)

    }


    private fun checkImportentConstraints(): Boolean {
        var allowStatus = false
        if (productDataLocal.data.book_name.isNullOrBlank()) {
            Toasti("Please Enter Book Name")
            allowStatus = false
        } else {
            if (productDataLocal.data.price.isNullOrBlank()) {
                Toasti("Please Enter Book Price")
                allowStatus = false
            } else {
                if (productDataLocal.data.isbn.isNullOrBlank()) {
                    Toasti("Please Enter Book ISBN")
                    allowStatus = false
                } else {
                    if (productDataLocal.data.writer_name.isNullOrBlank()) {
                        Toasti("Please Enter Writer Name")
                        allowStatus = false
                    } else {
                        if (productDataLocal.data.language.isNullOrBlank()) {
                            Toasti("Please Enter Book Language")
                            allowStatus = false
                        } else {
                            if (productType.equals("newProduct")) {
                                if (cate.isEmpty()) {
                                    Toasti("Please Enter Book Category")
                                    allowStatus = false
                                } else {
                                    allowStatus = true
                                }
                            } else {
                                if (binding.listOfCategories.text.isEmpty()) {
                                    Toasti("Please Enter Book Category")
                                    allowStatus = false
                                } else {
                                    if (productDataLocal.data.shipping_class.isNullOrBlank()) {
                                        Toasti(" Enter Shipping Class info.")
                                        allowStatus = false
                                    } else {
                                        allowStatus = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return allowStatus
    }

    private fun SetDataAndHideEmptyViews(data: ProductMainSingle?) {
        if (data != null) {
            productDataLocal = data
            if (!productDataLocal.data.book_name.isNullOrBlank()) {
                binding.bookNameTextView.setText(productDataLocal.data.book_name.toString())
            } else {
                // Product.addProperty("book_name", (productDataLocal.dataInput.bookName))
            }
            if (!productDataLocal.data.writer_name.isNullOrBlank()) {
                binding.productBasicWriterName.visibility = View.VISIBLE
                binding.productBasicWriterNameTextview.visibility = View.VISIBLE
                binding.productBasicWriterNameTextview.text = (productDataLocal.data.writer_name)
            }
            if (!productDataLocal.data.isbn.isNullOrBlank()) {
                binding.productIsbnText.visibility = View.VISIBLE
                binding.productIsbnTextview.visibility = View.VISIBLE
                binding.productIsbnTextview.text = productDataLocal.data.isbn
            }
            if (!productDataLocal.data.pages.isNullOrBlank()) {
                binding.productBasicPages.visibility = View.VISIBLE
                binding.productBasicPagesTextview.visibility = View.VISIBLE
                binding.productBasicPagesTextview.text = productDataLocal.data.pages
            }

            if (!productDataLocal.data.language.isNullOrBlank()) {
                binding.productBasicLanguage.visibility = View.VISIBLE
                binding.productBasicLanguageTextview.visibility = View.VISIBLE
                binding.productBasicLanguageTextview.text = productDataLocal.data.language
            }

            if (!productDataLocal.data.format.isNullOrBlank()) {
                binding.productBasicFormat.visibility = View.VISIBLE
                binding.productBasicFormatTextview.visibility = View.VISIBLE
                binding.productBasicFormatTextview.text = productDataLocal.data.format
            }

            if (!productDataLocal.data.price.isNullOrBlank()) {
                binding.productRegularPriceTextview.visibility = View.VISIBLE
                binding.productRegularPrice.visibility = View.VISIBLE
                binding.productRegularPriceTextview.text = productDataLocal.data.price
            }
            if (!productDataLocal.data.sale_price.isNullOrBlank()) {
                binding.productSalePriceTextview.visibility = View.VISIBLE
                binding.productSalePrice.visibility = View.VISIBLE
                binding.productSalePriceTextview.text = productDataLocal.data.sale_price.toString()
            }

            //Product.addProperty("wc_product_id", "11") // we will not send this one
            if (!productDataLocal.data.description.isNullOrBlank()) {
                binding.longDescriptionOfBook.text = productDataLocal.data.description
            }
            if (!productDataLocal.data.stock.isNullOrBlank()) {
                binding.productStockStatus.visibility = View.VISIBLE
                binding.productStockStatusTextview.visibility = View.VISIBLE
                binding.productStockStatusTextview.text = productDataLocal.data.stock
            }
            if (!productDataLocal.data.short_description.isNullOrBlank()) {
                binding.shortDescriptionTextview.visibility = View.VISIBLE
                binding.shortDescriptionTextview.text = productDataLocal.data.short_description
            }/*if(!productDataLocal.data.productType .isNullOrBlank()) {
                Product.addProperty("product_type", (productDataLocal.dataInput.productType))
            }*/

            if (!productDataLocal.data.weight.isNullOrBlank()) {
                binding.productShippingWeightTextview.text = productDataLocal.data.weight
            }

            if (!productDataLocal.data.length.isNullOrBlank()) {
                makeDimenstionFunction(productDataLocal.data.length.toString(), 0)
            }
            if (!productDataLocal.data.width.isNullOrBlank()) {
                makeDimenstionFunction(productDataLocal.data.width.toString(), 1)
            }
            if (!productDataLocal.data.height.isNullOrBlank()) {
                makeDimenstionFunction(productDataLocal.data.height.toString(), 2)
            }
            if (!productDataLocal.data.shipping_class.isNullOrBlank()) {
                binding.productShippingClass.visibility = View.VISIBLE
                binding.productShippingClassTextview.visibility = View.VISIBLE
                binding.productShippingClassTextview.text = productDataLocal.data.shipping_class
            }
            if (!productDataLocal.data.status.isNullOrBlank()) {

                binding.productStatusText.text =
                    getStatusString(productDataLocal.data.status.toString())
            }
            if (!productDataLocal.data.remarks.isNullOrBlank()) {
                binding.productRemarkPrintText.text = productDataLocal.data.remarks
            }
            if (!productDataLocal.data.sku.isNullOrBlank()) {
                binding.productSkuTextview.visibility = View.VISIBLE
                binding.productSku.visibility = View.VISIBLE
                binding.productSkuTextview.text = productDataLocal.data.sku
            }
            if (!productDataLocal.data.category.isNullOrEmpty()) {
                // val cateArray = JsonArray()
                var categorylist: String =
                    productDataLocal.data.category?.get(0)?.category_name.toString()
                for (i in 1..<(productDataLocal.data.category!!.size)) {
                    categorylist += ", " + productDataLocal.data.category?.get(i)?.category_name.toString()
                }
                binding.listOfCategories.visibility = View.VISIBLE
                binding.listOfCategories.text = categorylist
            }
            if (!productDataLocal.data.tags.isNullOrEmpty()) {
                var tagList: String = productDataLocal.data.tags?.get(0)?.tag_name.toString()
                for (i in 1..<productDataLocal.data.tags!!.size) {
                    tagList += ", " + productDataLocal.data.tags!!.get(i).tag_name.toString()
                }
                binding.listOfTags.visibility = View.VISIBLE
                binding.listOfTags.text = tagList
            }
        }

    }

    private fun getStatusString(status: String): String {
        var returnvalue: String = String()
        when (status) {
            "1" -> returnvalue = "Pending"
            "2" -> returnvalue = "Resubmission"
            "3" -> returnvalue = "Approved"
            "4" -> returnvalue = "Rejected"
            "5" -> returnvalue = "Out of Stock"
            "6" -> returnvalue = "Stopped"
        }
        return returnvalue
    }

    private fun getStatusNumber(status: String): String {
        var returnvalue: String = String()
        when (status) {
            "Pending" -> returnvalue = "1"
            "Resubmission" -> returnvalue = "2"
            "Approved" -> returnvalue = "3"
            "Rejected" -> returnvalue = "4"
            "Out of Stock" -> returnvalue = "5"
            "Stopped" -> returnvalue = "6"
        }
        return returnvalue
    }

    // to send to server
    private fun mapProductDataToJsonObject(): JsonObject {
        val Product = JsonObject()
        val tagArrayforJson = JsonArray()
        Product.addProperty("visibility", "visible")
        var decString: String
        if (productType.equals("newProduct")) {
            decString =
                productDataLocal.data.book_name + " by " + productDataLocal.data.writer_name + " " + cate.get(
                    0
                ).categoriesForListing.categoryDetail.categoryName + " book in " + productDataLocal.data.language
        } else {
            decString =
                productDataLocal.data.book_name + " by " + productDataLocal.data.writer_name + " " + productDataLocal.data.category?.get(
                    0
                )?.category_name + " book in " + productDataLocal.data.language
        }
        Product.addProperty("short_description", decString)
        if (productDataLocal.data.book_name.equals("Name of The Book", true)) {
            Toasti("Enter Name of the Book")
        } else {
            Product.addProperty("book_name", (productDataLocal.data.book_name))
        }
        if (!productDataLocal.data.writer_name.isNullOrBlank()) {
            Product.addProperty("writer_name", (productDataLocal.data.writer_name))
        }
        if (!productDataLocal.data.pub_seller_id.isNullOrBlank()) {
            Product.addProperty("pub_seller_id", productDataLocal.data.pub_seller_id)
        }
        if (!productDataLocal.data.isbn.isNullOrBlank()) {
            Product.addProperty("isbn", (productDataLocal.data.isbn))
        }
        if (!productDataLocal.data.pages.isNullOrBlank()) {
            Product.addProperty("pages", (productDataLocal.data.pages))
        }
        if (!productDataLocal.data.language.isNullOrBlank()) {
            Product.addProperty("language", (productDataLocal.data.language))
        }
        if (!productDataLocal.data.format.isNullOrBlank()) {
            Product.addProperty("format", (productDataLocal.data.format))
        }
        if (!productDataLocal.data.price.isNullOrBlank()) {
            Product.addProperty("price", (productDataLocal.data.price))
        }

        if (!productDataLocal.data.sale_price.isNullOrBlank()) {
            Product.addProperty("sale_price", (productDataLocal.data.sale_price))
        } else {
            Product.addProperty("sale_price", "")
        }
        if (!productDataLocal.data.description.isNullOrBlank()) {
            Product.addProperty("description", productDataLocal.data.description)
        }
        if (!productDataLocal.data.sku.isNullOrBlank()) {
            Product.addProperty("sku", (productDataLocal.data.sku))
        }
        if (!productDataLocal.data.stock.isNullOrBlank()) {
            Product.addProperty("stock", (productDataLocal.data.stock))
        }
        if (!productDataLocal.data.short_description.isNullOrBlank()) {
            Product.addProperty("short_description", (productDataLocal.data.short_description))
        }
        if (!productDataLocal.data.product_type.isNullOrBlank()) {
            Product.addProperty("product_type", (productDataLocal.data.product_type))
        }
        if (!productDataLocal.data.weight.isNullOrBlank()) {
            Product.addProperty("weight", (productDataLocal.data.weight))
        }
        if (!productDataLocal.data.height.isNullOrBlank()) {
            Product.addProperty("height", (productDataLocal.data.height))
        }
        if (!productDataLocal.data.width.isNullOrBlank()) {
            Product.addProperty("width", (productDataLocal.data.width))
        }
        if (!productDataLocal.data.length.isNullOrBlank()) {
            Product.addProperty("length", (productDataLocal.data.length))
        }
        if (!productDataLocal.data.shipping_class.isNullOrBlank()) {
            Product.addProperty("shipping_class", (productDataLocal.data.shipping_class))
        }
        if (productType.equals("newProduct")) {
            Product.addProperty("status", "1") //pending
        } else {
            if (currentState.equals("1")) {
                Product.addProperty("status", "1") //pending
            } else {
                if (currentState.equals("2") || currentState.equals("4")) {
                    if (productDataLocal.data.stock.equals("outofstock")) {
                        Product.addProperty("status", "5")   //out of Stock
                    } else {
                        Product.addProperty("status", "2")   //Resubmission
                    }
                } else {
                    if (currentState.equals("3")) {
                        if (productDataLocal.data.stock.equals("outofstock")) {
                            Product.addProperty("status", "5")   //out of Stock
                            //WC stop functionality
                        } else {
                            Product.addProperty("status", "2")   //Resubmission
                            //WC stop functionality
                        }
                    } else {
                        if (currentState.equals("5")) {
                            if (productDataLocal.data.stock.equals("instock")) {
                                Product.addProperty("status", "2")   //out of Stock
                            } else {
                                Product.addProperty("status", "2")   //Resubmission
                            }
                        } else {
                            if (currentState.equals("6")) {
                                if (productDataLocal.data.stock.equals("outofstock")) {
                                    Product.addProperty("status", "5")   //out of Stock
                                } else {
                                    Product.addProperty("status", "2")   //Resubmission
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!productDataLocal.data.visibility.isNullOrBlank()) {
            Product.addProperty("visibility", "Visible")  // hard coded
        }

        var statusString = String()
        statusString = "Last Status=" + getStatusString(productDataLocal.data.status.toString())
        if (!productDataLocal.data.remarks.isNullOrBlank()) {
            statusString = statusString + " \n" + binding.productRemarkInputInner.text
            Product.addProperty("remarks", statusString)
        } else {
            Product.addProperty("remarks", statusString)
        }

        if (!productDataLocal.data.related.isNullOrBlank()) {
            Product.addProperty("related", "None")  // Hard COded
        }

        Product.addProperty("user_type", 1)
        if (cate.isNotEmpty()) {
            val cateArray = JsonArray()
            for (i in 0..cate.size - 1) {
                val categories = JsonObject()
                categories.addProperty(
                    "name", cate.get(i).categoriesForListing.categoryDetail.categoryName
                )
                categories.addProperty(
                    "wc_category_id", cate.get(i).categoriesForListing.categoryDetail.id

                )
                cateArray.add(categories)
            }
            Product.add("categories", cateArray)
        }
        if (tagsList.isNotEmpty() || tagArrayLocal.isNotEmpty()) {
            if (tagsList.isNotEmpty()) {

                for (i in 0..tagsList.size - 1) {
                    val categories = JsonObject()
                    categories.addProperty(
                        "name", tagsList.get(i).tag.name
                    )
                    categories.addProperty(
                        "wc_tag_id", tagsList.get(i).tag.id
                    )
                    tagArrayforJson.add(categories)
                }
            }
            if (tagArrayLocal.isNotEmpty()) {
                for (i in 0..tagArrayLocal.size - 1) {
                    val categories = JsonObject()
                    categories.addProperty(
                        "name", tagArrayLocal.get(i).tagName
                    )
                    categories.addProperty(
                        "wc_tag_id", tagArrayLocal.get(i).tagId
                    )
                    tagArrayforJson.add(categories)
                }
            }
            Product.add("tags", tagArrayforJson)
        }
        return Product
    }

    private fun initComponents() {
        dialog = DialogGen()
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        saveShareBtn = findViewById(R.id.toolbar_share_save_button)
        toolbar = findViewById(R.id.toolbar_for_products)

    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (saveShareBtn.text.equals("Save")) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to discard your Changes?")
            builder.setPositiveButton("KEEP EDITING") { dialog, id -> }
            builder.setNegativeButton(
                "DISCARD"
            ) { dialog, id -> super.onBackPressed() }
            builder.show()
        } else {
            finish()
        }

    }

    private fun UpdateToolBarText(toolbar: Toolbar, title: String) {
        updateToolBarText(toolbar, title)
    }

    private fun SaveBeforeLeave() {
        saveShareBtn.text = "Save"
    }


    private fun hideEmptyViews() {

    }

    private fun makeDimenstionFunction(oneDimen: String, index: Int) {
        dimen.set(index, oneDimen)
        var dimenString = ""
        for (i in 0..2) {
            if (dimen[i] != "#") {
                if (dimenString.isEmpty()) {
                    dimenString = dimen[i] + "cm "
                } else {
                    dimenString = dimenString + " x " + dimen[i] + "cm"
                }
            }
        }
        binding.productShippingDimensionsTextview.text = dimenString
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_stop -> {
                if (productType.equals("newProduct")) {
                    Toasti("You Cant Stop a new Product")
                } else {
                    var status = "6"
                    if (productDataLocal.data.status == "1" || productDataLocal.data.status == "5" || productDataLocal.data.status == "2")       // for pending, out of stock and resubmission
                    {
                        dialog.show()
                        addProductViewModel.updateStatusInPre(status, productId)
                    } else {
                        if (productDataLocal.data.status == "3")   // for approved products
                        {
                            dialog.show()
                            addProductViewModel.updateStatusInPre(status, productId)
                            addProductViewModel.updateStatusInWC(
                                "pending", productDataLocal.data.wc_product_id.toString()
                            )
                           // Toasti(productDataLocal.data.wc_product_id.toString())
                        } else {
                            if (productDataLocal.data.status == "6") {
                                if (productDataLocal.data.wc_product_id.isNullOrBlank()) {
                                    dialog.show()
                                    addProductViewModel.updateStatusInPre(
                                        "1", productId
                                    )     //pending
                                } else {
                                    if (productDataLocal.data.stock.equals("outofstock")) {
                                        dialog.show()
                                        addProductViewModel.updateStatusInPre(
                                            "5", productId
                                        )   // out of stock
                                    } else {
                                        dialog.show()
                                        addProductViewModel.updateStatusInPre(
                                            "2", productId
                                        )   // Resubmission
                                    }
                                }
                            }
                        }
                    }
                }

            }
            R.id.action_trash -> {/*if (productType.equals("newProduct")) {
                    Toasti("Not Applicable on new Product")
                } else {
                    var intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                }*/
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTextWatcherTextView(textView: TextView): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence, i: Int, i1: Int, i2: Int
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                SaveBeforeLeave()
            }
        }
    }

    private fun getTextWatcherEditText(editText: EditText): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence, i: Int, i1: Int, i2: Int
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                SaveBeforeLeave()
            }
        }
    }

    private fun initTextChanger() {
        binding.bookNameTextView.addTextChangedListener(getTextWatcherEditText(binding.bookNameTextView))
        binding.productRegularPriceTextview.addTextChangedListener(
            getTextWatcherTextView(
                binding.productRegularPriceTextview
            )
        )
        binding.productSalePriceTextview.addTextChangedListener(getTextWatcherTextView(binding.productSalePriceTextview))
        binding.productIsbnTextview.addTextChangedListener(getTextWatcherTextView(binding.productIsbnTextview))
        binding.productBasicWriterNameTextview.addTextChangedListener(
            getTextWatcherTextView(
                binding.productBasicWriterNameTextview
            )
        )
        binding.productBasicPagesTextview.addTextChangedListener(getTextWatcherTextView(binding.productBasicPagesTextview))
        binding.productBasicFormatTextview.addTextChangedListener(getTextWatcherTextView(binding.productBasicFormatTextview))
        binding.productBasicLanguageTextview.addTextChangedListener(
            getTextWatcherTextView(
                binding.productBasicLanguageTextview
            )
        )
        binding.productSkuTextview.addTextChangedListener(getTextWatcherTextView(binding.productSkuTextview))
        binding.productStockQuantityTextview.addTextChangedListener(
            getTextWatcherTextView(
                binding.productStockQuantityTextview
            )
        )
        binding.productStockStatusTextview.addTextChangedListener(getTextWatcherTextView(binding.productStockStatusTextview))
        binding.productBackordersTextview.addTextChangedListener(getTextWatcherTextView(binding.productBackordersTextview))
        binding.listOfCategories.addTextChangedListener(getTextWatcherTextView(binding.listOfCategories))
        binding.listOfTags.addTextChangedListener(getTextWatcherTextView(binding.listOfTags))
        binding.shortDescriptionTextview.addTextChangedListener(getTextWatcherTextView(binding.shortDescriptionTextview))
        binding.longDescriptionOfBook.addTextChangedListener(getTextWatcherTextView(binding.longDescriptionOfBook))


        binding.productShippingWeightTextview.addTextChangedListener(
            getTextWatcherTextView(
                binding.productShippingWeightTextview
            )
        )
        binding.productShippingDimensionsTextview.addTextChangedListener(
            getTextWatcherTextView(
                binding.productShippingDimensionsTextview
            )
        )
        binding.productShippingClassTextview.addTextChangedListener(
            getTextWatcherTextView(
                binding.productShippingClassTextview
            )
        )
        binding.productRemarkInputInner.addTextChangedListener(getTextWatcherEditText(binding.productRemarkInputInner))
    }

    private fun hideAllViews() {
        binding.productRegularPrice.visibility = View.GONE
        binding.productRegularPriceTextview.visibility = View.GONE
        binding.productSalePrice.visibility = View.GONE
        binding.productSalePriceTextview.visibility = View.GONE
        binding.productIsbnText.visibility = View.GONE
        binding.productIsbnTextview.visibility = View.GONE
        binding.productBasicWriterName.visibility = View.GONE
        binding.productBasicWriterNameTextview.visibility = View.GONE
        binding.productBasicPages.visibility = View.GONE
        binding.productBasicPagesTextview.visibility = View.GONE
        binding.productBasicFormat.visibility = View.GONE
        binding.productBasicFormatTextview.visibility = View.GONE
        binding.productBasicLanguage.visibility = View.GONE
        binding.productBasicLanguageTextview.visibility = View.GONE
        binding.productSku.visibility = View.GONE
        binding.productSkuTextview.visibility = View.GONE
        binding.productStockQuantity.visibility = View.GONE
        binding.productStockQuantityTextview.visibility = View.GONE
        binding.productStockStatus.visibility = View.GONE
        binding.productStockStatusTextview.visibility = View.GONE
        binding.productBackorders.visibility = View.GONE
        binding.productBackordersTextview.visibility = View.GONE
        binding.listOfCategories.visibility = View.GONE
        binding.listOfTags.visibility = View.GONE
        binding.shortDescriptionTextview.visibility = View.GONE
        binding.productShippingWeight.visibility = View.GONE
        binding.productShippingWeightTextview.visibility = View.GONE
        binding.productShippingDimensions.visibility = View.GONE
        binding.productShippingDimensionsTextview.visibility = View.GONE
        binding.productShippingClass.visibility = View.GONE
        binding.productShippingClassTextview.visibility = View.GONE
    }

    private fun setUpSKU() {
        binding.productSku.visibility = View.VISIBLE
        binding.productSkuTextview.visibility = View.VISIBLE
        var sku: String
        var productCount: String
        if (Preferences.loadProductCount(applicationContext).isNullOrBlank()) {
            productCount = "1"
            Log.i("TAG", "setUpSKU: Product Count: " + productCount)
        } else {
            productCount =
                ((Preferences.loadProductCount(applicationContext).toInt()) + 1).toString()
            Log.i("TAG", "setUpSKU: Product Count: " + productCount)
        }
        sku =
            "HB-" + (Preferences.loadAccountInfo(applicationContext)).data_outer.get(0).sku_initial + "-Pub-" + productCount
        binding.productSkuTextview.text = sku
        productDataLocal.data.sku = sku
    }

    private fun inStockSetUp() {
        binding.productStockStatus.visibility = View.VISIBLE
        binding.productStockStatusTextview.text = "instock"
        binding.productStockStatusTextview.visibility = View.VISIBLE
        productDataLocal.data.stock = "instock"
    }

    private fun renameForNewActivity() {
        binding.productPriceText.text = "Add Price"
        binding.productBasicInfoText.text = "Add Basic Info"
        binding.productInventoryText.text = "Add Inventory"
        binding.categoryText.text = "Add Categories"
        binding.tagsText.text = "Add Tags"
        binding.shortDescriptionText.text = "Add Short Description"
        binding.productShippingText.text = "Add Shipping"
    }


    private fun searchTagToAddForWriterName(keyword: String) {
        addProductViewModel.searchWriterTagToAdd(keyword, 1)
    }

    private fun searchTagToAddLanguage(keyword: String) {
        addProductViewModel.searchTagToAddLangauage(keyword, 1)
    }

    private fun searchTagToAddCategory(keyword: String) {
        addProductViewModel.searchTagToAddCategory(keyword, 1)
    }
}