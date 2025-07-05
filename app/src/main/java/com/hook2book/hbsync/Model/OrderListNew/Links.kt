package com.hook2book.hbsync.Model.OrderListNew

import com.hook2book.hbsync.Model.CommonModels.Self
import com.hook2book.hbsync.Model.NewTag.Customer

data class Links(
    val collection: List<Collection>,
    val customer: List<Customer>,
    val self: List<Self>
)