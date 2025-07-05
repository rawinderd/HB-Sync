package com.hook2book.hbsync.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hook2book.hbsync.Activities.SubOrderDetail
import com.hook2book.hbsync.Adapters.SubOrderListingAdapter
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.Preferences
import com.hook2book.hbsync.ViewModels.PublisherOrderViewModel
import com.hook2book.hbsync.databinding.FragmentOrderBinding

class OrderFragment : Fragment(), SubOrderListingAdapter.ItemClickListener {
    private lateinit var binding: FragmentOrderBinding
    private lateinit var publisherOrderViewModel: PublisherOrderViewModel
    lateinit var adapter: SubOrderListingAdapter
    var offset: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_order, container, false)
        binding = FragmentOrderBinding.inflate(layoutInflater)
        val recyclerview = view.findViewById<RecyclerView>(R.id.recycler_sub_orders)
        adapter = SubOrderListingAdapter(this)
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.adapter = adapter
        publisherOrderViewModel = ViewModelProvider(this).get(PublisherOrderViewModel::class.java)
        publisherOrderViewModel.fetchSubOrders(Preferences.loadAccountInfo(context).data_outer.get(0).sku_initial)
        publisherOrderViewModel.getSubOrders().observe(viewLifecycleOwner) {
            // Toasti(it.data?.data_outer?.data?.get(0)?.first_name)
            //Toasti(it.data?.data_outer?.data?.get(0)?.last_name)
            adapter.setData(it.data)

        }
        return view
    }

    override fun onItemClick(position: Int, subOrderId: String) {
        val intent = Intent(context, SubOrderDetail::class.java)
        intent.putExtra("subOrderId", subOrderId.toString())
        startActivity(intent)
        //Toast.makeText(context, "Position "+position,Toast.LENGTH_SHORT).show()
    }
}