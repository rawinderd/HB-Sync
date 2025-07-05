package com.hook2book.hbsync.Adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.hook2book.hbsync.Model.SingleSubOrderDetail.SingleSubOrderDetail
import com.hook2book.hbsync.R

class SubOrderDetailAdapter(
    private val itemClickListener: ItemClickListener,
    private var subOrderDetailsLocal: SingleSubOrderDetail
) : RecyclerView.Adapter<SubOrderDetailAdapter.SubOrderViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(position: Int, state: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubOrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.model_ordered_products, parent, false)
        return SubOrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        var size = 0
        if (subOrderDetailsLocal.data_outer.isEmpty()) {
            return 0
        } else {
            if (subOrderDetailsLocal.data_outer.get(0).order_detail.isEmpty()) {
                size = 0
            } else {
                size = subOrderDetailsLocal.data_outer.get(0).order_detail.size
            }
            return size
        }
    }

    override fun onBindViewHolder(holder: SubOrderViewHolder, position: Int) {
        Log.i("TAG", "onBindViewHolder: Position " + position)
        val itemsViewModel = subOrderDetailsLocal.data_outer.get(0).order_detail.get(position)
        holder.productId.text = itemsViewModel.sku
        holder.productName.text = itemsViewModel.name
        val quantity = itemsViewModel.quantity.toString() + " x "
        holder.productQuantity.text = quantity
        val price = itemsViewModel.price.toString() + " = "
        holder.productPrice.text = price
        holder.productTotal.text = itemsViewModel.total
        holder.productShippingStatus.text = getStatusString(itemsViewModel.status.toString())
        holder.productShippingStatusSwitch.isChecked = !itemsViewModel.status.toString().equals("1")
    }

    fun setData(orderDetail: SingleSubOrderDetail?) {
        if (orderDetail != null) {

            subOrderDetailsLocal = orderDetail
        }
        notifyDataSetChanged()
    }

    inner class SubOrderViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val productId: TextView = itemView.findViewById(R.id.product_id_ordered_product)
        val productName: TextView = itemView.findViewById(R.id.product_name_ordered_product)
        val productPrice: TextView = itemView.findViewById(R.id.product_price_ordered_product)
        val productQuantity: TextView = itemView.findViewById(R.id.product_quantity_ordered_product)
        val productShippingStatus: TextView =
            itemView.findViewById(R.id.product_shipping_status_ordered_product)
        val productTotal: TextView = itemView.findViewById(R.id.product_total_ordered_product)
        val productShippingStatusSwitch: SwitchCompat =
            itemView.findViewById(R.id.product_shipped_switch)


        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition >= 0) {/*itemClickListener.onItemClick(
                        bindingAdapterPosition,
                        orderList.get(bindingAdapterPosition).id
                    )*/
                    Log.i("TAG", "onClick: Position " + bindingAdapterPosition)
                }
            }
            productShippingStatusSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    Log.i("TAG", ":switch on ")
                    productShippingStatus.text = "Shipped"
                    itemClickListener.onItemClick(
                        bindingAdapterPosition, true
                    )
                } else {
                    Log.i("TAG", ":switch off ")
                    productShippingStatus.text = "Pending"
                    itemClickListener.onItemClick(
                        bindingAdapterPosition, false
                    )
                }
            }
        }
    }

    private fun getStatusString(status: String?): String {
        var returnvalue: String = String()
        when (status) {
            "1" -> returnvalue = "Pending"
            "2" -> returnvalue = "Shipped"
        }
        return returnvalue
    }
}