package com.sikhreader.Model


data class Product( var status: String? = null,
                    var message: String? = null,
                    var data: List<Data>? = null)

class Data(var product_id: Int? = null,
                var book_name: String? = null,
                var writer_name: String? = null,
                var pub_seller_id: String? = null,
                var pages: String? = null,
                var format: String? = null,
                var language: String? = null,
                var wc_product_id: String? = null,
                var price: String? = null,
                var description: String? = null,
                var stock: String? = null,
                var short_description: String? = null,
                var product_type: String? = null,
                var weight: String? = null,
                var height: String? = null,
                var width: String? = null,
                var length: String? = null,
                var shipping_class: String? = null,
                var status: String? = null,
                var visibility: String? = null,
                var remarks: String? = null,
                var related: String? = null,
                var images: List<images>? = null,
                var tags: String? = null,
                var user_type: Int? = null,
                var created_at: String? = null,
                var updated_at: String? = null,
                var deleted_at: String? = null,
                var category: List<category>? = null) {


}
data class images(var imageId: String,var imageUrl:String)

data class category(var categoryId:Int,var categoryName:String)


