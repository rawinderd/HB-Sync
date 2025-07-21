package com.hook2book.hbsync.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.hook2book.hbsync.Activities.AddProduct
import com.hook2book.hbsync.Adapters.ProductListAdapter
import com.hook2book.hbsync.EnumClasses.ApiStatus
import com.hook2book.hbsync.Model.SearchProduct.Data
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.Preferences
import com.hook2book.hbsync.ViewModels.ProductListViewModel
import com.hook2book.hbsync.databinding.FragmentProductsBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment(), ProductListAdapter.ItemClickListener {

    private lateinit var binding: FragmentProductsBinding
    private var searchString = ""
    private var pubId: String = ""
    private var productStatus = ""
    var isScrolling: Boolean = false
    var pageNo: Int = 0
    var totalPages: Int? = 0
    var totalProducts: Int? = 0
    lateinit var searchBox: TextView
    var textErasedStatus: Boolean = false
    var keyword: String = "None"
    lateinit var adapter: ProductListAdapter
    lateinit var productListViewModel: ProductListViewModel
    private var productListLocal: MutableList<Data> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_products, container, false)
        val recyclerview = view.findViewById<RecyclerView>(R.id.productsRecycler)
        pubId = (Preferences.loadAccountInfo(context)).data_outer.get(0).sku_initial
        recyclerview.layoutManager = LinearLayoutManager(context)
        adapter = ProductListAdapter(this, productListLocal)
        recyclerview.adapter = adapter
        productListViewModel =
            ViewModelProvider(requireActivity()).get(ProductListViewModel::class.java)
        binding = FragmentProductsBinding.bind(view)
        searchBox = view.findViewById(R.id.product_search_input_inner)
        val dialog: AlertDialog = AlertDialog.Builder(requireContext()).setTitle("Loading Products")
            .setMessage("Please wait while we load your products.").setCancelable(false).create()
        dialog.show()
        fetchProducts()
        Log.i(
            "TAG",
            "fetchProducts: called main " + keyword + " " + pubId + " " + productStatus + " " + pageNo
        )
        binding.fabBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireActivity(), AddProduct::class.java)
            intent.putExtra("type", "newProduct")
            startActivity(intent)
        })
        binding.pullToRefreshProducts?.setOnRefreshListener(OnRefreshListener {
            refreshData() // your code
            binding.pullToRefreshProducts?.isRefreshing = false
        })
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = adapter.itemCount
                val visibleItemCount = recyclerView.childCount
                val firstVisibleItem =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (isScrolling && (totalItemCount - visibleItemCount <= firstVisibleItem)) {
                    isScrolling = false
                    if (totalPages == 1) {
                        Toast.makeText(
                            context, "End of Products by page 1", Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (pageNo == totalPages) {
                            //if (pageNo == totalPages?.plus(1)) {
                            Toast.makeText(
                                context, "End of Products", Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context, "Loading More Products ", Toast.LENGTH_SHORT
                            ).show()
                            fetchProducts()
                            Log.i(
                                "TAG",
                                "fetchProducts: called from scroll " + keyword + " " + pubId + " " + productStatus + " " + pageNo + " " + totalPages
                            )
                            // dialog.show()
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (start >= 2) {
                    textErasedStatus = true
                    val textlistener: TextListener = TextListener(lifecycle)
                    textlistener.textListenerFunction(
                        s.toString(), lifecycle
                    ) { newText ->
                        newText?.let {
                            if (it.isEmpty()) {
                                Log.i("TAG", "afterTextChanged: Search Reset")
                            } else {
                                Log.i("TAG", "afterTextChanged: Search on " + it)
                                resetData()
                                keyword = it
                                fetchProducts()
                                Log.i(
                                    "TAG",
                                    "fetchProducts: called from search>2 " + keyword + " " + pubId + " " + productStatus + " " + pageNo + " " + totalPages
                                )
                            }
                        }
                    }
                }
                if ((start == 1 || start == 0) && textErasedStatus == true) {
                    resetData()
                    fetchProducts()
                    textErasedStatus = false
                    Log.i(
                        "TAG",
                        "fetchProducts: called from none case senario " + keyword + " " + pubId + " " + productStatus + " " + pageNo + " " + totalPages
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        productListViewModel.getProductList().observe(viewLifecycleOwner, Observer {
            when (it.status) {

                ApiStatus.SUCCESS -> {
                    dialog.dismiss()
                    adapter.setData(it.data?.data_outer?.data)
                    totalPages = it.data?.data_outer?.last_page
                    totalProducts = it.data?.data_outer?.total
                    Log.i("TAG", "fetchProducts onCreateView: total pages Updated " + totalPages+" total products "+totalProducts)
                    Preferences.saveProductCount(context, totalProducts.toString())
                }
                ApiStatus.ERROR -> {
                    dialog.dismiss()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    val str="{\"message\":\"Product not found\"}"
                    Log.i("tag", "onCreateView: "+str)
                    if (it.message.equals("{\"message\":\"Product not found\"}")) {
                        Preferences.saveProductCount(context, "0")
                        Log.i("TAG", "fetchProducts onCreateView: total pages Updated 0")
                    }
                }
                ApiStatus.LOADING -> {

                }
            }
        })
        return view
    }

    private fun resetData() {
        pageNo = 0
        productListLocal.clear()
        keyword = "None"
        adapter.clearList()
        totalPages = 0
        Log.i(
            "TAG",
            "fetchProducts: called  reset data   " + keyword + " " + pubId + " " + productStatus + " " + pageNo + " " + totalPages
        )
    }


    private fun refreshData() {
        pageNo = 0
        totalPages = 0
        productListLocal.clear()
        adapter.clearList()
        keyword = "None"
        binding.productSearchInputInner.text?.clear()/* Log.i(
            "TAG",
            "fetchProducts: called refresh data1  " + keyword + " " + pubId + " " + productStatus + " " + pageNo + " " + totalPages
        )*/
        fetchProducts()/*Log.i(
            "TAG",
            "fetchProducts: called refresh data2  " + keyword + " " + pubId + " " + productStatus + " " + pageNo + " " + totalPages
        )*/

    }

    private fun fetchProducts() {

        Log.i(
            "TAG",
            "fetchProducts: called main function " + keyword + " " + pubId + " " + productStatus + " " + pageNo + " " + totalPages
        )
        pageNo++
        pubId = (Preferences.loadAccountInfo(context)).data_outer.get(0).sku_initial
        productListViewModel.fetchProducts(keyword, pubId, productStatus, pageNo)

        // resetVars()
    }

    override fun onItemClick(position: Int, id: Int?) {
        val intent = Intent(requireActivity(), AddProduct::class.java)
        intent.putExtra("type", "oldProduct")
        intent.putExtra("productId", id.toString())
        startActivity(intent)
    }

    internal class TextListener(lifecycle: Lifecycle) {
        var debouncePeriod: Long = 2000
        val coroutineScope = lifecycle.coroutineScope
        var searchJob: Job? = null
        fun textListenerFunction(
            newText: String?, lifecycle: Lifecycle, onDebouncingQueryTextChange: (String?) -> Unit
        ) {
            searchJob?.cancel()
            Log.i("TAG", "fetchProducts textListenerFunction: job cancelled")
            searchJob = coroutineScope.launch {
                newText?.let {
                    delay(debouncePeriod)
                    onDebouncingQueryTextChange(newText)
                }
            }
        }
    }
}


