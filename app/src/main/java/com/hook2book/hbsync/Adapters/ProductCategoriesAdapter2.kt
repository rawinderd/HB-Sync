package com.hook2book.hbsync.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hook2book.hbsync.Activities.ProductCategories2
import com.hook2book.hbsync.Model.NewTag.CategoriesForListing2
import com.hook2book.hbsync.R


class ProductCategoriesAdapter2(private val itemClickListener: ProductCategories2) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var categoryList: MutableList<CategoriesForListing2> = ArrayList()

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        if (categoryList.get(position).parentId == 0) {
            return 1
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.model_categories_heading, parent, false)
            return ViewHolderCategoryHeading(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.model_categories_list, parent, false)
            return ViewHolderCategoryItem(view)
        }
    }

    override fun getItemCount(): Int {

        return categoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ItemsViewModel = categoryList[position]
        var text = ItemsViewModel.categoryDetail.categoryName
        when (holder) {
            is ViewHolderCategoryItem -> holder.category_name.text = text

            is ViewHolderCategoryHeading -> holder.category_heading.text = text
        }
    }

    fun setData(parentChildCategoriesMix: MutableList<CategoriesForListing2>) {
        if (parentChildCategoriesMix != null) {

            categoryList.addAll(parentChildCategoriesMix)
        }
        notifyDataSetChanged()
    }

    fun RemoveFromAdapter(position: Int) {
        categoryList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addCategorytoCategoryList(position: Int, categoriesForListing: CategoriesForListing2) {
        categoryList.add(position, categoriesForListing)
        notifyDataSetChanged()
    }

    /*fun clearList() {
        categoryList.clear()
        notifyDataSetChanged()
    }*/

    inner class ViewHolderCategoryItem(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var category_name: TextView = itemView.findViewById(R.id.textview_product_category_name)

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition >= 0) {
                    itemClickListener.onItemClick(bindingAdapterPosition)
                }
            }
        }
    }

    inner class ViewHolderCategoryHeading(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var category_heading: TextView =
            itemView.findViewById(R.id.textview_product_category_heading)/*init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition >= 0) {
                    itemClickListener.onItemClick(bindingAdapterPosition)
                }
            }
        }*/
    }
}