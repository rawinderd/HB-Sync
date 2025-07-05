package com.hook2book.hbsync.Adapters



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hook2book.hbsync.Model.SearchProduct.Data

import com.hook2book.hbsync.R

class ProductListAdapter(
    private val itemClickListener: ItemClickListener, private var productList: MutableList<Data>
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.model_product_list_item, parent, false)
        return ViewHolder(view)
    }

    interface ItemClickListener {
        fun onItemClick(position: Int, id: Int?)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = productList[position]
        holder.book_name.text = ItemsViewModel.book_name
        holder.book_price.text = ItemsViewModel.price
        holder.writer_name.text = ItemsViewModel.writer_name
        holder.product_status.text = getStatusString(ItemsViewModel.status)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setData(data: List<Data>?) {

        if (data != null) {
            for (i in 0..<data.size) {
                productList.add(data.get(i))
            }
        }
        notifyDataSetChanged()
    }

    fun clearList() {
        productList.clear()
        notifyDataSetChanged()
    }


    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val book_name: TextView = itemView.findViewById(R.id.textview_product_name_search)
        val writer_name: TextView = itemView.findViewById(R.id.textview_writer_name)
        val book_price: TextView = itemView.findViewById(R.id.text_view_price_search)
        val product_status: TextView = itemView.findViewById(R.id.productStatus)

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition >= 0) {
                    itemClickListener.onItemClick(
                        bindingAdapterPosition, productList.get(bindingAdapterPosition).product_id
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


