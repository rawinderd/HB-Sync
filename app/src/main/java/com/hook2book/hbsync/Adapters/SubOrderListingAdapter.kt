package com.hook2book.hbsync.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hook2book.hbsync.Model.SubOrderData.Data
import com.hook2book.hbsync.Model.SubOrderData.SubOrderData
import com.hook2book.hbsync.R
import com.hook2book.hbsync.fragments.OrderFragment



class SubOrderListingAdapter(private val itemClickListener: OrderFragment) :
    RecyclerView.Adapter<SubOrderListingAdapter.ViewHolder>() {
    var subOrderList: MutableList<Data> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.model_sub_order, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = subOrderList.get(position)
        holder.subOrderID.text = items.sub_order_id
        holder.subOrderCustName.text = items.first_name
        holder.subOrderDate.text = items.created_at
        holder.subOrderPrice.text = calculateOrderTotal(position).toString()
        holder.subOrderStatus.text = getStatusString(items.order_status.toString())
        holder.publisherId.text = items.pub_id
    }

    private fun calculateOrderTotal(position: Int): Int {
        var orderTotal = 0
        for (i in 0..<subOrderList.get(position).order_detail.size) {
            orderTotal = orderTotal + subOrderList.get(position).order_detail.get(i).price
        }
        Log.i("TAG", "calculateOrderTotal: " + orderTotal)
        return orderTotal
    }

    override fun getItemCount(): Int {/* var size: Int
        if (subOrderList.isEmpty()) {
            size = 0
        } else {
            size = subOrderList.get(0).order_detail.size
        }*/
        return subOrderList.size
        //return size

    }

    interface ItemClickListener {
        fun onItemClick(position: Int, subOrderId: String)
    }

    fun setData(data: SubOrderData?) {
        if (data != null) {
            subOrderList.addAll(data.data_outer.data)
        }
        notifyDataSetChanged()
    }


    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val subOrderID: TextView = itemView.findViewById(R.id.textOrderId_value)
        val subOrderCustName: TextView = itemView.findViewById(R.id.textCustName)
        val subOrderDate: TextView = itemView.findViewById(R.id.textOrderDate)
        val subOrderPrice: TextView = itemView.findViewById(R.id.textOrderAmount)
        val subOrderStatus: TextView = itemView.findViewById(R.id.textOrderStatus)
        val publisherId: TextView = itemView.findViewById(R.id.textPublisherId)

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition >= 0) {
                    itemClickListener.onItemClick(
                        bindingAdapterPosition,
                        subOrderList.get(bindingAdapterPosition).sub_order_id
                    )
                }
            }
        }
    }

    private fun getStatusString(status: String?): String {
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
}