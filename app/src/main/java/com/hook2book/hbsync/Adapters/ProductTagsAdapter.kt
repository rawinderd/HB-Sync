package com.hook2book.hbsync.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hook2book.hbsync.Model.ProductTags.ProductTagsMain
import com.hook2book.hbsync.R


class ProductTagsAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<ProductTagsAdapter.ViewHolder>() {
    private var tagList: MutableList<ProductTagsMain>

    init {
        tagList = ArrayList()
    }


    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.model_tag_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return tagList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = tagList[position]
        holder.tag_name.text = ItemsViewModel.name
    }

    fun setData(data: List<ProductTagsMain>?) {
        if (tagList == null) {
            tagList = ArrayList()
        }

        // tagList.addAll(data)
        if (data != null) {
            for (i in 0..(data.size) - 1) {
                tagList.add(data.get(i))
            }
        }
        notifyDataSetChanged()
    }

    fun RemoveFromAdapter(position: Int) {
        tagList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addTagtoTagList(position: Int, productTagsMain: ProductTagsMain) {
        tagList.add(position, productTagsMain)
        notifyDataSetChanged()
    }

    fun clearList() {
        tagList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tag_name: TextView = itemView.findViewById(R.id.textview_product_tag_name)

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition >= 0) {
                    itemClickListener.onItemClick(bindingAdapterPosition)
                }
            }
        }
    }
}