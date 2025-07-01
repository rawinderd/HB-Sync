package com.hook2book.hbsync.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.databinding.ActivitySubOrderDetailBinding
import com.sikhreader.Adapters.SubOrderDetailAdapter
import com.sikhreader.Model.SingleSubOrderDetail.SingleSubOrderDetail
import com.sikhreader.ViewModels.SubOrderDetailViewModel

class SubOrderDetail : BaseActivity(), SubOrderDetailAdapter.ItemClickListener {

    private lateinit var binding: ActivitySubOrderDetailBinding
    private lateinit var subOrderDetailViewModel: SubOrderDetailViewModel
    private lateinit var orderStatusSpinner: Spinner
    private var subOrderDetailsLocal: SingleSubOrderDetail = SingleSubOrderDetail(
        data_outer = emptyList(), message = "", status = ""
    )
    lateinit var adapter: SubOrderDetailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.home_subOrderDetail)
        setToolbar(toolbar, true, "Orders", false)
        subOrderDetailViewModel = ViewModelProvider(this).get(SubOrderDetailViewModel::class.java)
        if (intent.hasExtra("subOrderId")) {
            subOrderDetailViewModel.fetchSingleSubOrder(
                intent.getStringExtra("subOrderId").toString()
            )
        }
        val recyclerview = findViewById<RecyclerView>(R.id.rvBookList)
        adapter = SubOrderDetailAdapter(this, subOrderDetailsLocal)
        recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        recyclerview.adapter = adapter
        subOrderDetailViewModel.getSubOrderDetail().observe(this) {
            subOrderDetailsLocalForDisplay(it.data)
            subOrderDetailsLocal = (it.data!!)
            adapter.setData(it.data)
        }
        val orderStatus = resources.getStringArray(R.array.orderStatus)
        orderStatusSpinner = findViewById(R.id.order_status_spinner)
        if (orderStatusSpinner != null) {
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, orderStatus)
            orderStatusSpinner.adapter = adapter
        }

        binding.submitOrderStatusBtn.setOnClickListener(View.OnClickListener {
            /*for (i in 0 until subOrderDetailsLocal.data_outer.get(0).order_detail.size) {
                Log.i(
                    "TAG",
                    "onCreate: ${i} - ${subOrderDetailsLocal.data_outer.get(0).order_detail.get(i).sub_order_product_id} - ${
                        subOrderDetailsLocal.data_outer.get(0).order_detail.get(i).status
                    }"
                )
            }*/
            var jsonArray = JsonArray()

            for (i in 0 until subOrderDetailsLocal.data_outer.get(0).order_detail.size) {
                var jsonObject = JsonObject()
                Log.i(
                    "TAG",
                    "onCreate: ${i} - ${subOrderDetailsLocal.data_outer.get(0).order_detail.get(i).sub_order_product_id} - ${
                        subOrderDetailsLocal.data_outer.get(0).order_detail.get(i).status
                    }"
                )
                if (subOrderDetailsLocal.data_outer.get(0).order_detail.get(i).status == 2) {
                    jsonObject.addProperty(
                        "status", subOrderDetailsLocal.data_outer.get(0).order_detail.get(i).status
                    )
                    jsonObject.addProperty(
                        "id",
                        subOrderDetailsLocal.data_outer.get(0).order_detail.get(i).sub_order_product_id
                    )
                    jsonArray.add(jsonObject)
                }
            }

            Log.e("Success Rawinder201", Gson().toJson(jsonArray))


            var trackCode: String = ""
            var courierCompany: String = ""
            var date: String = ""
            var status: String = ""

            if (binding.trackCodeInner.text?.isNotEmpty() == true) {

                trackCode = binding.trackCodeInner.text.toString()
            }
            if (binding.courierCompanyInner.text?.isNotEmpty() == true) {

                courierCompany = binding.courierCompanyInner.text.toString()
            }
            if (binding.dateInner.text?.isNotEmpty() == true) {

                date = binding.dateInner.text.toString()
            }
            status = getStatusNumber(binding.orderStatusSpinner.selectedItem.toString())
            subOrderDetailViewModel.submitOrderStatus(
                subOrderDetailsLocal.data_outer.get(0).sub_order_id,
                trackCode,
                courierCompany,
                date,
                status
            )
            subOrderDetailViewModel.submitProductStatus(jsonArray)
        })
    }

    private fun subOrderDetailsLocalForDisplay(data: SingleSubOrderDetail?) {
        if (data != null) {
            binding.tvSubOrderIdValue.text = data.data_outer.get(0).sub_order_id
            val name = data.data_outer.get(0).first_name + " " + data.data_outer.get(0).last_name
            binding.tvCustomerAddressName.text = name
            binding.tvCustomerAddressAddress1.text = data.data_outer.get(0).address_1
            binding.tvCustomerAddressAddress2.text = data.data_outer.get(0).address_2
            val cityPin = data.data_outer.get(0).city + " " + data.data_outer.get(0).postcode
            binding.tvCustomerAddressCityPin.text = cityPin
            val stateCountry = data.data_outer.get(0).state + " " + data.data_outer.get(0).country
            binding.tvCustomerAddressStateCountry.text = stateCountry
            binding.tvCustomerAddressMobile.text = data.data_outer.get(0).phone
            binding.orderStatusSpinner.setSelection((data.data_outer.get(0).order_status) - 1)/*if(!data.data_outer.get(0).order_status.toString().equals("1")) {
               binding.orderStatusSpinner.isEnabled = false
                binding.trackCodeInner.isEnabled = false
                binding.courierCompanyInner.isEnabled = false
                binding.dateInner.isEnabled = false
                binding.submitOrderStatusBtn.visibility = View.GONE
            }*/

            if (!data.data_outer.get(0).track_code.isNullOrBlank()) {
                binding.trackCodeInner.setText(data.data_outer.get(0).track_code)
            }
            if (!data.data_outer.get(0).shipping_company.isNullOrBlank()) {
                binding.courierCompanyInner.setText(data.data_outer.get(0).shipping_company)
            }
            if (!data.data_outer.get(0).shipping_date.isNullOrBlank()) {
                binding.dateInner.setText(data.data_outer.get(0).shipping_date)
            }


        }
    }

    override fun onItemClick(position: Int, state: Boolean) {
        //Toasti("Position: $position, State: $state")
        if (state.equals(true)) {
            subOrderDetailsLocal.data_outer.get(0).order_detail.get(position).status = 2

        } else {
            subOrderDetailsLocal.data_outer.get(0).order_detail.get(position).status = 1
        }
    }

    private fun getStatusString(status: String): String {
        var returnvalue: String = String()
        when (status) {
            "1" -> returnvalue = "Pending"
            "2" -> returnvalue = "Shipped"
            "3" -> returnvalue = "Partially Shipped"
            "4" -> returnvalue = "Failed"
            "5" -> returnvalue = "Completed"

        }
        return returnvalue
    }

    private fun getStatusNumber(status: String): String {
        var returnvalue: String = String()
        when (status) {
            "Pending" -> returnvalue = "1"
            "Shipped" -> returnvalue = "2"
            "Partially Shipped" -> returnvalue = "3"
            "Failed" -> returnvalue = "4"
            "Completed" -> returnvalue = "5"
        }
        return returnvalue
    }


    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_sub_order_detail)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }*/
}